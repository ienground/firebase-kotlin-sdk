package zone.ien.firebase.appcheck

import kotlin.test.Test
import kotlin.test.assertEquals
import zone.ien.firebase.appcheck.interop.AppCheckTokenListener
import zone.ien.firebase.appcheck.interop.InteropAppCheckTokenProvider

class AppCheckInteropApiContractTest {
    @Test
    fun 상호운용_타입이_공통_API에_노출된다() {
        assertEquals("AppCheckTokenResult", AppCheckTokenResult::class.simpleName)
        assertEquals("InteropAppCheckTokenProvider", InteropAppCheckTokenProvider::class.simpleName)
        assertEquals("AppCheckTokenListener", AppCheckTokenListener::class.simpleName)
    }
}
