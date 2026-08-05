package moe.zzy040330.taffyqsl.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import moe.zzy040330.taffyqsl.R
import moe.zzy040330.taffyqsl.ui.certificates.CertificatesScreen
import moe.zzy040330.taffyqsl.ui.logs.LogsScreen
import moe.zzy040330.taffyqsl.ui.logs.QsoEditScreen
import moe.zzy040330.taffyqsl.ui.logs.QsoListScreen
import moe.zzy040330.taffyqsl.ui.grids.GridScreen
import moe.zzy040330.taffyqsl.ui.lotw.LotwScreen
import moe.zzy040330.taffyqsl.ui.settings.AboutScreen
import moe.zzy040330.taffyqsl.ui.settings.LicensesScreen
import moe.zzy040330.taffyqsl.ui.settings.SettingsScreen
import moe.zzy040330.taffyqsl.ui.stations.StationsScreen

sealed class Screen(val route: String, val titleResId: Int) {
    data object Certificates : Screen("certificates", R.string.nav_certificates)
    data object Stations : Screen("stations", R.string.nav_stations)
    data object Logs : Screen("logs", R.string.nav_logs)
    data object Lotw : Screen("lotw", R.string.nav_lotw)
    data object Settings : Screen("settings", R.string.nav_settings)
    data object Grids : Screen("grids", R.string.grids_title)
}

