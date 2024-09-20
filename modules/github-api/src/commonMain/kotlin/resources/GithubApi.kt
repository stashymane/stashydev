package resources

import io.ktor.resources.*
import kotlinx.serialization.SerialName
import model.GithubRepo

@Resource("https://api.github.com/")
class GithubApi {
    @Resource("users")
    class Users(val parent: GithubApi = GithubApi()) {
        @Resource("{username}")
        class ByUsername(val username: String, val parent: Users = Users()) {
            @Resource("repos")
            class Repos(
                val parent: ByUsername,
                val type: GithubRepo.OwnerType? = null,
                val sort: GithubRepo.SortBy? = null,
                val direction: GithubRepo.Direction? = null,
                @SerialName("per_page") val limit: Int? = null,
                val page: Int? = null
            ) {
                constructor(username: String) : this(ByUsername(username))
            }
        }
    }
}