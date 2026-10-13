package zone.ien.firebase.appcheck.debug

import kotlin.test.Test
import kotlin.test.assertEquals

class DebugProviderApiContractTest {
    @Test
    fun testDebugProviderFactoryExposedInCommonApi() {
        assertEquals("DebugAppCheckProviderFactory", DebugAppCheckProviderFactory::class.simpleName)
    }
}
