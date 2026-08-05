package moe.zzy040330.taffyqsl.ui.grids

enum class GridConfirmFilter {
    ALL,
    CONFIRMED_ONLY,
    UNCONFIRMED_ONLY
}

enum class SatelliteFilter {
    ALL,
    SATELLITE_ONLY,
    TERRESTRIAL_ONLY
}

data class GridQso(
    val call: String,
    val date: String,
    val time: String,
    val band: String,
    val mode: String,
    val sat: String,
    val confirmed: Boolean,
    val rstSent: String = "",
    val rstRcvd: String = ""
)

data class GridEntry(
    val grid: String,           // 4-char
    val confirmed: Boolean,
    val qsoCount: Int,
    val bands: Set<String>,
    val modes: Set<String>,
    val satellites: Set<String>,
    val lastDate: String?,
    val qsos: List<GridQso> = emptyList()
)

data class GridStats(
    val confirmedCount: Int,
    val unconfirmedCount: Int,
    val totalUnique: Int
)
