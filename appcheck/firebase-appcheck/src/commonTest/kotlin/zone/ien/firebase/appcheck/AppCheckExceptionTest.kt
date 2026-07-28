package zone.ien.firebase.appcheck

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class AppCheckExceptionTest {
    @Test
    fun testAppCheckExceptionPreservesMessageAndCause() {
        val cause = IllegalArgumentException("잘못된 토큰")
        val exception = AppCheckException("검증 실패", cause)

        assertEquals("검증 실패", exception.message)
        assertSame(cause, exception.cause)
    }
}
