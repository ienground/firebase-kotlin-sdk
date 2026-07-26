package zone.ien.firebase.abt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AbtValidationTest {
    @Test
    fun 지원하는_origin과_legacy_alias만_허용한다() {
        assertEquals("frc", normalizeAbtOriginService("frc"))
        assertEquals("fiam", normalizeAbtOriginService("fiam"))
        assertEquals("frc", normalizeAbtOriginService("remote-config"))
        assertEquals("fiam", normalizeAbtOriginService("in-app-messaging"))
        assertFailsWith<IllegalArgumentException> {
            normalizeAbtOriginService("custom")
        }
    }

    @Test
    fun triggerEvent를_제외한_필수_필드를_모두_검증한다() {
        val valid = validExperiment()
        validateAbtExperiments(listOf(valid))

        REQUIRED_ABT_EXPERIMENT_KEYS.forEach { key ->
            assertFailsWith<AbtException>("누락 필드: $key") {
                validateAbtExperiments(listOf(valid - key))
            }
        }
    }

    @Test
    fun 시작_시각과_시간값의_형식을_검증한다() {
        listOf(
            "2026-02-30T00:00:00",
            "2026-07-26 00:00:00",
            "2026-07-26T24:00:00"
        ).forEach { invalidStartTime ->
            assertFailsWith<AbtException> {
                validateAbtExperiments(
                    listOf(validExperiment() + (ABT_EXPERIMENT_START_TIME_KEY to invalidStartTime))
                )
            }
        }

        listOf(ABT_TRIGGER_TIMEOUT_KEY, ABT_TIME_TO_LIVE_KEY).forEach { key ->
            assertFailsWith<AbtException> {
                validateAbtExperiments(listOf(validExperiment() + (key to "invalid")))
            }
            assertFailsWith<AbtException> {
                validateAbtExperiments(listOf(validExperiment() + (key to "-1")))
            }
        }
    }

    private fun validExperiment(): Map<String, String> = mapOf(
        ABT_EXPERIMENT_ID_KEY to "checkout",
        ABT_VARIANT_ID_KEY to "a",
        ABT_EXPERIMENT_START_TIME_KEY to "2026-07-26T00:00:00",
        ABT_TRIGGER_TIMEOUT_KEY to "1000",
        ABT_TIME_TO_LIVE_KEY to "2000"
    )
}
