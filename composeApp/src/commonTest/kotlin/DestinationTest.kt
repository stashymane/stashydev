import model.Destination
import kotlin.test.Test

class DestinationTest {
    @Test
    fun testRoute() {
        println(Destination.Home.toUri())
        println(Destination.Projects.List.toUri())
        println(Destination.Projects.Id(1234).toUri())
    }
}
