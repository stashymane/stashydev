package icons.outlinelarge

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import icons.Icons

val Icons.OutlineLarge.Cases: ImageVector
    get() {
        if (_Cases != null) {
            return _Cases!!
        }
        _Cases = ImageVector.Builder(
            name = "OutlineLarge.Cases",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(135.38f, 807.69f)
                quadToRelative(-23.05f, 0f, -39.22f, -16.16f)
                quadTo(80f, 775.37f, 80f, 752.31f)
                verticalLineToRelative(-375.16f)
                horizontalLineToRelative(30.77f)
                verticalLineToRelative(375.16f)
                quadToRelative(0f, 9.23f, 7.69f, 16.92f)
                quadToRelative(7.69f, 7.69f, 16.92f, 7.69f)
                horizontalLineToRelative(562.85f)
                verticalLineToRelative(30.77f)
                lineTo(135.38f, 807.69f)
                close()
                moveTo(236.92f, 706.15f)
                quadToRelative(-23.05f, 0f, -39.22f, -16.16f)
                quadToRelative(-16.16f, -16.16f, -16.16f, -39.22f)
                verticalLineToRelative(-411.08f)
                horizontalLineToRelative(232f)
                verticalLineToRelative(-64.31f)
                quadToRelative(0f, -23.05f, 16.16f, -39.22f)
                quadTo(445.87f, 120f, 468.92f, 120f)
                horizontalLineToRelative(123.7f)
                quadToRelative(23.05f, 0f, 39.22f, 16.16f)
                quadTo(648f, 152.33f, 648f, 175.38f)
                verticalLineToRelative(64.31f)
                horizontalLineToRelative(232f)
                verticalLineToRelative(411.08f)
                quadToRelative(0f, 23.06f, -16.16f, 39.22f)
                quadToRelative(-16.17f, 16.16f, -39.22f, 16.16f)
                horizontalLineToRelative(-587.7f)
                close()
                moveTo(236.92f, 675.38f)
                horizontalLineToRelative(587.7f)
                quadToRelative(9.23f, 0f, 16.92f, -7.69f)
                quadToRelative(7.69f, -7.69f, 7.69f, -16.92f)
                verticalLineToRelative(-380.31f)
                lineTo(212.31f, 270.46f)
                verticalLineToRelative(380.31f)
                quadToRelative(0f, 9.23f, 7.69f, 16.92f)
                quadToRelative(7.69f, 7.69f, 16.92f, 7.69f)
                close()
                moveTo(444.31f, 239.69f)
                horizontalLineToRelative(172.92f)
                verticalLineToRelative(-64.31f)
                quadToRelative(0f, -9.23f, -7.69f, -16.92f)
                quadToRelative(-7.69f, -7.69f, -16.92f, -7.69f)
                horizontalLineToRelative(-123.7f)
                quadToRelative(-9.23f, 0f, -16.92f, 7.69f)
                quadToRelative(-7.69f, 7.69f, -7.69f, 16.92f)
                verticalLineToRelative(64.31f)
                close()
                moveTo(212.31f, 675.38f)
                verticalLineToRelative(-404.92f)
                verticalLineToRelative(404.92f)
                close()
            }
        }.build()

        return _Cases!!
    }

@Suppress("ObjectPropertyName")
private var _Cases: ImageVector? = null
