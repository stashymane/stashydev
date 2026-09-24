package ui.theme

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.github.ajalt.colormath.extensions.android.composecolor.toColormathColor
import com.github.ajalt.colormath.extensions.android.composecolor.toComposeColor
import com.github.ajalt.colormath.model.Oklab
import com.github.ajalt.colormath.transform.EasingFunction
import com.github.ajalt.colormath.transform.interpolator
import kotlin.math.hypot

object GradientDefaults {
    val easingFunction: Easing = LinearEasing
    const val STEPS: Int = 20
}

fun Easing.asEasingFunction(): EasingFunction = EasingFunction(::transform)

fun easeGradientBetween(
    startColor: Color,
    endColor: Color,
    steps: Int = GradientDefaults.STEPS,
    start: Float = 0f,
    end: Float = 1f,
    easing: Easing = GradientDefaults.easingFunction
): Array<Pair<Float, Color>> = buildList {
    val interpolator = Oklab.interpolator {
        this.easing = easing.asEasingFunction()
        stop(startColor.toColormathColor().toOklab())
        stop(endColor.toColormathColor().toOklab())
    }

    for (i in 0..steps) {
        val progress = i / (steps - 1f)
        val color = interpolator.interpolate(progress).toSRGB().toComposeColor()

        val position = start + progress * (end - start)

        add(position to color)
    }
}.toTypedArray()

fun Brush.Companion.easeVerticalGradient(
    startColor: Color,
    endColor: Color,
    steps: Int = GradientDefaults.STEPS,
    start: Float = 0f,
    end: Float = 1f,
    easing: Easing = GradientDefaults.easingFunction
): Brush = Brush.verticalGradient(
    *easeGradientBetween(startColor, endColor, steps, start, end, easing)
)

fun Brush.Companion.easeHorizontalGradient(
    startColor: Color,
    endColor: Color,
    steps: Int = GradientDefaults.STEPS,
    start: Float = 0f,
    end: Float = 1f,
    easing: Easing = GradientDefaults.easingFunction
): Brush = Brush.horizontalGradient(
    *easeGradientBetween(startColor, endColor, steps, start, end, easing)
)


fun Brush.Companion.fullRadialGradient(
    vararg colorStops: Pair<Float, Color>,
    size: Size,
    radius: Float = 1f,
    x: Float = 0.5f,
    y: Float = 0.5f
): Brush {
    val radius = hypot(size.width * radius, size.height * radius)
    return Brush.radialGradient(
        *colorStops,
        center = Offset(size.width * x, size.height * y),
        radius = radius
    )
}
