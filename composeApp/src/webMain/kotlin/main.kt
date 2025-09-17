import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.compose.rememberNavController
import kotlinx.browser.document
import locals.LocalNavController
import org.koin.compose.KoinApplication

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        KoinApplication(
            application = {
                modules(KoinModule)
            }
        ) {
            val navController = rememberNavController()
            CompositionLocalProvider(LocalNavController provides navController) {
                App()
            }
        }
    }
}
