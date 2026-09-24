package screens.about.components

import Project
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.composeColor
import ui.components.LanguageBadge
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import kotlin.math.roundToInt

@OptIn(ExperimentalGridApi::class)
@Composable
fun LanguageShareChart(
    languageShare: Map<String, Double>,
    modifier: Modifier = Modifier,
    count: Int = 5,
    minimum: Float = 1f
) {
    val entries = remember(languageShare) {
        languageShare.entries
            .filter { it.value > minimum }
            .sortedByDescending { it.value }
            .take(count)
    }
    if (entries.isEmpty()) return

    val maxShare = remember(entries) {
        entries.maxOf { it.value }.coerceAtLeast(1.0)
    }
    val fallbackColor = MaterialTheme.colorScheme.primary

    Grid(
        {
            gap(4.dp)
            column(GridTrackSize.MaxContent)
            column(1.fr)
        },
        modifier,
    ) {
        entries.forEach { (label, share) ->
            val language = Project.Language.fromLabel(label)
            val barColor = language.composeColor ?: fallbackColor
            val fraction = (share / maxShare).toFloat().coerceIn(0f, 1f)

            LanguageBadge(
                language,
                Modifier.gridItem(alignment = Alignment.CenterEnd).fillMaxWidth(),
            )

            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .background(barColor)
                    .gridItem(alignment = Alignment.CenterStart)
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    "${share.roundToInt()}%",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = Bold)
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun LanguageShareChartPreview() = PreviewHost {
    LanguageShareChart(
        mapOf(
            "Kotlin" to 61.7,
            "Rust" to 37.3,
            "HTML" to 0.7,
        )
    )
}
