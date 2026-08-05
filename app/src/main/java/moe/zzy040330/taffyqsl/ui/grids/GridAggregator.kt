package moe.zzy040330.taffyqsl.ui.grids

object GridAggregator {

    private val GRID4 = Regex("[A-R]{2}[0-9]{2}", RegexOption.IGNORE_CASE)

    /** Possible ADIF keys that may hold a worked grid. */
    private val GRID_KEYS = listOf(
        "GRIDSQUARE",
        "VUCC_GRIDS",
        "APP_LOTW_GRIDSQUARE",
        "APP_LOTW_2XQSL_GRIDSQUARE"
    )

    fun extractGridsFromQso(qso: Map<String, String>): List<String> {
        val found = linkedSetOf<String>()
        for (key in GRID_KEYS) {
            val raw = qso[key]?.trim().orEmpty()
            if (raw.isEmpty()) continue
            // May be "FN31,FN32" or "FN31 FN32"
            for (part in raw.split(',', ' ', ';', '\t')) {
                val p = part.trim().uppercase()
                if (p.length >= 4) {
                    val g4 = p.take(4)
                    if (GRID4.matches(g4)) found.add(g4)
                }
            }
        }
        // Fallback: scan all values for a grid-looking token
        if (found.isEmpty()) {
            for ((k, v) in qso) {
                if (k.contains("CALL", ignoreCase = true)) continue
                val m = GRID4.find(v.uppercase())
                if (m != null && k.contains("GRID", ignoreCase = true)) {
                    found.add(m.value.take(4))
                }
            }
        }
        return found.toList()
    }

    fun aggregate(
        confirmedQsos: List<Map<String, String>>,
        unconfirmedQsos: List<Map<String, String>>
    ): List<GridEntry> {
        data class Acc(
            var confirmed: Boolean = false,
            var count: Int = 0,
            val bands: MutableSet<String> = mutableSetOf(),
            val modes: MutableSet<String> = mutableSetOf(),
            val sats: MutableSet<String> = mutableSetOf(),
            var lastDate: String? = null,
            val qsos: MutableList<GridQso> = mutableListOf()
        )

        val map = linkedMapOf<String, Acc>()

        fun toQso(q: Map<String, String>, asConfirmed: Boolean): GridQso {
            val sat = q["SAT_NAME"]?.trim().orEmpty()
            val prop = q["PROP_MODE"]?.trim()?.uppercase().orEmpty()
            val satLabel = when {
                sat.isNotEmpty() -> sat.uppercase()
                prop == "SAT" -> "SAT"
                else -> ""
            }
            return GridQso(
                call = q["CALL"]?.trim()?.uppercase().orEmpty().ifBlank { "?" },
                date = q["QSO_DATE"]?.trim().orEmpty(),
                time = q["TIME_ON"]?.trim().orEmpty(),
                band = q["BAND"]?.trim()?.uppercase().orEmpty(),
                mode = q["MODE"]?.trim()?.uppercase().orEmpty(),
                sat = satLabel,
                confirmed = asConfirmed,
                rstSent = q["RST_SENT"]?.trim().orEmpty(),
                rstRcvd = q["RST_RCVD"]?.trim().orEmpty()
            )
        }

        fun ingest(qsos: List<Map<String, String>>, asConfirmed: Boolean) {
            for (q in qsos) {
                val grids = extractGridsFromQso(q)
                if (grids.isEmpty()) continue

                val band = q["BAND"]?.trim()?.uppercase().orEmpty()
                val mode = q["MODE"]?.trim()?.uppercase().orEmpty()
                val sat = q["SAT_NAME"]?.trim().orEmpty()
                val prop = q["PROP_MODE"]?.trim()?.uppercase().orEmpty()
                val satLabel = when {
                    sat.isNotEmpty() -> sat.uppercase()
                    prop == "SAT" -> "SAT"
                    else -> ""
                }
                val date = q["QSO_DATE"]?.trim()
                val gq = toQso(q, asConfirmed)

                for (g4 in grids) {
                    val acc = map.getOrPut(g4) { Acc() }
                    acc.count++
                    if (asConfirmed) acc.confirmed = true
                    if (band.isNotEmpty()) acc.bands.add(band)
                    if (mode.isNotEmpty()) acc.modes.add(mode)
                    if (satLabel.isNotEmpty()) acc.sats.add(satLabel)
                    if (date != null && (acc.lastDate == null || date > acc.lastDate!!)) {
                        acc.lastDate = date
                    }
                    acc.qsos.add(gq)
                }
            }
        }

        ingest(unconfirmedQsos, asConfirmed = false)
        ingest(confirmedQsos, asConfirmed = true)

        return map.entries
            .map { (grid, a) ->
                GridEntry(
                    grid = grid,
                    confirmed = a.confirmed,
                    qsoCount = a.count,
                    bands = a.bands,
                    modes = a.modes,
                    satellites = a.sats,
                    lastDate = a.lastDate,
                    qsos = a.qsos.sortedWith(
                        compareByDescending<GridQso> { it.date }.thenByDescending { it.time }
                    )
                )
            }
            .sortedWith(compareByDescending<GridEntry> { it.confirmed }.thenBy { it.grid })
    }

    fun isSatelliteQso(qso: Map<String, String>): Boolean {
        val sat = qso["SAT_NAME"]?.trim().orEmpty()
        val prop = qso["PROP_MODE"]?.trim()?.uppercase().orEmpty()
        return sat.isNotEmpty() || prop == "SAT"
    }

    fun filterQsos(
        qsos: List<Map<String, String>>,
        band: String?,
        mode: String?,
        satFilter: SatelliteFilter,
        satName: String?
    ): List<Map<String, String>> {
        return qsos.filter { q ->
            if (!band.isNullOrBlank()) {
                val b = q["BAND"]?.trim()?.uppercase().orEmpty()
                if (b != band.uppercase()) return@filter false
            }
            if (!mode.isNullOrBlank()) {
                val m = q["MODE"]?.trim()?.uppercase().orEmpty()
                if (m != mode.uppercase()) return@filter false
            }
            val isSat = isSatelliteQso(q)
            when (satFilter) {
                SatelliteFilter.ALL -> Unit
                SatelliteFilter.SATELLITE_ONLY -> if (!isSat) return@filter false
                SatelliteFilter.TERRESTRIAL_ONLY -> if (isSat) return@filter false
            }
            if (!satName.isNullOrBlank()) {
                val name = q["SAT_NAME"]?.trim()?.uppercase().orEmpty()
                if (name != satName.uppercase()) return@filter false
            }
            true
        }
    }

    fun stats(entries: List<GridEntry>): GridStats {
        val conf = entries.count { it.confirmed }
        val unconf = entries.count { !it.confirmed }
        return GridStats(conf, unconf, entries.size)
    }

    fun countQsosWithGrid(qsos: List<Map<String, String>>): Int =
        qsos.count { extractGridsFromQso(it).isNotEmpty() }
}
