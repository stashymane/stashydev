package ui.components.nav

import AppBackStack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.HazeProgressive
import dev.chrisbanes.haze.blur.blurEffect
import dev.chrisbanes.haze.hazeEffect
import icons.Icons
import icons.filled.ChevronBackward
import model.NavEntry
import model.Screen
import org.jetbrains.compose.resources.stringResource
import ui.LocalBackStack
import ui.LocalContainerSize
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.ContainerSize
import ui.theme.easeVerticalGradient
import ui.theme.inDp

@OptIn(ExperimentalGridApi::class)
@Composable
fun NavBar(
    modifier: Modifier = Modifier.height(80.dp)
) {
    val backStack = LocalBackStack.current
    val containerSize = LocalContainerSize.current

    Grid(
        {
            column(64.dp)

            column(1.fr)
            column(1.fr)
            column(1.fr)

            row(1.fr)

            gap(8.dp)
        },
        modifier.padding(8.dp)
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            val iconSize = LocalTextStyle.current.lineHeight.inDp()

            NavLink(
                title = {},
                icon = { Icon(Icons.Filled.ChevronBackward, "back", Modifier.size(iconSize)) },
                isActive = false,
                modifier = Modifier.fillMaxSize().aspectRatio(1f, true)
            ) {
                backStack.removeLast()
            }

            NavEntry.All.forEach { entry ->
                NavLink(
                    title = {
                        if (containerSize > ContainerSize.Small) {
                            Text(stringResource(entry.title), fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.fillMaxWidth())
                        }
                    },
                    icon = {
                        Icon(entry.icon, null, Modifier.size(iconSize))
                    },
                    isActive = backStack.currentGroup == entry.screen.group,
                    modifier = Modifier.fillMaxSize()
                ) {
                    backStack.add(entry.screen)
                }
            }
        }
    }
}


@Composable
fun Modifier.navHazeEffect(state: HazeState, backgroundColor: Color = Color.Unspecified): Modifier {
    val overlayGradient = remember(backgroundColor) {
        Brush.verticalGradient(listOf(backgroundColor, Color.Transparent))
    }

    return this.hazeEffect(state) {
        blurEffect {
            noiseFactor = 0f
            progressive = HazeProgressive.verticalGradient(
                easing = LinearEasing,
                startIntensity = 1f,
                endIntensity = 0f
            )
            this.backgroundColor = backgroundColor
        }
    }.drawWithContent {
        drawRect(overlayGradient)
        drawContent()
    }
}


@ComponentPreview
@Composable
private fun NavBarPreview() = PreviewHost {
    val backStack = AppBackStack(initial = Screen.Projects)
    CompositionLocalProvider(LocalBackStack provides backStack) {
        NavBar()
    }
}
