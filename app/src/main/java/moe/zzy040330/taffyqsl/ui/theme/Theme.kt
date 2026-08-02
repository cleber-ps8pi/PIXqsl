package moe.zzy040330.taffyqsl.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light mode – soft green on light background
private val LightColorScheme = lightColorScheme(
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

    surfaceBright = PixLightBackground,
    surfaceDim = Color(0xFFD0E0C8),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = PixLightSurface,
    surfaceContainer = Color(0xFFE0ECD8),
    surfaceContainerHigh = Color(0xFFD4E4C8),
    surfaceContainerHighest = Color(0xFFC8D8BC),

    outline = PixLightOutline,
    outlineVariant = Color(0xFFB0C0A4),
    scrim = Color.Black,

    inverseSurface = PixLightText,
    inverseOnSurface = PixLightBackground,
    inversePrimary = PixPrimaryBright,
)

// Dark mode – matches the provided screenshot (lime green on near-black)
private val DarkColorScheme = darkColorScheme(
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

    surfaceBright = PixSurfaceHigh,
    surfaceDim = PixBackground,
    surfaceContainerLowest = Color(0xFF060808),
    surfaceContainerLow = PixSurface,
    surfaceContainer = PixSurfaceContainer,
    surfaceContainerHigh = PixSurfaceHigh,
    surfaceContainerHighest = PixSurfaceHighest,

    outline = PixOutline,
    outlineVariant = PixOutlineVariant,
    scrim = Color.Black,

    inverseSurface = PixText,
    inverseOnSurface = PixBackground,
    inversePrimary = PixPrimaryDark,
)

@Composable
fun TaffyQslTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
