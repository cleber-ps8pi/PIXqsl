package moe.zzy040330.taffyqsl.ui.grids

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moe.zzy040330.taffyqsl.data.config.ConfigRepository
import moe.zzy040330.taffyqsl.data.lotw.LotwCredentialManager
import moe.zzy040330.taffyqsl.data.lotw.LotwException
import moe.zzy040330.taffyqsl.data.lotw.LotwQueryParams
import moe.zzy040330.taffyqsl.data.lotw.LotwService
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GridViewModel(application: Application) : AndroidViewModel(application) {

    private val credentialManager = LotwCredentialManager(application)
    private val service = LotwService()
    private val config = ConfigRepository.getInstance(application)
    private val cache = GridCache(application)

    val bands = config.bands
    val modes = config.modes

    private val _hasCredentials = MutableStateFlow(credentialManager.hasCredentials())
    val hasCredentials: StateFlow<Boolean> = _hasCredentials.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?> = _error.asStateFlow()

    private val _hasQueried = MutableStateFlow(false)
    val hasQueried: StateFlow<Boolean> = _hasQueried.asStateFlow()

    private val _fromCache = MutableStateFlow(false)
    val fromCache: StateFlow<Boolean> = _fromCache.asStateFlow()

    private val confirmedRaw = MutableStateFlow<List<Map<String, String>>>(emptyList())

    data class DownloadStats(
        val confirmedQsos: Int = 0,
        val confirmedWithGrid: Int = 0
    )

    private val _downloadStats = MutableStateFlow(DownloadStats())
    val downloadStats = _downloadStats.asStateFlow()

    val satelliteFilter = MutableStateFlow(SatelliteFilter.ALL)
    val bandFilter = MutableStateFlow("")
    val modeFilter = MutableStateFlow("")
    val satNameFilter = MutableStateFlow("")
    val ownCall = MutableStateFlow("")
    /** Client-side grid search (after data loaded). */
    val gridSearch = MutableStateFlow("")
    /** true = full reimport from 1970; false = only newer than cache. */
    val fullReimport = MutableStateFlow(false)

    private data class FilterState(
        val conf: List<Map<String, String>>,
        val satelliteFilter: SatelliteFilter,
        val band: String,
        val mode: String,
        val satName: String,
        val gridSearch: String
    )

    private val filterState = combine(
        confirmedRaw,
        satelliteFilter,
        bandFilter,
        modeFilter,
        satNameFilter
    ) { conf, sf, band, mode, satName ->
        FilterState(conf, sf, band, mode, satName, gridSearch.value)
    }.combine(gridSearch) { st, gs -> st.copy(gridSearch = gs) }

    val entries: StateFlow<List<GridEntry>> = filterState
        .combine(satelliteFilter) { st, _ ->
            val filtered = GridAggregator.filterQsos(
                st.conf, st.band, st.mode, st.satelliteFilter, st.satName.ifBlank { null }
            )
            var list = GridAggregator.aggregate(filtered, emptyList())
            val q = st.gridSearch.trim().uppercase()
            if (q.isNotEmpty()) {
                list = list.filter { entry ->
                    entry.grid.contains(q) ||
                        entry.qsos.any { it.call.contains(q) }
                }
            }
            list
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val stats: StateFlow<GridStats> = entries
        .combine(satelliteFilter) { list, _ -> GridAggregator.stats(list) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GridStats(0, 0, 0))

    val knownSatellites: StateFlow<List<String>> = confirmedRaw
        .combine(satelliteFilter) { conf, _ ->
            conf.mapNotNull { q ->
                q["SAT_NAME"]?.trim()?.takeIf { it.isNotEmpty() }?.uppercase()
            }.distinct().sorted()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        // Load cache immediately so UI is fast
        viewModelScope.launch {
            val snap = withContext(Dispatchers.IO) { cache.load() }
            if (snap != null && snap.qsos.isNotEmpty()) {
                confirmedRaw.value = snap.qsos
                _downloadStats.value = DownloadStats(
                    confirmedQsos = snap.qsos.size,
                    confirmedWithGrid = GridAggregator.countQsosWithGrid(snap.qsos)
                )
                _hasQueried.value = true
                _fromCache.value = true
            }
        }
    }

    fun refreshCredentials() {
        _hasCredentials.value = credentialManager.hasCredentials()
    }

    fun query() {
        viewModelScope.launch {
            if (!credentialManager.hasCredentials()) {
                _hasCredentials.value = false
                return@launch
            }
            val creds = credentialManager.loadCredentials() ?: return@launch
            val user = creds.first
            val pass = creds.second

            _isLoading.value = true
            _error.value = null

            val call = ownCall.value.trim().ifBlank { null }
            val snap = withContext(Dispatchers.IO) { cache.load() }

            // Incremental: start from day after newest cached QSO (or same day to be safe)
            val start: String = if (!fullReimport.value && snap?.newestQsoDate != null) {
                normalizeDate(snap.newestQsoDate) ?: "1970-01-01"
            } else {
                "1970-01-01"
            }

            try {
                val confResult = queryWithRetry(
                    user, pass,
                    LotwQueryParams(
                        qsoQsl = "yes",
                        qsoOwnCall = call,
                        qsoStartDate = start,
                        qsoEndDate = null,
                        qsoQslDetail = true
                    )
                )
                val newList = confResult.getOrElse { throw it }

                val merged = if (!fullReimport.value && snap != null && snap.qsos.isNotEmpty()) {
                    mergeQsos(snap.qsos, newList)
                } else {
                    newList
                }

                confirmedRaw.value = merged
                _downloadStats.value = DownloadStats(
                    confirmedQsos = merged.size,
                    confirmedWithGrid = GridAggregator.countQsosWithGrid(merged)
                )
                withContext(Dispatchers.IO) { cache.save(merged) }
                _hasQueried.value = true
                _fromCache.value = false
            } catch (e: Throwable) {
                _error.value = e
                _hasQueried.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun mergeQsos(
        old: List<Map<String, String>>,
        newer: List<Map<String, String>>
    ): List<Map<String, String>> {
        fun key(q: Map<String, String>): String =
            listOf(
                q["CALL"].orEmpty(),
                q["QSO_DATE"].orEmpty(),
                q["TIME_ON"].orEmpty(),
                q["BAND"].orEmpty(),
                q["MODE"].orEmpty(),
                q["SAT_NAME"].orEmpty()
            ).joinToString("|")

        val map = linkedMapOf<String, Map<String, String>>()
        old.forEach { map[key(it)] = it }
        newer.forEach { map[key(it)] = it }
        return map.values.toList()
    }

    private fun normalizeDate(raw: String): String? {
        val d = raw.replace("-", "")
        if (d.length != 8) return raw.takeIf { it.contains("-") }
        return "${d.substring(0, 4)}-${d.substring(4, 6)}-${d.substring(6, 8)}"
    }

    private suspend fun queryWithRetry(
        user: String,
        pass: String,
        params: LotwQueryParams,
        attempts: Int = 3
    ): Result<List<Map<String, String>>> {
        var last: Result<List<Map<String, String>>> = Result.failure(IllegalStateException("no attempt"))
        repeat(attempts) { index ->
            last = service.query(user, pass, params)
            if (last.isSuccess) return last
            val code = (last.exceptionOrNull() as? LotwException.ServerError)?.httpCode
            if (code == 503 || code == 502 || code == 429) {
                delay(2000L * (index + 1))
            } else {
                return last
            }
        }
        return last
    }

    fun errorMessage(t: Throwable): String = when (t) {
        is LotwException.AuthFailed -> "LoTW: username/password incorrect"
        is LotwException.ServerError -> when (t.httpCode) {
            503, 502 -> "LoTW temporarily unavailable (HTTP ${t.httpCode}). Wait a moment and try again."
            429 -> "LoTW rate limit (HTTP 429). Wait and try again."
            else -> "LoTW server error (HTTP ${t.httpCode})"
        }
        else -> t.message ?: t.toString()
    }
}
