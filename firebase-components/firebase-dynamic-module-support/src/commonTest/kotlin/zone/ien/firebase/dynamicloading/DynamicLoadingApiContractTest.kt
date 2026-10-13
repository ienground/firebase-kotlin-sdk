package zone.ien.firebase.dynamicloading

import kotlin.test.Test
import kotlin.test.assertEquals

class DynamicLoadingApiContractTest {
    @Test
    fun testDynamicLoadingRegistrarExposedInCommonApi() {
        assertEquals("DynamicLoadingRegistrar", DynamicLoadingRegistrar::class.simpleName)
    }
}
