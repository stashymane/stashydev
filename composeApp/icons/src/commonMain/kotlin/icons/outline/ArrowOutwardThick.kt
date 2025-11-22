package icons.outline

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import icons.Icons

val Icons.Outline.ArrowOutwardThick: ImageVector
    get() {
        if (_ArrowOutwardThick != null) {
            return _ArrowOutwardThick!!
        }
        _ArrowOutwardThick = ImageVector.Builder(
            name = "ArrowOutwardThick",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveToRelative(253f, 753f)
                lineToRelative(-84f, -84f)
                lineToRelative(382f, -382f)
                lineTo(227f, 287f)
                verticalLineToRelative(-118f)
                horizontalLineToRelative(526f)
                verticalLineToRelative(526f)
                lineTo(635f, 695f)
                verticalLineToRelative(-324f)
                lineTo(253f, 753f)
                close()
            }
        }.build()

        return _ArrowOutwardThick!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowOutwardThick: ImageVector? = null
