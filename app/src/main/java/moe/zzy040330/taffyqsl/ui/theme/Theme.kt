package moe.zzy040330.taffyqsl.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import moe.zzy040330.taffyqsl.data.AppColorTheme

// ---------------------------------------------------------------------------
// GREEN (PIXqsl default)
// ---------------------------------------------------------------------------
private val GreenLight = lightColorScheme(
    primary = PixLightPrimary,
    onPrimary = PixLightOnPrimary,
    primaryContainer = PixLightPrimaryContainer,
    onPrimaryContainer = PixLightOnPrimaryContainer,
    secondary = PixPrimaryDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC8E8A0),
    onSecondaryContainer = Color(0xFF1A300A),
    tertiary = PixTertiary,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB0E8D8),
    onTertiaryContainer = Color(0xFF0A2A20),
    error = PixError,
    onError = Color.White,
    errorContainer = Color(0xFFFFDADA),
    onErrorContainer = Color(0xFF4A1010),
    background = PixLightBackground,
    onBackground = PixLightText,
    surface = PixLightSurface,
    onSurface = PixLightText,
    surfaceVariant = Color(0xFFD8E8D0),
    onSurfaceVariant = PixLightTextSecondary,
    surfaceTint = PixLightPrimary,
    outline = PixLightOutline,
    outlineVariant = Color(0xFFB0C0A4),
    scrim = Color.Black,
    inverseSurface = PixLightText,
    inverseOnSurface = PixLightBackground,
    inversePrimary = PixPrimaryBright,
)

private val GreenDark = darkColorScheme(
    primary = PixPrimary,
    onPrimary = PixOnPrimary,
    primaryContainer = PixPrimaryContainer,
    onPrimaryContainer = PixOnPrimaryContainer,
    secondary = PixPrimaryBright,
    onSecondary = PixOnPrimary,
    secondaryContainer = PixSecondaryContainer,
    onSecondaryContainer = PixOnSecondaryContainer,
    tertiary = PixTertiary,
    onTertiary = PixOnPrimary,
    tertiaryContainer = PixTertiaryContainer,
    onTertiaryContainer = Color(0xFFA0E8D0),
    error = PixError,
    onError = Color.White,
    errorContainer = PixErrorContainer,
    onErrorContainer = PixOnErrorContainer,
    background = PixBackground,
    onBackground = PixText,
    surface = PixSurface,
    onSurface = PixText,
    surfaceVariant = PixSurfaceVariant,
    onSurfaceVariant = PixTextSecondary,
    surfaceTint = PixPrimary,
    outline = PixOutline,
    outlineVariant = PixOutlineVariant,
    scrim = Color.Black,
    inverseSurface = PixText,
    inverseOnSurface = PixBackground,
    inversePrimary = PixPrimaryDark,
)

// ---------------------------------------------------------------------------
// PURPLE (Catppuccin-inspired)
// ---------------------------------------------------------------------------
private val PurpleLight = lightColorScheme(
    primary = Color(0xFF8839EF),
    onPrimary = Color(0xFFEFF1F5),
    primaryContainer = Color(0xFFEDE7FF),
    onPrimaryContainer = Color(0xFF35003F),
    secondary = Color(0xFF209FB5),
    onSecondary = Color(0xFFEFF1F5),
    secondaryContainer = Color(0xFFCAEEF6),
    onSecondaryContainer = Color(0xFF001F28),
    tertiary = Color(0xFFEA76CB),
    onTertiary = Color(0xFFEFF1F5),
    tertiaryContainer = Color(0xFFFFD7F4),
    onTertiaryContainer = Color(0xFF35003F),
    error = Color(0xFFD20F39),
    onError = Color.White,
    errorContainer = Color(0xFFFFD9DC),
    onErrorContainer = Color(0xFF40000B),
    background = Color(0xFFEFF1F5),
    onBackground = Color(0xFF4C4F69),
    surface = Color(0xFFEFF1F5),
    onSurface = Color(0xFF4C4F69),
    surfaceVariant = Color(0xFFCCD0DA),
    onSurfaceVariant = Color(0xFF6C6F85),
    surfaceTint = Color(0xFF8839EF),
    outline = Color(0xFF6C6F85),
    outlineVariant = Color(0xFF7C7F93),
    scrim = Color.Black,
)

