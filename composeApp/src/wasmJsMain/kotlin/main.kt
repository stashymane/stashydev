import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToNavigation
import androidx.navigation.compose.rememberNavController
import kotlinx.browser.document
import kotlinx.browser.window
import locals.LocalNavController
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    startKoin {
        modules(KoinModule)
    }

    ComposeViewport(document.body!!) {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavController provides navController) {
            App()
        }
        LaunchedEffect(Unit) {
            window.bindToNavigation(navController)
        }
    }
}
