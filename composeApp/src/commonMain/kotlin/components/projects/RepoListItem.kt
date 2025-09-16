package components.projects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.GithubRepo
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun RepoListItem(
    repo: GithubRepo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.clickable(onClick = onClick).then(modifier)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)) {
            Column {
                Text(repo.name, style = MaterialTheme.typography.titleLarge)

                repo.description?.let {
                    Text(it)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                arrayOf(
                    Icons.Default.Star to "${repo.stars}",
                    Icons.Default.Watch to "${repo.watchers}",
                    Icons.Default.ForkRight to "${repo.forks}"
                ).forEach {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = it.first, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text(it.second)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                    listOf(
                        repo.license?.name to Icons.Default.DocumentScanner,
                        repo.language to Icons.Default.Code,
                        repo.updatedAt?.toString() to Icons.Default.CalendarToday
                    ).forEach {
                        it.first?.let { text ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = it.second,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(text)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RepoCardPreview() {

}
