package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import icons.Icons
import icons.outline.Refresh24Dp
import ui.components.InlineIcon
import ui.preview.ComponentPreview
import ui.preview.PreviewHost

@Composable
fun LoadingFailedScreen(
    modifier: Modifier = Modifier,
    error: Throwable? = null,
    onRetry: (() -> Unit)? = null,
    description: @Composable () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var showErrorDialog by remember { mutableStateOf(false) }

    Box(modifier.fillMaxSize().padding(32.dp), contentAlignment = Center) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            description()

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (error != null) {
                    TextButton({ showErrorDialog = true }) {
                        Text("More details")
                    }
                }

                onRetry?.let { onRetry ->
                    Button({
                        if (lifecycleOwner.lifecycle.currentState.isAtLeast(RESUMED))
                            onRetry()
                    }) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InlineIcon(Icons.Outline.Refresh24Dp)
                            Text("Try again")
                        }
                    }
                }
            }
        }
    }

    if (showErrorDialog && error != null) {
        ErrorDialog(error) { showErrorDialog = false }
    }
}

@Composable
private fun ErrorDialog(error: Throwable, onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismiss,
        {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = {
                    uriHandler.openUri("https://github.com/stashymane/stashydev/discussions/new?category=general")
                }) {
                    Text("Report issue")
                }

                Button(onClick = onDismiss) {
                    Text("Close")
                }
            }
        },
        title = { Text("An error occurred.") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(error.message ?: "No message available.")

                TextField(
                    error.stackTraceToString(),
                    {},
                    Modifier.weight(1f, false).heightIn(max = 600.dp),
                    readOnly = true,
                    shape = MaterialTheme.shapes.medium
                )

                Text("Before reporting, make sure this issue isn't caused by external factors, like network instability or your browser.")
            }
        }
    )
}

@ComponentPreview
@Composable
private fun LoadingFailedScreenPreview() = PreviewHost {
    LoadingFailedScreen(onRetry = {}, error = IllegalArgumentException("woops")) {
        Text("Failed to load content.")
    }
}

@ComponentPreview
@Composable
private fun ErrorDialogPreview() = PreviewHost {
    ErrorDialog(IllegalArgumentException("Something went wrong")) {}
}
