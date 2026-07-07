package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.inDp

@Composable
fun UnderConstruction() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProvideTextStyle(MaterialTheme.typography.displaySmall) {
            val lineHeight = LocalTextStyle.current.lineHeight.inDp()
            ConstructionLines(Modifier.height(lineHeight).fillMaxWidth())

            Box(
                Modifier.background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text("Under Construction")
            }
        }
    }
}

@Composable
private fun ConstructionLines(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.error

    Spacer(
        modifier
            .clipToBounds()
            .drawWithCache {
                val spacing = 8.dp.toPx()
                val strokeWidth = 4.dp.toPx()
                val angle = 45f

                onDrawBehind {
                    rotate(angle) {
                        val diagonal = size.width + size.height

                        var x = -diagonal
                        while (x < diagonal * 2) {
                            drawLine(
                                color = color,
                                start = Offset(x, -diagonal),
                                end = Offset(x, diagonal * 2),
                                strokeWidth = strokeWidth
                            )
                            x += spacing
                        }
                    }
                }
            })
}

@ComponentPreview
@Composable
private fun UnderConstructionPreview() = PreviewHost {
    UnderConstruction()
}
