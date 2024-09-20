package screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import components.util.AnimatedLaunch
import components.util.NullSwapper
import kotlinx.serialization.json.Json
import model.HomeLink
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes

@OptIn(InternalResourceApi::class)
@Composable
fun HomeScreen(contentPadding: PaddingValues) {
    val uriHandler = LocalUriHandler.current
    var links: List<HomeLink>? by rememberSaveable { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        try {
            links = Json.decodeFromString(readResourceBytes("/strings/links.json").decodeToString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(
        Modifier.fillMaxSize().padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {

        AnimatedLaunch(
            0f to 1f, {
                alpha = it
                translationY = (1 - it) * 10
            },
            spec = tween(1000, easing = LinearOutSlowInEasing)
        ) {
            Text(
                "stashymane",
                modifier = it,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )
        }

        NullSwapper(links) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                it.forEachIndexed { index, (icon, name, subtitle, url) ->
                    AnimatedLaunch(
                        0f to 1f,
                        {
                            alpha = it
                            translationY = (1 - it) * 10
                        },
                        tween(1000, delayMillis = 250 + index * 250, easing = LinearOutSlowInEasing)
                    ) { modifier ->
                        TextButton({ uriHandler.openUri(url) }, modifier = modifier) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.AirportShuttle, contentDescription = null)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(name)
                                    subtitle?.let {
                                        Text(
                                            it,
                                            style = LocalTextStyle.current.copy(
                                                color = LocalContentColor.current.copy(alpha = 0.5f),
                                                fontSize = 0.7.em
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
