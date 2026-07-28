package model

import Project
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import icons.Icons
import icons.logos.CSharp
import icons.logos.Java
import icons.logos.Kotlin
import icons.logos.Rust

fun Project.Language.getIcon(): ImageVector = when (this) {
    Project.Language.Kotlin -> Icons.Logos.Kotlin
    Project.Language.Java -> Icons.Logos.Java
    Project.Language.Rust -> Icons.Logos.Rust
    Project.Language.CSharp -> Icons.Logos.CSharp
}

fun Project.Language.getColor(): Color = when (this) {
    Project.Language.Kotlin -> Color(0.5f, 0.32f, 1.0f)
    Project.Language.Java -> Color(1.0f, 0.38f, 0.224f)
    Project.Language.Rust -> Color(0.937f, 0.553f, 0.38f)
    Project.Language.CSharp -> Color(0.545f, 0.455f, 0.867f)
}
