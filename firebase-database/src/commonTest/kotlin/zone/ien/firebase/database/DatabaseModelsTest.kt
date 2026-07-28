package zone.ien.firebase.database

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class DatabaseModelsTest {
    @Test
    fun testTransactionResultProvidesSuccessAndAbort() {
        assertEquals(listOf("SUCCESS", "ABORT"), TransactionResult.entries.map { it.name })
    }

    @Test
    fun testDatabaseExceptionPreservesMessageAndCause() {
        val cause = IllegalStateException("원인")
        val exception = DatabaseException("실패", cause)

        assertEquals("실패", exception.message)
        assertSame(cause, exception.cause)
    }
}
