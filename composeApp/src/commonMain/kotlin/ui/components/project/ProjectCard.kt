package ui.components.project

import Project
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.logos.Kotlin

@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest).padding(16.dp)
    ) {
        Row {
            Text(project.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }

        if (project.languages.isNotEmpty()) {
            ProjectLanguageRow(project.languages)
        }


        project.description?.let { description ->
            Row {
                Text(description, style = MaterialTheme.typography.bodyMedium)
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        languages.forEach { language ->
            val textStyle = MaterialTheme.typography.labelLarge
            val entryHeight = with(LocalDensity.current) {
                textStyle.fontSize.toDp() * 0.8f
            }
            language.icon()?.let { icon ->
                Icon(icon, contentDescription = language.name, Modifier.size(entryHeight))
            }
            Text(language.name, style = textStyle)
        }
    }
}

private fun Project.Language.icon(): ImageVector? = when (this.name) {
    "Kotlin" -> Icons.Logos.Kotlin
    else -> null
}
