package icons.logos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import icons.Icons

val Icons.Logos.Kotlin: ImageVector
    get() {
        if (_Kotlin != null) {
            return _Kotlin!!
        }
        _Kotlin = ImageVector.Builder(
            name = "Logos.Kotlin",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            group(pivotX = 24f, pivotY = 24f, scaleX = 0.75f, scaleY = 0.75f) {
                path(fill = SolidColor(Color.White)) {
                    moveTo(48f, 48f)
                    horizontalLineTo(0f)
                    verticalLineTo(0f)
                    horizontalLineTo(48f)
                    lineTo(23.505f, 23.647f)
                    lineTo(48f, 48f)
                    close()
                }
            }
        }.build()

        return _Kotlin!!
    }

@Suppress("ObjectPropertyName")
private var _Kotlin: ImageVector? = null
