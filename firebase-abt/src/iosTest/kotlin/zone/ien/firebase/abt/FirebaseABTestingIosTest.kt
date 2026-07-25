package zone.ien.firebase.abt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FirebaseABTestingIosTest {
    @Test
    fun 실험_목록을_메모리에서_교체한다() {
        val testing = FirebaseABTesting()

        testing.replaceAllExperiments(
            listOf(
                mapOf("experimentId" to "checkout", "variantId" to "a"),
                mapOf("experimentId" to "home", "variantId" to "b")
            ),
            originService = "remote-config"
        )

        assertEquals(
            listOf("checkout" to "a", "home" to "b"),
            testing.getAllExperiments("remote-config").map {
                it.experimentId to it.variantId
            }
        )
    }

    @Test
    fun 실험_목록을_모두_제거한다() {
        val testing = FirebaseABTesting()
        testing.replaceAllExperiments(
            listOf(mapOf("experimentId" to "checkout", "variantId" to "a")),
            originService = "remote-config"
        )

        testing.removeAllExperiments("remote-config")

        assertTrue(testing.getAllExperiments("remote-config").isEmpty())
    }

    @Test
    fun 누락된_실험_필드는_빈_문자열로_보존한다() {
        val testing = FirebaseABTesting()

        testing.replaceAllExperiments(listOf(emptyMap()), "remote-config")

        val experiment = testing.getAllExperiments("remote-config").single()
        assertEquals("", experiment.experimentId)
        assertEquals("", experiment.variantId)
    }
}
