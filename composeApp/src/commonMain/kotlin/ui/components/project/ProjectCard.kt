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
import icons.logos.Kotlin
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
        modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Text(project.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
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

        Column {
            ProvideTextStyle(
                MaterialTheme.typography.bodyMedium.merge(color = LocalContentColor.current.copy(alpha = 0.8f))
            ) {
                if (project.languages.isNotEmpty()) {
                    ProjectLanguageRow(project.languages)
                }

                if (project.urls.isNotEmpty()) {
                    project.urls.forEach { url ->
                        LinkButton(url)
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

@ComponentPreview
@Composable
private fun ProjectCardPreview() = PreviewHost {
    ProjectCard(PreviewData.project)
}
