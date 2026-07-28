package zone.ien.firebase.abt

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FirebaseABTestingIosTest {
    @Test
    fun testRecordedExperimentNotReportedAsApplied() {
        val testing = FirebaseABTesting("frc")

        testing.replaceAllExperiments(
            listOf(
                mapOf(
                    "experimentId" to "checkout",
                    "variantId" to "a",
                    "triggerEvent" to "purchase",
                    "experimentStartTime" to "2026-07-26T00:00:00",
                    "triggerTimeoutMillis" to "1000",
                    "timeToLiveMillis" to "2000"
                )
            )
        )

        assertTrue(testing.getAllExperiments().isEmpty())
        assertEquals(
            listOf("checkout" to "a"),
            testing.requestedExperiments.map {
                it.experimentId to it.variantId
            }
        )

        val experiment = testing.requestedExperiments.first()
        assertEquals("purchase", experiment.triggerEvent)
        assertEquals("2026-07-26T00:00:00", experiment.experimentStartTime)
        assertEquals(1_000L, experiment.triggerTimeoutMillis)
        assertEquals(2_000L, experiment.timeToLiveMillis)
        assertEquals(AbtApplicationStatus.RECORDED_NOT_APPLIED, testing.lastResult?.status)
        assertEquals(AbtRequestType.REPLACE_ALL, testing.lastRequest?.type)
        assertEquals(false, testing.lastResult?.isNativeApplicationSupported)
    }

    @Test
    fun testClearsAllExperiments() {
        val testing = FirebaseABTesting("frc")
        testing.replaceAllExperiments(
            listOf(validExperiment()),
            originService = "remote-config"
        )

        testing.removeAllExperiments("remote-config")

        assertTrue(testing.getAllExperiments("remote-config").isEmpty())
        assertTrue(testing.requestedExperiments.isEmpty())
        assertEquals(AbtRequestType.REMOVE_ALL, testing.lastRequest?.type)
    }

    @Test
    fun testRejectsMissingRequiredExperimentFields() {
        val testing = FirebaseABTesting("frc")

        assertFailsWith<AbtException> {
            testing.replaceAllExperiments(listOf(emptyMap()), "remote-config")
        }
    }

    @Test
    fun testRejectsDifferentOriginInOverloadCall() {
        val testing = FirebaseABTesting("frc")

        assertEquals("frc", testing.originService)
        assertFailsWith<IllegalArgumentException> {
            testing.replaceAllExperiments(emptyList(), "fiam")
        }
    }

    @Test
    fun suspend_요청도_미적용_기록_결과를_반환한다() = runBlocking {
        val testing = FirebaseABTesting("fiam")

        val result = testing.replaceAllExperimentsAsync(listOf(validExperiment()))

        assertEquals(AbtApplicationStatus.RECORDED_NOT_APPLIED, result.status)
        assertEquals(false, result.isNativeApplicationSupported)
        assertEquals("checkout", testing.requestedExperiments.single().experimentId)
        assertTrue(testing.getAllExperimentsAsync().isEmpty())
    }


    private fun validExperiment(): Map<String, String> = mapOf(
        "experimentId" to "checkout",
        "variantId" to "a",
        "experimentStartTime" to "2026-07-26T00:00:00",
        "triggerTimeoutMillis" to "1000",
        "timeToLiveMillis" to "2000"
    )
}
