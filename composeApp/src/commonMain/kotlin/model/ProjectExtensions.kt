package model

import Project
import Project.Language.Companion.CSharp
import Project.Language.Companion.Java
import Project.Language.Companion.Kotlin
import Project.Language.Companion.Nix
import Project.Language.Companion.Rust
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.ajalt.colormath.extensions.android.composecolor.toComposeColor
import icons.Icons
import icons.logos.*

fun Project.Language.getIcon(): ImageVector? = when (this) {
    Kotlin -> Icons.Logos.Kotlin
    Java -> Icons.Logos.Java
    Rust -> Icons.Logos.Rust
    CSharp -> Icons.Logos.CSharp
    Nix -> Icons.Logos.Nixos
    else -> null
}

val Project.Language.composeColor: Color?
    get() = color?.toComposeColor()
