package icons.logos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import icons.Icons

val Icons.Logos.Nixos: ImageVector
    get() {
        if (_Nixos != null) {
            return _Nixos!!
        }
        _Nixos = ImageVector.Builder(
            name = "Logos.Nixos",
            defaultWidth = 2400.dp,
            defaultHeight = 2400.dp,
            viewportWidth = 2400f,
            viewportHeight = 2400f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(704f, 1227.7f)
                lineToRelative(560f, 969.9f)
                lineToRelative(-256f, 0f)
                lineToRelative(-128f, -221.7f)
                lineToRelative(-128f, 221.7f)
                lineToRelative(-128f, 0f)
                lineToRelative(-64f, -110.9f)
                lineToRelative(192f, -332.6f)
                lineToRelative(-176f, -304.8f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(928f, 784.3f)
                lineToRelative(-560f, 969.9f)
                lineToRelative(-128f, -221.7f)
                lineToRelative(128f, -221.7f)
                lineToRelative(-256f, 0f)
                lineToRelative(-64f, -110.9f)
                lineToRelative(64f, -110.9f)
                lineToRelative(384f, 0f)
                lineToRelative(176f, -304.8f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(1424f, 756.6f)
                lineToRelative(-1120f, 0f)
                lineToRelative(128f, -221.7f)
                lineToRelative(256f, 0f)
                lineToRelative(-128f, -221.7f)
                lineToRelative(64f, -110.9f)
                lineToRelative(128f, 0f)
                lineToRelative(192f, 332.6f)
                lineToRelative(352f, 0f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(1696f, 1172.3f)
                lineToRelative(-560f, -969.9f)
                lineToRelative(256f, 0f)
                lineToRelative(128f, 221.7f)
                lineToRelative(128f, -221.7f)
                lineToRelative(128f, 0f)
                lineToRelative(64f, 110.9f)
                lineToRelative(-192f, 332.6f)
                lineToRelative(176f, 304.8f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(1472f, 1615.7f)
                lineToRelative(560f, -969.9f)
                lineToRelative(128f, 221.7f)
                lineToRelative(-128f, 221.7f)
                lineToRelative(256f, 0f)
                lineToRelative(64f, 110.9f)
                lineToRelative(-64f, 110.9f)
                lineToRelative(-384f, 0f)
                lineToRelative(-176f, 304.8f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(976f, 1643.4f)
                lineToRelative(1120f, 0f)
                lineToRelative(-128f, 221.7f)
                lineToRelative(-256f, 0f)
                lineToRelative(128f, 221.7f)
                lineToRelative(-64f, 110.9f)
                lineToRelative(-128f, 0f)
                lineToRelative(-192f, -332.6f)
                lineToRelative(-352f, 0f)
                close()
            }
        }.build()

        return _Nixos!!
    }

@Suppress("ObjectPropertyName")
private var _Nixos: ImageVector? = null
