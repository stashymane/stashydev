package model

import Project
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.ajalt.colormath.extensions.android.composecolor.toComposeColor
import icons.Icons
import icons.logos.CSharp
import icons.logos.Java
import icons.logos.Kotlin
import icons.logos.Rust

fun Project.Language.getIcon(): ImageVector? = when (this) {
    Project.Language.Kotlin -> Icons.Logos.Kotlin
    Project.Language.Java -> Icons.Logos.Java
    Project.Language.Rust -> Icons.Logos.Rust
    Project.Language.CSharp -> Icons.Logos.CSharp
    else -> null
}

val Project.Language.composeColor: Color?
    get() = color?.toComposeColor()
