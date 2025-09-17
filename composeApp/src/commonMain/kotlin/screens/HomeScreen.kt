package screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import components.SiteFooter
import components.SiteHeader
import components.nav.NavBlock
import dev.stashy.home.Res
import icons.Icons
import icons.outlinelarge.Cases
import icons.outlinelarge.FitScreen
import icons.outlinelarge.UserSearch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeScreen(contentPadding: PaddingValues) {
    val localPlatformContext = LocalPlatformContext.current
    val background = remember {
        ImageRequest.Builder(localPlatformContext).data(Res.getUri("drawable/background.jpg")).crossfade(true).build()
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AsyncImage(
            background,
            null,
            Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.1f
        )

        Column(
            Modifier.widthIn(max = 900.dp).heightIn(max = 720.dp).padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SiteHeader()
            Row(
                Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NavBlock(
                    Modifier.weight(1f),
                    onClick = {},
                    icon = Icons.OutlineLarge.Cases,
                    text = "Projects",
                    background = {
                        AsyncImage(
                            Res.getUri("drawable/spaceman.gif"),
                            null,
                            contentScale = ContentScale.Crop
                        )
                    })
                NavBlock(
                    Modifier.weight(1f),
                    onClick = {},
                    icon = Icons.OutlineLarge.FitScreen,
                    text = "Media",
                    background = {
                        AsyncImage(
                            Res.getUri("drawable/wobble.gif"),
                            null,
                            contentScale = ContentScale.Crop
                        )
                    })
                NavBlock(
                    Modifier.weight(1f),
                    onClick = {},
                    icon = Icons.OutlineLarge.UserSearch,
                    text = "About",
                    background = {
                        AsyncImage(
                            Res.getUri("drawable/skroll.gif"),
                            null,
                            contentScale = ContentScale.Crop
                        )
                    })
            }
            SiteFooter()
        }
    }
}
