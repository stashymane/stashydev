package screens.about

import UserMeta
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import model.Links
import org.koin.compose.viewmodel.koinViewModel
import screens.LoadingFailedScreen
import screens.LoadingScreen
import screens.ScreenContent
import screens.about.components.BusinessCard
import toRelativeString
import ui.LocalContainerSize
import ui.components.LinkButton
import ui.preview.DevicePreview
import ui.preview.PreviewData
import ui.preview.PreviewHost
import ui.theme.ContainerSize.Regular
import kotlin.math.min

@Composable
fun AboutScreen(
    vm: AboutViewmodel = koinViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        vm.onLaunch()
    }

    AnimatedContent(
        state,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { screenState ->
        when (screenState) {
            is AboutScreenState.Loading -> LoadingScreen {}
            is AboutScreenState.Failed -> LoadingFailedScreen(onRetry = vm::onReload) {
                Text("Failed to load profile.")
            }

            is AboutScreenState.Success -> AboutScreenContent(screenState.meta)
        }
    }
}

@OptIn(ExperimentalGridApi::class)
@Composable
private fun AboutScreenContent(meta: UserMeta) = ScreenContent {
    val containerSize = LocalContainerSize.current
    val columns = when {
        containerSize >= Regular -> 3
        else -> 1
    }

    Grid(
        {
            gap(8.dp)

            repeat(columns) {
                column(1f / columns)
            }
        },
        Modifier.fillMaxWidth().padding(8.dp)
    ) {
        BusinessCard(meta = meta, Modifier.gridItem(columnSpan = min(columns, 2)))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Links.Groups.All.forEach { group ->
                Column(
                    Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(8.dp)
                ) {
                    Text(group.name, style = MaterialTheme.typography.labelMedium)

                    group.links.forEach { link ->
                        LinkButton(link.url, Modifier.padding(horizontal = 4.dp))
                    }
                }
            }
        }

//        Column(
//            Modifier.padding(horizontal = 22.dp, vertical = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text("Languages", style = MaterialTheme.typography.headlineMedium, fontWeight = Bold)
//
//            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                LanguageBadge(Project.Language.Kotlin)
//                LanguageBadge(Project.Language.Rust)
//                LanguageBadge(Project.Language.Java)
//            }
//        }

//        Column(
//            Modifier.padding(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Text(
//                "Top languages this month",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold
//            )
//            LanguageShareChart(languageShare = meta.languageShare, Modifier.fillMaxWidth())
//        }
    }

    Row(Modifier.padding(16.dp)) {
        Text(
            "Data generated from GitHub ${meta.generatedAt.toRelativeString()}",
            style = MaterialTheme.typography.labelMedium,
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
    }
}

@DevicePreview
@Composable
private fun AboutScreenPreview() = PreviewHost {
    AboutScreenContent(PreviewData.userMeta)
}
