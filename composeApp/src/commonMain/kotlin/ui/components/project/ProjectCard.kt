package ui.components.project

import Project
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.logos.GitHub
import icons.logos.Kotlin
import icons.outline.CaptivePortal
import io.ktor.http.*
import ui.components.LinkButton
import ui.preview.ComponentPreview
import ui.preview.PreviewData
import ui.preview.PreviewHost
import ui.theme.inDp

@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.background(MaterialTheme.colorScheme.surfaceContainer).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Text(project.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }


        project.description?.let { description ->
            Row(Modifier.weight(1f)) {
                Text(description, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Column {
            ProvideTextStyle(
                MaterialTheme.typography.bodyMedium.merge(color = LocalContentColor.current.copy(alpha = 0.8f))
            ) {
                if (project.languages.isNotEmpty()) {
                    ProjectLanguageRow(project.languages)
                }

                if (project.urls.isNotEmpty()) {
                    Column(modifier) {
                        project.urls.forEach { url ->
                            LinkButton(url)
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
    val iconSize = LocalTextStyle.current.lineHeight.inDp() * 0.8f

    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        languages.forEach { language ->
            language.icon()?.let { icon ->
                Icon(icon, contentDescription = language.name, Modifier.size(iconSize))
            }
            Text(language.name, fontWeight = FontWeight.Bold)
        }
    }
}

private fun Project.Language.icon(): ImageVector? = when (this.name) {
    "Kotlin" -> Icons.Logos.Kotlin
    else -> null
}

private fun Url.icon(): ImageVector = when (this.host) {
    "github.com" -> Icons.Logos.GitHub
    else -> Icons.Outline.CaptivePortal
}

@ComponentPreview
@Composable
private fun ProjectCardPreview() = PreviewHost {
    ProjectCard(PreviewData.project)
}
