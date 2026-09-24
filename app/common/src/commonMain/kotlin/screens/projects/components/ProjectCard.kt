package screens.projects.components

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outline.Briefcases
import model.composeColor
import toRelativeString
import ui.components.Badge
import ui.components.InlineIcon
import ui.components.LanguageBadge
import ui.components.LinkButton
import ui.preview.ComponentPreview
import ui.preview.PreviewData
import ui.preview.PreviewHost
import ui.theme.AppTheme
import ui.theme.easeGradientBetween
import ui.theme.fullRadialGradient

@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    val languageColor = project.languages.firstOrNull()?.composeColor
        ?: MaterialTheme.colorScheme.primary

    AppTheme(languageColor, animate = false) {
        val innerContainerColor = remember(languageColor, containerColor) {
            languageColor.copy(alpha = 0.1f).compositeOver(containerColor)
        }
        val backgroundGradient = remember(innerContainerColor, containerColor) {
            easeGradientBetween(
                innerContainerColor,
                containerColor,
                steps = 6,
                easing = EaseOut
            )
        }

        Column(
            modifier.drawWithCache {
                val gradient = Brush.fullRadialGradient(
                    *backgroundGradient, size = size, x = 0.5f, y = 1f
                )

                onDrawBehind {
                    drawRect(gradient)
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

            project.updatedAt?.let { updatedAt ->
                ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                    Row {
                        Text("Updated ")
                        Text(updatedAt.toRelativeString())
                    }
                }
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

@ComponentPreview
@Composable
private fun ProjectCardPreview() = PreviewHost {
    ProjectCard(PreviewData.project)
}
