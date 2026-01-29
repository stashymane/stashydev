package ui.theme

import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.scaleToBounds
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import ui.locals.LocalSharedTransitionScope


@Composable
fun Modifier.navBlockSharedBounds(type: String): Modifier =
    this then with(LocalSharedTransitionScope.current) {
        Modifier.sharedBounds(
            rememberSharedContentState("nav-$type"),
            LocalNavAnimatedContentScope.current,
            resizeMode = scaleToBounds(ContentScale.Crop, Alignment.TopStart),
        )
    }

@Composable
fun Modifier.navTitleSharedElement(type: String): Modifier =
    this then with(LocalSharedTransitionScope.current) {
        Modifier.sharedElement(
            rememberSharedContentState("nav-$type"),
            LocalNavAnimatedContentScope.current
        )
    }
