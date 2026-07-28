package ui.components.project

import Project
import androidx.compose.animation.core.EaseOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.ajalt.colormath.extensions.android.composecolor.toComposeColor
import icons.Icons
import icons.outline.Briefcases
import ui.components.Badge
import ui.components.InlineIcon
import ui.components.LinkButton
import ui.preview.ComponentPreview
import ui.preview.PreviewData
import ui.preview.PreviewHost
import ui.theme.AppTheme
import ui.theme.easeGradientBetween
import kotlin.math.hypot

@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
) {
    val defaultColor = MaterialTheme.colorScheme.primary
    val containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    val languageColor = remember(project.languages, defaultColor) {
        project.languages.firstOrNull()?.color?.toComposeColor() ?: defaultColor
    }

    AppTheme(languageColor) {
        val innerContainerColor = remember(languageColor, containerColor) {
            languageColor.copy(alpha = 0.1f).compositeOver(containerColor)
        }
        val backgroundGradient = remember(innerContainerColor, containerColor) {
            easeGradientBetween(
                innerContainerColor,
                containerColor,
                easing = EaseOut
            )
        }

        Column(
            modifier.drawWithCache {
                val radius = hypot(size.width, size.height)
                val brush = Brush.radialGradient(
                    *backgroundGradient.toTypedArray(),
                    center = Offset(size.width / 2f, size.height),
                    radius = radius
                )

                onDrawBehind {
                    drawRect(brush)
                }
            }.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                Text(
                    project.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                project.languages.forEach { language ->
                    LanguageBadge(language)
                }
            }

            project.description?.let { description ->
                Row {
                    Text(
                        description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalContentColor.current.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(Modifier.heightIn(min = 32.dp).weight(1f))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                project.license?.let { license ->
                    Badge(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        InlineIcon(Icons.Outline.Briefcases, null)
                        Text(license)
                    }
                }

                ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                    if (project.urls.isNotEmpty()) {
                        project.urls.forEach { url ->
                            LinkButton(url, Modifier.padding(horizontal = 6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectLanguageRow(
    languages: List<Project.Language>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        languages.forEach { language ->
            LanguageBadge(language)
        }
    }
}

@ComponentPreview
@Composable
private fun ProjectCardPreview() = PreviewHost {
    ProjectCard(PreviewData.project)
}
