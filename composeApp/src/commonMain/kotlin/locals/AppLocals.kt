package locals

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import coil3.ImageLoader

val LocalNavController: ProvidableCompositionLocal<NavHostController> =
    staticCompositionLocalOf { error("LocalNavController not initialized.") }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
    staticCompositionLocalOf { error("LocalSharedTransitionScope not initialized.") }

val LocalImageLoader: ProvidableCompositionLocal<ImageLoader> =
    staticCompositionLocalOf { error("ImageLoader has not been initialized.") }
