package zone.ien.firebase.abt

import java.util.Date
import java.util.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AbtExperimentInfoAndroidTest {
    @Test
    fun 공식_Android_실험_정보의_전체_map_필드를_보존한다() {
        val previousTimeZone = TimeZone.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        try {
            val nativeInfo = com.google.firebase.abt.AbtExperimentInfo(
                "checkout",
                "a",
                "purchase",
                Date(0L),
                1_000L,
                2_000L
            )

            val info = AbtExperimentInfo(nativeInfo)

            assertEquals("checkout", info.experimentId)
            assertEquals("a", info.variantId)
            assertEquals("purchase", info.triggerEvent)
            assertEquals(19, info.experimentStartTime.length)
            assertEquals(1_000L, info.triggerTimeoutMillis)
            assertEquals(2_000L, info.timeToLiveMillis)
        } finally {
            TimeZone.setDefault(previousTimeZone)
        }
    }

    @Test
    fun 공식_Android_실험_정보_변환에_실패하면_예외를_노출한다() {
        val nativeInfo = com.google.firebase.abt.AbtExperimentInfo(
            "checkout",
            "a",
            null,
            Date(0L),
            1_000L,
            2_000L
        )

        assertFailsWith<AbtException> {
            nativeInfo.toAbtExperimentValues("missingMethod")
        }
    }
}