private val PurpleDark = darkColorScheme(
    primary = Color(0xFFCBA6F7),
    onPrimary = Color(0xFF1E1E2E),
    primaryContainer = Color(0xFF4D3572),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFF89B4FA),
    onSecondary = Color(0xFF1E1E2E),
    secondaryContainer = Color(0xFF2B3D65),
    onSecondaryContainer = Color(0xFFD8E2FF),
    tertiary = Color(0xFFF5C2E7),
    onTertiary = Color(0xFF1E1E2E),
    tertiaryContainer = Color(0xFF5C2D55),
    onTertiaryContainer = Color(0xFFFFD7F4),
    error = Color(0xFFF38BA8),
    onError = Color(0xFF1E1E2E),
    errorContainer = Color(0xFF5C1A27),
    onErrorContainer = Color(0xFFFFD9DC),
    background = Color(0xFF1E1E2E),
    onBackground = Color(0xFFCDD6F4),
    surface = Color(0xFF1E1E2E),
    onSurface = Color(0xFFCDD6F4),
    surfaceVariant = Color(0xFF313244),
    onSurfaceVariant = Color(0xFFBAC2DE),
    surfaceTint = Color(0xFFCBA6F7),
    outline = Color(0xFF9399B2),
    outlineVariant = Color(0xFF7F849C),
    scrim = Color.Black,
)

// ---------------------------------------------------------------------------
// BLUE
// ---------------------------------------------------------------------------
private val BlueLight = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = Color(0xFF0277BD),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCAE6FF),
    onSecondaryContainer = Color(0xFF001E30),
    tertiary = Color(0xFF00838F),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB2EBF2),
    onTertiaryContainer = Color(0xFF002022),
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = Color(0xFFF8F9FF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFF8F9FF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDEE3EB),
    onSurfaceVariant = Color(0xFF42474E),
    surfaceTint = Color(0xFF1565C0),
    outline = Color(0xFF72777F),
    outlineVariant = Color(0xFFC2C7CF),
    scrim = Color.Black,
)

private val BlueDark = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF004881),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = Color(0xFF81D4FA),
    onSecondary = Color(0xFF00344A),
    secondaryContainer = Color(0xFF004D6B),
    onSecondaryContainer = Color(0xFFC8E7FF),
    tertiary = Color(0xFF4DD0E1),
    onTertiary = Color(0xFF00363D),
    tertiaryContainer = Color(0xFF004F58),
    onTertiaryContainer = Color(0xFFB2EBF2),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    background = Color(0xFF0B121A),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF0B121A),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF1C2834),
    onSurfaceVariant = Color(0xFFC2C7CF),
    surfaceTint = Color(0xFF90CAF9),
    outline = Color(0xFF8C9198),
    outlineVariant = Color(0xFF42474E),
    scrim = Color.Black,
)

// ---------------------------------------------------------------------------
// AMBER
// ---------------------------------------------------------------------------
private val AmberLight = lightColorScheme(
    primary = Color(0xFFE65100),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDBCC),
    onPrimaryContainer = Color(0xFF351000),
    secondary = Color(0xFFF9A825),
    onSecondary = Color(0xFF3B2F00),
    secondaryContainer = Color(0xFFFFE08A),
    onSecondaryContainer = Color(0xFF241A00),
    tertiary = Color(0xFFBF360C),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDAD2),
    onTertiaryContainer = Color(0xFF3C0800),
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = Color(0xFFFFF8F4),
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFFFF8F4),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFF4DED5),
    onSurfaceVariant = Color(0xFF52443D),
    surfaceTint = Color(0xFFE65100),
    outline = Color(0xFF85736C),
    outlineVariant = Color(0xFFD7C2B9),
    scrim = Color.Black,
)

private val AmberDark = darkColorScheme(
    primary = Color(0xFFFFB74D),
    onPrimary = Color(0xFF4A2800),
    primaryContainer = Color(0xFF6A3B00),
    onPrimaryContainer = Color(0xFFFFDDB8),
    secondary = Color(0xFFFFD54F),
    onSecondary = Color(0xFF3B2F00),
    secondaryContainer = Color(0xFF564500),
    onSecondaryContainer = Color(0xFFFFE08A),
    tertiary = Color(0xFFFF8A65),
    onTertiary = Color(0xFF5F1600),
    tertiaryContainer = Color(0xFF862200),
    onTertiaryContainer = Color(0xFFFFDAD2),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    background = Color(0xFF14100C),
    onBackground = Color(0xFFECE0D9),
    surface = Color(0xFF14100C),
    onSurface = Color(0xFFECE0D9),
    surfaceVariant = Color(0xFF2A211C),
    onSurfaceVariant = Color(0xFFD7C2B9),
    surfaceTint = Color(0xFFFFB74D),
    outline = Color(0xFFA08D85),
    outlineVariant = Color(0xFF52443D),
    scrim = Color.Black,
)

private fun colorSchemeFor(theme: AppColorTheme, dark: Boolean): ColorScheme {
    return when (theme) {
        AppColorTheme.GREEN -> if (dark) GreenDark else GreenLight
        AppColorTheme.PURPLE -> if (dark) PurpleDark else PurpleLight
        AppColorTheme.BLUE -> if (dark) BlueDark else BlueLight
        AppColorTheme.AMBER -> if (dark) AmberDark else AmberLight
    }
}

@Composable
fun TaffyQslTheme(
    colorTheme: AppColorTheme = AppColorTheme.GREEN,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorSchemeFor(colorTheme, darkTheme),
        typography = Typography,
        content = content
    )
}
