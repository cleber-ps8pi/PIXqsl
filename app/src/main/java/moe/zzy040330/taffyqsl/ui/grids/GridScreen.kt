package moe.zzy040330.taffyqsl.ui.grids

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import moe.zzy040330.taffyqsl.R

private val ConfirmedColor = Color(0xFF6BCB3C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridScreen(
    navController: NavController,
    vm: GridViewModel = viewModel()
) {
    val hasCredentials by vm.hasCredentials.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    val hasQueried by vm.hasQueried.collectAsState()
    val fromCache by vm.fromCache.collectAsState()
    val entries by vm.entries.collectAsState()
    val stats by vm.stats.collectAsState()
    val downloadStats by vm.downloadStats.collectAsState()
    val knownSats by vm.knownSatellites.collectAsState()

    val satFilter by vm.satelliteFilter.collectAsState()
    val bandFilter by vm.bandFilter.collectAsState()
    val modeFilter by vm.modeFilter.collectAsState()
    val satName by vm.satNameFilter.collectAsState()
    val ownCall by vm.ownCall.collectAsState()
    val gridSearch by vm.gridSearch.collectAsState()
    val fullReimport by vm.fullReimport.collectAsState()

    var filtersExpanded by remember { mutableStateOf(false) }
    // Collapse filters when sync finishes
    LaunchedEffect(isLoading) {
        if (!isLoading && hasQueried) {
            filtersExpanded = false
        }
    }
    var selectedEntry by remember { mutableStateOf<GridEntry?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.grids_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { vm.query() },
                        enabled = hasCredentials && !isLoading
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.grids_query))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (!hasCredentials) {
                Text(
                    text = stringResource(R.string.lotw_no_credentials_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Button(onClick = { navController.navigate("settings") }) {
                    Text(stringResource(R.string.lotw_go_to_settings))
                }
                return@Column
            }

            Text(
                text = stringResource(R.string.grids_confirmed_count, stats.confirmedCount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ConfirmedColor
            )
            if (hasQueried) {
                Text(
                    text = stringResource(
                        R.string.grids_download_stats_simple,
                        downloadStats.confirmedQsos,
                        downloadStats.confirmedWithGrid
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (fromCache) {
                    Text(
                        text = stringResource(R.string.grids_from_cache),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Filters collapsible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { filtersExpanded = !filtersExpanded }
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.grids_filters),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (filtersExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }

            if (filtersExpanded) {
                OutlinedTextField(
                    value = ownCall,
                    onValueChange = { vm.ownCall.value = it.uppercase() },
                    label = { Text(stringResource(R.string.grids_own_call_optional)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                FilterDropdown(
                    label = stringResource(R.string.grids_filter_satellite),
                    value = when (satFilter) {
                        SatelliteFilter.ALL -> stringResource(R.string.grids_filter_sat_all)
                        SatelliteFilter.SATELLITE_ONLY -> stringResource(R.string.grids_filter_sat_only)
                        SatelliteFilter.TERRESTRIAL_ONLY -> stringResource(R.string.grids_filter_terrestrial)
                    },
                    options = listOf(
                        SatelliteFilter.ALL to stringResource(R.string.grids_filter_sat_all),
                        SatelliteFilter.SATELLITE_ONLY to stringResource(R.string.grids_filter_sat_only),
                        SatelliteFilter.TERRESTRIAL_ONLY to stringResource(R.string.grids_filter_terrestrial)
                    ),
                    onSelect = { vm.satelliteFilter.value = it }
                )
                val bandOptions = listOf("" to stringResource(R.string.lotw_filter_any)) +
                    vm.bands.map { it.name to it.name }
                FilterDropdown(
                    label = stringResource(R.string.band),
                    value = bandFilter.ifBlank { stringResource(R.string.lotw_filter_any) },
                    options = bandOptions,
                    onSelect = { vm.bandFilter.value = it }
                )
                val modeOptions = listOf("" to stringResource(R.string.lotw_filter_any)) +
                    vm.modes.map { it.name to it.name }
                FilterDropdown(
                    label = stringResource(R.string.mode),
                    value = modeFilter.ifBlank { stringResource(R.string.lotw_filter_any) },
                    options = modeOptions,
                    onSelect = { vm.modeFilter.value = it }
                )
                if (knownSats.isNotEmpty()) {
                    val satOptions = listOf("" to stringResource(R.string.lotw_filter_any)) +
                        knownSats.map { it to it }
                    FilterDropdown(
                        label = stringResource(R.string.grids_filter_sat_name),
                        value = satName.ifBlank { stringResource(R.string.lotw_filter_any) },
                        options = satOptions,
                        onSelect = { vm.satNameFilter.value = it }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vm.fullReimport.value = false }
                ) {
                    Checkbox(
                        checked = !fullReimport,
                        onCheckedChange = { vm.fullReimport.value = !it }
                    )
                    Text(
                        text = stringResource(R.string.grids_import_new_only),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vm.fullReimport.value = true }
                ) {
                    Checkbox(
                        checked = fullReimport,
                        onCheckedChange = { vm.fullReimport.value = it }
                    )
                    Text(
                        text = stringResource(R.string.grids_import_full),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { vm.query() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(stringResource(R.string.grids_query))
            }

            error?.let {
                Text(
                    text = vm.errorMessage(it),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            // Grid search after data is available
            if (hasQueried && !isLoading) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = gridSearch,
                    onValueChange = { vm.gridSearch.value = it.uppercase().take(15) },
                    label = { Text(stringResource(R.string.grids_search_label)) },
                    placeholder = { Text(stringResource(R.string.grids_search_hint)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(8.dp))

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                !hasQueried -> {
                    Text(
                        text = stringResource(R.string.grids_query_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
                entries.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.grids_no_results),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(entries, key = { it.grid }) { entry ->
                            GridRow(entry) { selectedEntry = entry }
                        }
                    }
                }
            }
        }
    }

    selectedEntry?.let { entry ->
        ModalBottomSheet(
            onDismissRequest = { selectedEntry = null },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.grids_contacts_title, entry.grid),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.grids_qso_count, entry.qsoCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))
                if (entry.qsos.isEmpty()) {
                    Text(stringResource(R.string.grids_no_contacts))
                } else {
                    LazyColumn(
                        modifier = Modifier.height(360.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(entry.qsos) { qso ->
                            ContactRow(qso)
                        }
                    }
                }
                TextButton(onClick = { selectedEntry = null }) {
                    Text(stringResource(R.string.close))
                }
            }
        }
    }
}

@Composable
private fun ContactRow(qso: GridQso) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = qso.call,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            if (qso.confirmed) {
                Text(
                    text = "QSL",
                    style = MaterialTheme.typography.labelMedium,
                    color = ConfirmedColor
                )
            }
        }
        val line = buildString {
            val d = qso.date.replace("-", "")
            if (d.length == 8) append("${d.substring(0,4)}-${d.substring(4,6)}-${d.substring(6,8)}")
            else if (qso.date.isNotEmpty()) append(qso.date)
            val t = qso.time.padEnd(4, '0')
            if (t.length >= 4) {
                if (isNotEmpty()) append(" ")
                append("${t.substring(0,2)}:${t.substring(2,4)} UTC")
            }
            if (qso.band.isNotEmpty()) {
                if (isNotEmpty()) append(" · ")
                append(qso.band)
            }
            if (qso.mode.isNotEmpty()) {
                if (isNotEmpty()) append(" · ")
                append(qso.mode)
            }
            if (qso.sat.isNotEmpty()) {
                if (isNotEmpty()) append(" · SAT ")
                append(qso.sat)
            }
        }
        if (line.isNotEmpty()) {
            Text(
                text = line,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun GridRow(entry: GridEntry, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(ConfirmedColor)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.grid,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                val detail = buildString {
                    append(stringResource(R.string.grids_qso_count, entry.qsoCount))
                    if (entry.bands.isNotEmpty()) {
                        append(" · ")
                        append(entry.bands.sorted().joinToString(", "))
                    }
                    if (entry.satellites.isNotEmpty()) {
                        append(" · SAT ")
                        append(entry.satellites.sorted().joinToString(", "))
                    }
                }
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> FilterDropdown(
    label: String,
    value: String,
    options: List<Pair<T, String>>,
    onSelect: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            singleLine = true
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (key, labelText) ->
                DropdownMenuItem(
                    text = { Text(labelText) },
                    onClick = {
                        onSelect(key)
                        expanded = false
                    }
                )
            }
        }
    }
}
