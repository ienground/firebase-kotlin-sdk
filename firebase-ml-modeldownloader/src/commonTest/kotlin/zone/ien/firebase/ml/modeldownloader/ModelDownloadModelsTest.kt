package zone.ien.firebase.ml.modeldownloader

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModelDownloadModelsTest {
    @Test
    fun testDownloadConditionsDefaultsDisabled() {
        val conditions = CustomModelDownloadConditions.Builder().build()

        assertFalse(conditions.requireWifi)
        assertFalse(conditions.requireDeviceIdle)
        assertFalse(conditions.requireCharging)
    }

    @Test
    fun testCanCombineDownloadConditions() {
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .requireDeviceIdle()
            .requireCharging()
            .build()

        assertTrue(conditions.requireWifi)
        assertTrue(conditions.requireDeviceIdle)
        assertTrue(conditions.requireCharging)
    }

    @Test
    fun testDownloadTypeProvidesAllSupportedValues() {
        assertEquals(
            listOf("LOCAL_MODEL", "LOCAL_MODEL_UPDATE_IN_BACKGROUND", "LATEST_MODEL"),
            DownloadType.entries.map { it.name }
        )
    }
}
