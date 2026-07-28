package zone.ien.firebase.appcheck

import kotlin.test.Test
import kotlin.test.assertEquals
import zone.ien.firebase.appcheck.interop.AppCheckTokenListener
import zone.ien.firebase.appcheck.interop.InteropAppCheckTokenProvider

class AppCheckInteropApiContractTest {
    @Test
    fun testInteropTypesExposedInCommonApi() {
        assertEquals("AppCheckTokenResult", AppCheckTokenResult::class.simpleName)
        assertEquals("InteropAppCheckTokenProvider", InteropAppCheckTokenProvider::class.simpleName)
        assertEquals("AppCheckTokenListener", AppCheckTokenListener::class.simpleName)
    }
}
