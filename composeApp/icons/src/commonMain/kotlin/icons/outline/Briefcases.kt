package icons.outline

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import icons.Icons

val Icons.Outline.Briefcases: ImageVector
    get() {
        if (_Briefcases != null) {
            return _Briefcases!!
        }
        _Briefcases = ImageVector.Builder(
            name = "Briefcases",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(120f, 912f)
                quadToRelative(-29.7f, 0f, -50.85f, -21.15f)
                quadTo(48f, 869.7f, 48f, 840f)
                verticalLineToRelative(-456f)
                horizontalLineToRelative(72f)
                verticalLineToRelative(456f)
                horizontalLineToRelative(648f)
                verticalLineToRelative(72f)
                lineTo(120f, 912f)
                close()
                moveTo(264f, 768f)
                quadToRelative(-29.7f, 0f, -50.85f, -21.15f)
                quadTo(192f, 725.7f, 192f, 696f)
                verticalLineToRelative(-456f)
                horizontalLineToRelative(192f)
                verticalLineToRelative(-72f)
                quadToRelative(0f, -29.7f, 21.15f, -50.85f)
                quadTo(426.3f, 96f, 456f, 96f)
                horizontalLineToRelative(192f)
                quadToRelative(29.7f, 0f, 50.85f, 21.15f)
                quadTo(720f, 138.3f, 720f, 168f)
                verticalLineToRelative(72f)
                horizontalLineToRelative(192f)
                verticalLineToRelative(456f)
                quadToRelative(0f, 29.7f, -21.15f, 50.85f)
                quadTo(869.7f, 768f, 840f, 768f)
                lineTo(264f, 768f)
                close()
                moveTo(264f, 696f)
                horizontalLineToRelative(576f)
                verticalLineToRelative(-384f)
                lineTo(264f, 312f)
                verticalLineToRelative(384f)
                close()
                moveTo(456f, 240f)
                horizontalLineToRelative(192f)
                verticalLineToRelative(-72f)
                lineTo(456f, 168f)
                verticalLineToRelative(72f)
                close()
                moveTo(264f, 696f)
                verticalLineToRelative(-384f)
                verticalLineToRelative(384f)
                close()
            }
        }.build()

        return _Briefcases!!
    }

@Suppress("ObjectPropertyName")
private var _Briefcases: ImageVector? = null
