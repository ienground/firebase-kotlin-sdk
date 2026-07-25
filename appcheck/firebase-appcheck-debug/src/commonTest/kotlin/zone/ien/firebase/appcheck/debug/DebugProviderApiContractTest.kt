package zone.ien.firebase.appcheck.debug

import kotlin.test.Test
import kotlin.test.assertEquals

class DebugProviderApiContractTest {
    @Test
    fun 디버그_공급자_팩토리가_공통_API에_노출된다() {
        assertEquals("DebugAppCheckProviderFactory", DebugAppCheckProviderFactory::class.simpleName)
    }
}
