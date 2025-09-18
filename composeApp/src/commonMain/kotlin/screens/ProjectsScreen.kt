package screens

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.filled.ArrowBack
import locals.LocalBackStack
import locals.LocalSharedTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProjectsScreen(contentPadding: PaddingValues) {
    val transitionScope = LocalSharedTransitionScope.current
    val backStack = LocalBackStack.current

    LazyVerticalGrid(
        modifier =
            Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(480.dp),
        contentPadding = contentPadding
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { backStack.removeLastOrNull() }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
                Text("Projects", style = MaterialTheme.typography.headlineLarge)
            }
        }

        item {
            Text("TODO make content lol")
        }
    }
}
