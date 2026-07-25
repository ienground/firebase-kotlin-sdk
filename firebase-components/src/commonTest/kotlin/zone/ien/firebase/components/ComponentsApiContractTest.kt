package zone.ien.firebase.components

import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentsApiContractTest {
    @Test
    fun 컴포넌트_계약이_공통_API에_노출된다() {
        assertEquals("Component", Component::class.simpleName)
        assertEquals("ComponentRegistrar", ComponentRegistrar::class.simpleName)
    }
}