private val topLevelRoutes = setOf(
    Screen.Certificates.route,
    Screen.Stations.route,
    Screen.Logs.route,
    Screen.Lotw.route,
    Screen.Settings.route
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaffyQslApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomNav = currentDestination?.route in topLevelRoutes

    Scaffold(
        topBar = {
            if (showBottomNav) {
                TopAppBar(
                    title = { PixqslThemedHeader() },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                val navItemColors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.65f),
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.65f),
                )
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    NavigationBarItem(
                        colors = navItemColors,
                        icon = { Icon(Icons.Default.Description, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_logs)) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Logs.route } == true,
                        onClick = {
                            navController.navigate(Screen.Logs.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        colors = navItemColors,
                        icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_stations)) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Stations.route } == true,
                        onClick = {
                            navController.navigate(Screen.Stations.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    NavigationBarItem(
                        colors = navItemColors,
                        icon = { Icon(Icons.Default.Security, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_certificates)) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Certificates.route } == true,
                        onClick = {
                            navController.navigate(Screen.Certificates.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    NavigationBarItem(
                        colors = navItemColors,
                        icon = { Icon(Icons.Default.CloudUpload, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_lotw)) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Lotw.route } == true,
                        onClick = {
                            navController.navigate(Screen.Lotw.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        colors = navItemColors,
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_settings)) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Settings.route } == true,
                        onClick = {
                            navController.navigate(Screen.Settings.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Logs.route
        ) {
            composable(Screen.Certificates.route) {
                CertificatesScreen(innerPadding)
            }
            composable(Screen.Stations.route) {
                StationsScreen(innerPadding)
            }
            composable(Screen.Logs.route) {
                LogsScreen(innerPadding, navController)
            }
            composable(Screen.Lotw.route) {
                LotwScreen(
                    innerPadding = innerPadding,
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    },
                    onNavigateToGrids = {
                        navController.navigate(Screen.Grids.route)
                    }
                )
            }
            composable(Screen.Grids.route) {
                GridScreen(navController = navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(innerPadding, navController)
            }
            composable("about") {
                AboutScreen(navController)
            }
            composable("licenses") {
                LicensesScreen(navController)
            }
            composable(
                route = "qso_list/{fileName}",
                arguments = listOf(
                    navArgument("fileName") { type = NavType.StringType }
                )
            ) {
                QsoListScreen(navController = navController)
            }
            composable(
                route = "qso_edit/{fileName}/{qsoId}",
                arguments = listOf(
                    navArgument("fileName") { type = NavType.StringType },
                    navArgument("qsoId") { type = NavType.LongType }
                )
            ) {
                QsoEditScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun PixqslThemedHeader() {
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AntennaIcon(
                color = primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "PIXqsl",
                    color = primary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "by Cleber (PS8PI)",
                    color = primary.copy(alpha = 0.85f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        DottedWorldMap(
            color = primary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(150.dp)
                .height(48.dp)
                .padding(end = 4.dp)
        )
    }
}

@Composable
private fun AntennaIcon(color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.40f
        val stroke = (w * 0.065f).coerceAtLeast(2f)

        // signal arcs
        drawArc(
            color = color.copy(alpha = 0.28f),
            startAngle = -48f,
            sweepAngle = 52f,
            useCenter = false,
            topLeft = Offset(cx - w * 0.02f, h * 0.06f),
            size = androidx.compose.ui.geometry.Size(w * 0.58f, h * 0.42f),
            style = Stroke(width = stroke * 0.7f, cap = StrokeCap.Round)
        )
        drawArc(
            color = color.copy(alpha = 0.48f),
            startAngle = -42f,
            sweepAngle = 46f,
            useCenter = false,
            topLeft = Offset(cx, h * 0.14f),
            size = androidx.compose.ui.geometry.Size(w * 0.42f, h * 0.32f),
            style = Stroke(width = stroke * 0.75f, cap = StrokeCap.Round)
        )

        // lattice tower legs
        val topY = h * 0.22f
        val baseY = h * 0.86f
        val halfTop = w * 0.035f
        val halfBase = w * 0.17f
        drawLine(color, Offset(cx - halfTop, topY), Offset(cx - halfBase, baseY), stroke, StrokeCap.Round)
        drawLine(color, Offset(cx + halfTop, topY), Offset(cx + halfBase, baseY), stroke, StrokeCap.Round)

        // horizontal braces
        for (t in listOf(0.32f, 0.50f, 0.68f)) {
            val y = topY + (baseY - topY) * t
            val spread = halfTop + (halfBase - halfTop) * t
            drawLine(
                color.copy(alpha = 0.95f),
                Offset(cx - spread, y),
                Offset(cx + spread, y),
                stroke * 0.72f,
                StrokeCap.Round
            )
        }

        // X braces
        val y1 = topY + (baseY - topY) * 0.32f
        val y2 = topY + (baseY - topY) * 0.50f
        val s1 = halfTop + (halfBase - halfTop) * 0.32f
        val s2 = halfTop + (halfBase - halfTop) * 0.50f
        drawLine(color.copy(alpha = 0.75f), Offset(cx - s1, y1), Offset(cx + s2, y2), stroke * 0.5f, StrokeCap.Round)
        drawLine(color.copy(alpha = 0.75f), Offset(cx + s1, y1), Offset(cx - s2, y2), stroke * 0.5f, StrokeCap.Round)

        val y3 = topY + (baseY - topY) * 0.50f
        val y4 = topY + (baseY - topY) * 0.68f
        val s3 = halfTop + (halfBase - halfTop) * 0.50f
        val s4 = halfTop + (halfBase - halfTop) * 0.68f
        drawLine(color.copy(alpha = 0.75f), Offset(cx - s3, y3), Offset(cx + s4, y4), stroke * 0.5f, StrokeCap.Round)
        drawLine(color.copy(alpha = 0.75f), Offset(cx + s3, y3), Offset(cx - s4, y4), stroke * 0.5f, StrokeCap.Round)

        // top beacon
        drawCircle(color = color, radius = w * 0.065f, center = Offset(cx, h * 0.13f))
        drawLine(color, Offset(cx, h * 0.13f + w * 0.065f), Offset(cx, topY), stroke * 0.75f, StrokeCap.Round)

        // base plates
        drawLine(color, Offset(cx - halfBase * 1.08f, baseY), Offset(cx + halfBase * 1.08f, baseY), stroke * 1.2f, StrokeCap.Round)
        drawLine(
            color.copy(alpha = 0.85f),
            Offset(cx - halfBase * 0.95f, baseY + h * 0.045f),
            Offset(cx + halfBase * 0.95f, baseY + h * 0.045f),
            stroke * 0.9f,
            StrokeCap.Round
        )
    }
}

@Composable
private fun DottedWorldMap(color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stepX = w / 18f
        val stepY = h / 10f
        val r = minOf(stepX, stepY) * 0.28f

        fun inLand(nx: Float, ny: Float): Boolean {
            if ((nx - 0.28f) * (nx - 0.28f) / 0.04f + (ny - 0.55f) * (ny - 0.55f) / 0.12f <= 1f) return true
            if ((nx - 0.28f) * (nx - 0.28f) / 0.045f + (ny - 0.28f) * (ny - 0.28f) / 0.03f <= 1f) return true
            if ((nx - 0.42f) * (nx - 0.42f) / 0.02f + (ny - 0.48f) * (ny - 0.48f) / 0.04f <= 1f) return true
            if ((nx - 0.58f) * (nx - 0.58f) / 0.05f + (ny - 0.38f) * (ny - 0.38f) / 0.05f <= 1f) return true
            if ((nx - 0.72f) * (nx - 0.72f) / 0.008f + (ny - 0.32f) * (ny - 0.32f) / 0.02f <= 1f) return true
            if ((nx - 0.72f) * (nx - 0.72f) / 0.03f + (ny - 0.78f) * (ny - 0.78f) / 0.025f <= 1f) return true
            return false
        }

        var row = 0
        var y = stepY * 0.5f
        while (y < h) {
            var x = stepX * 0.5f + (if (row % 2 == 0) 0f else stepX * 0.5f)
            while (x < w) {
                val nx = x / w
                val ny = y / h
                if (inLand(nx, ny)) {
                    val alpha = 0.55f + 0.35f * (1f - ny)
                    drawCircle(
                        color = color.copy(alpha = alpha.coerceIn(0.35f, 0.9f)),
                        radius = r,
                        center = Offset(x, y)
                    )
                }
                x += stepX
            }
            y += stepY
            row++
        }
    }
}
