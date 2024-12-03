package screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import locals.LocalSharedTransitionScope
import model.GithubRepo
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProjectScreen(project: GithubRepo, contentPadding: PaddingValues, animatedContentScope: AnimatedContentScope) {
    val transitionScope = LocalSharedTransitionScope.current

    with(transitionScope) {
        Column(
            modifier = Modifier.padding(contentPadding).sharedElement(
                transitionScope.rememberSharedContentState("project-${project.id}"),
                animatedContentScope
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(project.name, fontSize = 4.em, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview
@Composable
private fun ProjectScreenPreview() {
//    ProjectScreen()
}