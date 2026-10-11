package zone.ien.firebase.components

import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentsApiContractTest {
    @Test
    fun testComponentContractExposedInCommonApi() {
        assertEquals("Component", Component::class.simpleName)
        assertEquals("ComponentRegistrar", ComponentRegistrar::class.simpleName)
    }
}
