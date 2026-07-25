package zone.ien.firebase.database

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class DatabaseModelsTest {
    @Test
    fun 트랜잭션_결과는_성공과_중단을_제공한다() {
        assertEquals(listOf("SUCCESS", "ABORT"), TransactionResult.entries.map { it.name })
    }

    @Test
    fun 데이터베이스_예외는_메시지와_원인을_보존한다() {
        val cause = IllegalStateException("원인")
        val exception = DatabaseException("실패", cause)

        assertEquals("실패", exception.message)
        assertSame(cause, exception.cause)
    }
}
