import model.GithubRepo
import kotlin.test.Test

class SerializationTests {
    @Test
    fun testRepo() {
        loadResource("data/repo/get.json").readText().shouldDecodeTo<GithubRepo>()
    }

    @Test
    fun testUser() {
        loadResource("data/repo/list.json").readText().shouldDecodeTo<List<GithubRepo>>()
    }
}
