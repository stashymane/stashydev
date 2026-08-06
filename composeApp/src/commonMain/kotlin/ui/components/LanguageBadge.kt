package ui.components

import Project
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import model.composeColor
import model.getIcon
import ui.preview.ComponentPreview
import ui.preview.PreviewHost

private data class LanguageBadgeMeta(
    val icon: ImageVector?,
    val color: Color?
) {
    companion object {
        fun from(language: Project.Language): LanguageBadgeMeta =
            LanguageBadgeMeta(language.getIcon(), language.composeColor)
    }
}

@Composable
fun LanguageBadge(
    language: Project.Language,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(fontWeight = Bold)
) {
    val meta = remember(language) { LanguageBadgeMeta.from(language) }
    val backgroundColor = MaterialTheme.colorScheme.inverseSurface
    val containerColor = remember(meta.color, backgroundColor) {
        meta.color?.copy(alpha = 0.3f)?.compositeOver(backgroundColor) ?: backgroundColor
    }

    Badge(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = MaterialTheme.colorScheme.surface,
        textStyle = textStyle
    ) {
        meta.icon?.let { icon ->
            InlineIcon(icon, language.label)
        }

        Text(language.label, Modifier.padding(end = 2.dp), color = LocalContentColor.current)
    }
}

@ComponentPreview
@Composable
private fun LanguageBadgePreview() = PreviewHost {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Project.Language.entries.forEach {
            LanguageBadge(it)
        }
    }
}
