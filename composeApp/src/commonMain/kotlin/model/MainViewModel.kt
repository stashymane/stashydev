package model

import androidx.lifecycle.ViewModel
import httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _repoState: MutableStateFlow<RepoState> = MutableStateFlow(RepoState.Loading)
    val repoState = _repoState.asStateFlow()

    suspend fun loadRepositories() {
        if (_repoState.value is RepoState.Loaded) return

        _repoState.emit(RepoState.Loading)
        try {
            val response = httpClient.get("https://api.github.com/users/stashymane/repos")
            val repos = response.body<List<GithubRepo>>()
            _repoState.emit(RepoState.Loaded(repos))
        } catch (e: Exception) {
            _repoState.emit(RepoState.Failed(e))
        }
    }

    sealed class RepoState {
        data object Loading : RepoState()
        data class Loaded(val repos: List<GithubRepo>) : RepoState()
        data class Failed(val reason: Throwable) : RepoState()
    }
}