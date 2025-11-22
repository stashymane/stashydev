package icons.outlinelarge

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import icons.Icons

val Icons.OutlineLarge.FitScreen: ImageVector
    get() {
        if (_FitScreen != null) {
            return _FitScreen!!
        }
        _FitScreen = ImageVector.Builder(
            name = "OutlineLarge.FitScreen",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(809.23f, 355.38f)
                verticalLineToRelative(-100f)
                quadToRelative(0f, -9.23f, -7.69f, -16.92f)
                quadToRelative(-7.69f, -7.69f, -16.92f, -7.69f)
                horizontalLineToRelative(-100f)
                lineTo(684.62f, 200f)
                horizontalLineToRelative(100f)
                quadToRelative(23.05f, 0f, 39.22f, 16.16f)
                quadTo(840f, 232.33f, 840f, 255.38f)
                verticalLineToRelative(100f)
                horizontalLineToRelative(-30.77f)
                close()
                moveTo(120f, 355.38f)
                verticalLineToRelative(-100f)
                quadToRelative(0f, -23.05f, 16.16f, -39.22f)
                quadTo(152.33f, 200f, 175.38f, 200f)
                horizontalLineToRelative(100f)
                verticalLineToRelative(30.77f)
                horizontalLineToRelative(-100f)
                quadToRelative(-9.23f, 0f, -16.92f, 7.69f)
                quadToRelative(-7.69f, 7.69f, -7.69f, 16.92f)
                verticalLineToRelative(100f)
                lineTo(120f, 355.38f)
                close()
                moveTo(684.62f, 760f)
                verticalLineToRelative(-30.77f)
                horizontalLineToRelative(100f)
                quadToRelative(9.23f, 0f, 16.92f, -7.69f)
                quadToRelative(7.69f, -7.69f, 7.69f, -16.92f)
                verticalLineToRelative(-100f)
                lineTo(840f, 604.62f)
                verticalLineToRelative(100f)
                quadToRelative(0f, 23.05f, -16.16f, 39.22f)
                quadTo(807.67f, 760f, 784.62f, 760f)
                horizontalLineToRelative(-100f)
                close()
                moveTo(175.38f, 760f)
                quadToRelative(-23.05f, 0f, -39.22f, -16.16f)
                quadTo(120f, 727.67f, 120f, 704.62f)
                verticalLineToRelative(-100f)
                horizontalLineToRelative(30.77f)
                verticalLineToRelative(100f)
                quadToRelative(0f, 9.23f, 7.69f, 16.92f)
                quadToRelative(7.69f, 7.69f, 16.92f, 7.69f)
                horizontalLineToRelative(100f)
                lineTo(275.38f, 760f)
                horizontalLineToRelative(-100f)
                close()
                moveTo(255.38f, 624.62f)
                verticalLineToRelative(-289.24f)
                horizontalLineToRelative(449.24f)
                verticalLineToRelative(289.24f)
                lineTo(255.38f, 624.62f)
                close()
                moveTo(286.15f, 593.85f)
                horizontalLineToRelative(387.7f)
                verticalLineToRelative(-227.7f)
                horizontalLineToRelative(-387.7f)
                verticalLineToRelative(227.7f)
                close()
                moveTo(286.15f, 593.85f)
                verticalLineToRelative(-227.7f)
                verticalLineToRelative(227.7f)
                close()
            }
        }.build()

        return _FitScreen!!
    }

@Suppress("ObjectPropertyName")
private var _FitScreen: ImageVector? = null
