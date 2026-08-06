package screens.about.components

import UserMeta
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy.Companion.Offscreen
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import icons.Icons
import icons.outline.PinDrop24Dp
import model.Links
import ui.components.InlineIcon
import ui.components.LinkButton
import ui.preview.ComponentPreview
import ui.preview.PreviewData
import ui.preview.PreviewHost
import ui.theme.easeGradientBetween
import ui.theme.fullRadialGradient

private val maskGradient = easeGradientBetween(Color.White, Color.Transparent)

@Composable
fun BusinessCard(
    meta: UserMeta,
    modifier: Modifier = Modifier,
) {
    val profile = meta.profile
    val surfaceColor = MaterialTheme.colorScheme.surfaceContainerLow

    val gradient = remember(surfaceColor) {
        easeGradientBetween(surfaceColor, Color.Transparent)
    }

    Box(modifier) {
        ContributionGraph(
            graph = meta.contributionGraph,
            peakColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.matchParentSize().padding(4.dp)
                .graphicsLayer(compositingStrategy = Offscreen)
                .drawWithCache {
                    val mask = Brush.fullRadialGradient(
                        *maskGradient,
                        size = size,
                        radius = 1f,
                        x = 1f,
                        y = 1f
                    )

                    onDrawWithContent {
                        drawContent()
                        drawRect(mask, blendMode = BlendMode.DstIn)
                    }
                },
            gapFraction = 0.05f
        )

        Column(
            Modifier
                .fillMaxWidth()
                .drawWithCache {
                    val gradient = Brush.fullRadialGradient(*gradient, size = size, x = 0f, y = 0f)
                    onDrawWithContent {
                        drawRect(gradient)
                        drawContent()
                    }
                }.padding(horizontal = 22.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                val displayFont = MaterialTheme.typography.displayLarge
                Text(
                    profile.login,
                    style = displayFont,
                    fontWeight = Black,
                    autoSize = TextAutoSize.StepBased(
                        displayFont.fontSize * 0.7f,
                        displayFont.fontSize
                    ),
                    maxLines = 1
                )
                profile.name?.let { realName ->
                    Text(
                        realName,
                        style = MaterialTheme.typography.titleLarge,
                        letterSpacing = 0.07.em,
                        fontWeight = Bold,
                        color = LocalContentColor.current.copy(alpha = 0.8f)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                profile.location?.let { location ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InlineIcon(Icons.Outline.PinDrop24Dp, "Location")
                        Text(location, fontWeight = Bold)
                    }
                }

                LinkButton(Links.email.url)
            }

            profile.bio?.let { bio ->
                Column {
                    Text(bio)
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BusinessCardPreview() = PreviewHost {
    BusinessCard(PreviewData.userMeta)
}
