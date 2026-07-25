package zone.ien.firebase.appcheck.debug.testing

import kotlin.test.Test
import kotlin.test.assertEquals

class DebugTestingApiContractTest {
    @Test
    fun 디버그_테스트_도우미가_공통_API에_노출된다() {
        assertEquals("DebugAppCheckTestHelper", DebugAppCheckTestHelper::class.simpleName)
    }
}
