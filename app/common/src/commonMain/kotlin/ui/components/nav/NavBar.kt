package ui.components.nav

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.hazeBlur
import dev.stashy.navigation.compose.LocalNavigator
import dev.stashy.standard.ui.responsive.ContainerSize
import dev.stashy.standard.ui.responsive.LocalResponsiveInfo
import icons.Icons
import icons.filled.ChevronBackward
import model.NavEntry
import model.Screen
import org.jetbrains.compose.resources.stringResource
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.inDp

@Composable
fun NavBar(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.current
    val responsiveInfo = LocalResponsiveInfo.current

    Grid(
        {
            column(64.dp)

            column(1.fr)
            column(1.fr)
            column(1.fr)

            row(1.fr)

            gap(8.dp)
        },
        modifier.height(80.dp).padding(8.dp)
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            val iconSize = LocalTextStyle.current.lineHeight.inDp()

            NavLink(
                title = {},
                icon = { Icon(Icons.Filled.ChevronBackward, "back", Modifier.size(iconSize)) },
                isActive = false,
                modifier = Modifier.fillMaxSize().aspectRatio(1f, true)
            ) {
                navigator.goBack()
            }

            NavEntry.All.forEach { entry ->
                NavLink(
                    title = {
                        if (responsiveInfo > ContainerSize.Small) {
                            Text(stringResource(entry.title), fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.fillMaxWidth())
                        }
                    },
                    icon = {
                        Icon(entry.icon, null, Modifier.size(iconSize))
                    },
                    isActive = navigator.state.topLevelRoute == entry.screen,
                    modifier = Modifier.fillMaxSize()
                ) {
                    navigator.navigate(entry.screen)
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

    return this.hazeBlur(
        input = HazeInput.Sources(state),
        style = HazeBlurStyle {
            noiseFactor(0f)
            progressive(
                HazeProgressive.verticalGradient(
                    easing = LinearEasing,
                    startIntensity = 1f,
                    endIntensity = 0f
                )
            )
            backgroundColor(backgroundColor)
        }).drawWithContent {
        drawRect(overlayGradient)
        drawContent()
    }
}


@ComponentPreview
@Composable
private fun NavBarPreview() = PreviewHost {
    val navigator = LocalNavigator.current
    navigator.navigate(Screen.Projects)

    NavBar()
}
