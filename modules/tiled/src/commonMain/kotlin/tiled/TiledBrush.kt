package tiled

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import coil3.Image

fun ImageBitmap.toTiledBrush(
    tileModeX: TileMode = TileMode.Repeated,
    tileModeY: TileMode = TileMode.Repeated,
): ShaderBrush = ShaderBrush(ImageShader(this, tileModeX, tileModeY))

fun Image.toTiledBrush(
    tileModeX: TileMode = TileMode.Repeated,
    tileModeY: TileMode = TileMode.Repeated,
): ShaderBrush = toImageBitmap().toTiledBrush(tileModeX, tileModeY)

expect fun Image.toImageBitmap(): ImageBitmap
