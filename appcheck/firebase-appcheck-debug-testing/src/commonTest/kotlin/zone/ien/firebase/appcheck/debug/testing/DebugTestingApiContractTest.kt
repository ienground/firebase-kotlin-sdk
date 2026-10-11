package zone.ien.firebase.appcheck.debug.testing

import kotlin.test.Test
import kotlin.test.assertEquals

class DebugTestingApiContractTest {
    @Test
    fun testDebugTestingHelpersExposedInCommonApi() {
        assertEquals("DebugAppCheckTestHelper", DebugAppCheckTestHelper::class.simpleName)
    }
}
