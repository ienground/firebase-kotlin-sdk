package zone.ien.firebase.dynamicloading

import kotlin.test.Test
import kotlin.test.assertEquals

class DynamicLoadingApiContractTest {
    @Test
    fun 동적_로딩_등록자가_공통_API에_노출된다() {
        assertEquals("DynamicLoadingRegistrar", DynamicLoadingRegistrar::class.simpleName)
    }
}
