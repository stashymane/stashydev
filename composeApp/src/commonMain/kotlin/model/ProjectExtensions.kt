package model

import Project
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
