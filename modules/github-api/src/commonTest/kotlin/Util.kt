import com.goncalossilva.resources.Resource
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.shouldBe

fun loadResource(path: String): Resource {
    val resource = Resource(path)
    resource.exists().shouldBe(true)
    return resource
}

inline fun <reified T> String.shouldDecodeTo(): T = shouldNotThrowAny {
    json.decodeFromString<T>(this)
}
