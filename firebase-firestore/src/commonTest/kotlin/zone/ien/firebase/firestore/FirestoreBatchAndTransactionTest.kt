package zone.ien.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FirestoreBatchAndTransactionTest {
    @Test
    fun testFirestoreSettingsBuilder() {
        val settings = firestoreSettings {
            host = "custom.firestore.host:8080"
            isSslEnabled = false
            isPersistenceEnabled = true
        }
        assertEquals("custom.firestore.host:8080", settings.host)
        assertEquals(false, settings.isSslEnabled)
        assertEquals(true, settings.isPersistenceEnabled)
    }

    @Test
    fun testAggregateSourceEnum() {
        assertEquals("SERVER", AggregateSource.SERVER.name)
    }

    @Test
    fun testLoadBundleTaskProgressModel() {
        val progress = LoadBundleTaskProgress(
            documentsLoaded = 10,
            totalDocuments = 20,
            bytesLoaded = 1024L,
            totalBytes = 2048L,
            taskState = LoadBundleTaskState.SUCCESS
        )
        assertEquals(10, progress.documentsLoaded)
        assertEquals(20, progress.totalDocuments)
        assertEquals(1024L, progress.bytesLoaded)
        assertEquals(2048L, progress.totalBytes)
        assertEquals(LoadBundleTaskState.SUCCESS, progress.taskState)
    }

    @Test
    fun testAggregateFieldConstructors() {
        assertNotNull(AggregateField.count())
        assertNotNull(AggregateField.sum("price"))
        assertNotNull(AggregateField.average("rating"))
    }
}
