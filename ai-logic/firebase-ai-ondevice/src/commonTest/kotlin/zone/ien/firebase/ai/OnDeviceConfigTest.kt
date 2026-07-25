package zone.ien.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals

class OnDeviceConfigTest {
    @Test
    fun 모든_추론_모드로_설정을_생성할_수_있다() {
        InferenceMode.entries.forEach { mode ->
            assertEquals(mode, OnDeviceConfig(mode).mode)
        }
    }

    @Test
    fun 추론_모드는_지원하는_항목을_모두_제공한다() {
        assertEquals(
            listOf("PREFER_ON_DEVICE", "PREFER_IN_CLOUD", "ONLY_ON_DEVICE"),
            InferenceMode.entries.map { it.name }
        )
    }
}
