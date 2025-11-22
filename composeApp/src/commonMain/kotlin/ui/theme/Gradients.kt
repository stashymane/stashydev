package ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.pow

/**
 * Creates an exponential gradient between two colors with configurable curve and step distribution.
 * The resulting steps are concentrated more where the change is most noticeable.
 *
 * @param startColor The starting color of the gradient
 * @param endColor The ending color of the gradient
 * @param steps Number of gradient steps
 * @param start Starting position (0f to 1f)
 * @param end Ending position (0f to 1f)
 * @param curve Controls the curve intensity (1f = linear, >1f = exponential, <1f = logarithmic)
 * @return List of position-color pairs for the gradient
 */
fun exponentialGradientBetween(
    startColor: Color,
    endColor: Color,
    steps: Int,
    start: Float = 0f,
    end: Float = 1f,
    curve: Float = 2f
): List<Pair<Float, Color>> = buildList {
    for (i in 0..steps) {
        val t = i / steps.toFloat()

        // Apply exponential curve for position distribution
        val curvedPositionT = when {
            curve == 1f -> t // Linear
            curve > 1f -> t.pow(1f / curve) // More positions where change is rapid
            else -> 1f - (1f - t).pow(curve) // More positions where change is rapid
        }

        // Apply exponential curve for color interpolation
        val curvedColorT = when {
            curve == 1f -> t // Linear
            curve > 1f -> t.pow(curve) // Exponential color transition
            else -> 1f - (1f - t).pow(1f / curve) // Logarithmic color transition
        }

        val pos = start + curvedPositionT * (end - start)

        // Interpolate between colors using the curved interpolation
        val color = Color(
            red = startColor.red + (endColor.red - startColor.red) * curvedColorT,
            green = startColor.green + (endColor.green - startColor.green) * curvedColorT,
            blue = startColor.blue + (endColor.blue - startColor.blue) * curvedColorT,
            alpha = startColor.alpha + (endColor.alpha - startColor.alpha) * curvedColorT
        )

        add(pos to color)
    }
}

/**
 * Creates an exponential gradient brush between two colors.
 */
fun Brush.Companion.exponentialGradient(
    startColor: Color,
    endColor: Color,
    steps: Int = 10,
    start: Float = 0f,
    end: Float = 1f,
    curve: Float = 2f
): Brush = Brush.linearGradient(
    *exponentialGradientBetween(startColor, endColor, steps, start, end, curve).toTypedArray()
)

/**
 * Creates a vertical exponential gradient brush between two colors.
 */
fun Brush.Companion.exponentialVerticalGradient(
    startColor: Color,
    endColor: Color,
    steps: Int = 10,
    start: Float = 0f,
    end: Float = 1f,
    curve: Float = 2f
): Brush = Brush.verticalGradient(
    *exponentialGradientBetween(startColor, endColor, steps, start, end, curve).toTypedArray()
)

/**
 * Creates a horizontal exponential gradient brush between two colors.
 */
fun Brush.Companion.exponentialHorizontalGradient(
    startColor: Color,
    endColor: Color,
    steps: Int = 10,
    start: Float = 0f,
    end: Float = 1f,
    curve: Float = 2f
): Brush = Brush.horizontalGradient(
    *exponentialGradientBetween(startColor, endColor, steps, start, end, curve).toTypedArray()
)
