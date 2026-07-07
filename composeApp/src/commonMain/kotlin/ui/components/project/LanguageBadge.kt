package ui.components.project

import Project
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.logos.CSharp
import icons.logos.Java
import icons.logos.Kotlin
import icons.logos.Rust
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.inDp

private class LanguageBadgeMeta(
    val icon: ImageVector,
    val color: Color
)

@Composable
fun LanguageBadge(language: Project.Language) {
    val iconSize = LocalTextStyle.current.lineHeight.inDp() * 0.8f
    val meta = remember(language) { language.getMeta() }
    val gradient = meta?.color?.let { color ->
        Brush.verticalGradient(listOf(color.copy(alpha = 0.25f), color.copy(alpha = 0.05f)))
    } ?: Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surfaceContainerHigh))

    Row(
        Modifier.background(gradient)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        meta?.icon?.let { icon ->
            Icon(icon, contentDescription = language.name, Modifier.size(iconSize))
        }

        Text(language.name, fontWeight = FontWeight.Bold)
    }
}

private fun Project.Language.getMeta(): LanguageBadgeMeta? = when (this) {
    Project.Language.Kotlin -> LanguageBadgeMeta(
        Icons.Logos.Kotlin, Color(
            0.5f,
            0.32f,
            1.0f
        )
    )

    Project.Language.Java -> LanguageBadgeMeta(
        Icons.Logos.Java, Color(
            1.0f,
            0.38f,
            0.224f
        )
    )

    Project.Language.Rust -> LanguageBadgeMeta(
        Icons.Logos.Rust, Color(
            0.937f,
            0.553f,
            0.38f
        )
    )

    Project.Language.CSharp -> LanguageBadgeMeta(
        Icons.Logos.CSharp, Color(
            0.545f,
            0.455f,
            0.867f
        )
    )
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
