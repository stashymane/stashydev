package icons.outline

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import icons.Icons

val Icons.Outline.PinDrop24Dp: ImageVector
    get() {
        if (_PinDrop24Dp != null) {
            return _PinDrop24Dp!!
        }
        _PinDrop24Dp = ImageVector.Builder(
            name = "Outline.PinDrop24Dp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(480f, 659f)
                quadToRelative(99f, -80f, 149.5f, -154f)
                reflectiveQuadTo(680f, 366f)
                quadToRelative(0f, -90f, -56f, -148f)
                reflectiveQuadToRelative(-144f, -58f)
                quadToRelative(-88f, 0f, -144f, 58f)
                reflectiveQuadToRelative(-56f, 148f)
                quadToRelative(0f, 65f, 50.5f, 139f)
                reflectiveQuadTo(480f, 659f)
                close()
                moveTo(480f, 760f)
                quadTo(339f, 656f, 269.5f, 558f)
                reflectiveQuadTo(200f, 366f)
                quadToRelative(0f, -125f, 78f, -205.5f)
                reflectiveQuadTo(480f, 80f)
                quadToRelative(124f, 0f, 202f, 80.5f)
                reflectiveQuadTo(760f, 366f)
                quadToRelative(0f, 94f, -69.5f, 192f)
                reflectiveQuadTo(480f, 760f)
                close()
                moveTo(480f, 440f)
                quadToRelative(33f, 0f, 56.5f, -23.5f)
                reflectiveQuadTo(560f, 360f)
                quadToRelative(0f, -33f, -23.5f, -56.5f)
                reflectiveQuadTo(480f, 280f)
                quadToRelative(-33f, 0f, -56.5f, 23.5f)
                reflectiveQuadTo(400f, 360f)
                quadToRelative(0f, 33f, 23.5f, 56.5f)
                reflectiveQuadTo(480f, 440f)
                close()
                moveTo(200f, 880f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(560f)
                verticalLineToRelative(80f)
                lineTo(200f, 880f)
                close()
                moveTo(480f, 360f)
                close()
            }
        }.build()

        return _PinDrop24Dp!!
    }

@Suppress("ObjectPropertyName")
private var _PinDrop24Dp: ImageVector? = null
