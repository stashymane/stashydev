package ui.locals

import AppBackStack
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import coil3.ImageLoader

val LocalBackStack: ProvidableCompositionLocal<AppBackStack> =
    staticCompositionLocalOf { error("LocalBackStack not initialized.") }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
    compositionLocalOf { error("LocalSharedTransitionScope not initialized.") }

val LocalImageLoader: ProvidableCompositionLocal<ImageLoader> =
    staticCompositionLocalOf { error("ImageLoader has not been initialized.") }

val LocalScrollConnection: ProvidableCompositionLocal<NestedScrollConnection?> =
    compositionLocalOf { null }

val LocalScaffoldPadding: ProvidableCompositionLocal<PaddingValues> =
    compositionLocalOf { PaddingValues() }
