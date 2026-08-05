package moe.zzy040330.taffyqsl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import moe.zzy040330.taffyqsl.data.AppAppearanceMode
import moe.zzy040330.taffyqsl.data.AppPreferences
import moe.zzy040330.taffyqsl.ui.TaffyQslApp
import moe.zzy040330.taffyqsl.ui.theme.TaffyQslTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val prefs = remember { AppPreferences.getInstance(this) }
            val colorTheme = prefs.colorTheme
            val darkTheme = when (prefs.appearanceMode) {
                AppAppearanceMode.SYSTEM -> isSystemInDarkTheme()
                AppAppearanceMode.LIGHT -> false
                AppAppearanceMode.DARK -> true
            }
            TaffyQslTheme(colorTheme = colorTheme, darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaffyQslApp()
                }
            }
        }
    }
}
