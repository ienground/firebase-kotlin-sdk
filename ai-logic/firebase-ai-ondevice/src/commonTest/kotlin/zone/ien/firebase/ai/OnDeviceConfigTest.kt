package zone.ien.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals

class OnDeviceConfigTest {
    @Test
    fun testCanCreateConfigWithAllInferenceModes() {
        InferenceMode.entries.forEach { mode ->
            assertEquals(mode, OnDeviceConfig(mode).mode)
        }
    }

    @Test
    fun testInferenceModeProvidesAllSupportedValues() {
        assertEquals(
            listOf("PREFER_ON_DEVICE", "PREFER_IN_CLOUD", "ONLY_ON_DEVICE"),
            InferenceMode.entries.map { it.name }
        )
    }
}
