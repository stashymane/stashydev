package locals

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController: ProvidableCompositionLocal<NavHostController> =
    staticCompositionLocalOf { throw RuntimeException("LocalNavController not initialized.") }