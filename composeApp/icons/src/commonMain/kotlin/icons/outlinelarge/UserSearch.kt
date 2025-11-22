package icons.outlinelarge

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import icons.Icons

val Icons.OutlineLarge.UserSearch: ImageVector
    get() {
        if (_UserSearch != null) {
            return _UserSearch!!
        }
        _UserSearch = ImageVector.Builder(
            name = "OutlineLarge.UserSearch",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(435.53f, 370.77f)
                quadToRelative(23.47f, 0f, 39.35f, -16.04f)
                quadToRelative(15.89f, -16.03f, 15.89f, -39.5f)
                quadToRelative(0f, -23.46f, -16.04f, -39.35f)
                quadTo(458.7f, 260f, 435.23f, 260f)
                quadToRelative(-23.46f, 0f, -39.35f, 16.03f)
                quadTo(380f, 292.07f, 380f, 315.53f)
                quadToRelative(0f, 23.47f, 16.03f, 39.35f)
                quadToRelative(16.04f, 15.89f, 39.5f, 15.89f)
                close()
                moveTo(436.65f, 530.77f)
                quadToRelative(30.81f, 0f, 60.25f, -10.94f)
                quadToRelative(29.43f, -10.94f, 53.41f, -31.6f)
                quadToRelative(-30.46f, -14.61f, -58.93f, -21.42f)
                quadToRelative(-28.46f, -6.81f, -55.23f, -6.81f)
                quadToRelative(-33.69f, 0f, -64.07f, 7.19f)
                quadToRelative(-30.39f, 7.19f, -50.08f, 21.04f)
                quadToRelative(24.42f, 20.41f, 53.75f, 31.48f)
                quadToRelative(29.33f, 11.06f, 60.9f, 11.06f)
                close()
                moveTo(853.38f, 836.15f)
                lineTo(632.38f, 615.15f)
                quadToRelative(-43.07f, 35.85f, -91.11f, 55.73f)
                quadToRelative(-48.04f, 19.89f, -106.19f, 19.89f)
                quadToRelative(-123.31f, 0f, -209.2f, -85.89f)
                quadTo(140f, 519f, 140f, 395.38f)
                quadToRelative(0f, -123.61f, 85.88f, -209.5f)
                quadTo(311.77f, 100f, 435.38f, 100f)
                quadToRelative(123.62f, 0f, 209.5f, 85.88f)
                quadToRelative(85.89f, 85.89f, 85.89f, 209.2f)
                quadToRelative(0f, 58.15f, -19.89f, 106.57f)
                quadToRelative(-19.88f, 48.43f, -55.73f, 91.5f)
                lineToRelative(220.23f, 220.23f)
                lineToRelative(-22f, 22.77f)
                close()
                moveTo(435.88f, 660f)
                quadToRelative(109.66f, 0f, 186.89f, -77.73f)
                quadTo(700f, 504.54f, 700f, 394.88f)
                quadToRelative(0f, -109.65f, -77.15f, -186.88f)
                quadToRelative(-77.14f, -77.23f, -187.47f, -77.23f)
                quadToRelative(-109.15f, 0f, -186.88f, 77.15f)
                quadToRelative(-77.73f, 77.14f, -77.73f, 187.46f)
                quadToRelative(0f, 109.16f, 77.73f, 186.89f)
                quadTo(326.23f, 660f, 435.88f, 660f)
                close()
                moveTo(435.38f, 395.38f)
                close()
            }
        }.build()

        return _UserSearch!!
    }

@Suppress("ObjectPropertyName")
private var _UserSearch: ImageVector? = null
