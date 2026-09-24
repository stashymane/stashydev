package ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import ui.LocalSharedTransitionScope

@Composable
fun Modifier.navigationSharedElement(): Modifier =
    this then with(LocalSharedTransitionScope.current) {
        Modifier.sharedElement(
            rememberSharedContentState("nav-content"),
            LocalNavAnimatedContentScope.current,
        )
    }
