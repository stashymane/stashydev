package locals

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.staticCompositionLocalOf
import coil3.ImageLoader
import screens.Screen

val LocalBackStack: ProvidableCompositionLocal<SnapshotStateList<Screen>> =
    staticCompositionLocalOf { error("LocalBackStack not initialized.") }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
    compositionLocalOf { error("LocalSharedTransitionScope not initialized.") }

val LocalImageLoader: ProvidableCompositionLocal<ImageLoader> =
    staticCompositionLocalOf { error("ImageLoader has not been initialized.") }
