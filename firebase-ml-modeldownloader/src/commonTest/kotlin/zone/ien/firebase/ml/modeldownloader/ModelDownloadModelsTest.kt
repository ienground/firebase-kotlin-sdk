package zone.ien.firebase.ml.modeldownloader

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModelDownloadModelsTest {
    @Test
    fun 다운로드_조건의_기본값은_모두_비활성이다() {
        val conditions = CustomModelDownloadConditions.Builder().build()

        assertFalse(conditions.requireWifi)
        assertFalse(conditions.requireDeviceIdle)
        assertFalse(conditions.requireCharging)
    }

    @Test
    fun 다운로드_조건을_조합할_수_있다() {
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
    fun 다운로드_유형은_지원하는_항목을_모두_제공한다() {
        assertEquals(
            listOf("LOCAL_MODEL", "LOCAL_MODEL_UPDATE_IN_BACKGROUND", "LATEST_MODEL"),
            DownloadType.entries.map { it.name }
        )
    }
}
