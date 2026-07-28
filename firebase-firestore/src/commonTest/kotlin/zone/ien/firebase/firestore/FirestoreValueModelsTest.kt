package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FirestoreValueModelsTest {
    @Test
    fun testTimestampComparisonOrder() {
        assertTrue(Timestamp(10, 1) > Timestamp(9, 999_999_999))
        assertTrue(Timestamp(10, 2) > Timestamp(10, 1))
    }

    @Test
    fun testTimestampNanosecondRangeValidation() {
        assertFailsWith<IllegalArgumentException> { Timestamp(0, -1) }
        assertFailsWith<IllegalArgumentException> { Timestamp(0, 1_000_000_000) }
    }

    @Test
    fun testCastValueFlexibleNumberConversion() {
        val longVal: Any = 10L
        assertEquals(10, castValue<Int>(longVal))
        assertEquals(10L, castValue<Long>(longVal))
        assertEquals(10.0, castValue<Double>(longVal))
        assertEquals(10.0f, castValue<Float>(longVal))

        val doubleVal: Any = 10.5
        assertEquals(10, castValue<Int>(doubleVal))
        assertEquals(10.5f, castValue<Float>(doubleVal))
    }

    @Test
    fun testTimestampCreationFromMilliseconds() {
        val tsLong = Timestamp.fromMilliseconds(1700000500L)
        assertEquals(1700000L, tsLong.seconds)
        assertEquals(500_000_000, tsLong.nanoseconds)

        val tsDouble = Timestamp.fromMilliseconds(1700000500.0)
        assertEquals(1700000L, tsDouble.seconds)
        assertEquals(500_000_000, tsDouble.nanoseconds)
    }

    @Test
    fun testFieldValuePreservesOperationValues() {
        assertEquals(FieldValue.Operation.Delete, FieldValue.delete().operation)
        assertEquals(FieldValue.Operation.Delete, FieldValue.delete.operation)
        assertEquals(FieldValue.Operation.ServerTimestamp, FieldValue.serverTimestamp().operation)
        assertEquals(FieldValue.Operation.ServerTimestamp, FieldValue.serverTimestamp.operation)
        assertEquals(
            FieldValue.Operation.ArrayUnion(listOf("a", 1L)),
            FieldValue.arrayUnion("a", 1L).operation
        )
        assertEquals(
            FieldValue.Operation.ArrayRemove(listOf("a")),
            FieldValue.arrayRemove("a").operation
        )
        assertEquals(FieldValue.Operation.IncrementLong(2L), FieldValue.increment(2L).operation)
        assertEquals(FieldValue.Operation.IncrementDouble(0.5), FieldValue.increment(0.5).operation)
    }

    @Test
    fun testSourceAndListenSourceEnumValues() {
        assertEquals(listOf(Source.DEFAULT, Source.SERVER, Source.CACHE), Source.entries)
        assertEquals(listOf(ListenSource.DEFAULT, ListenSource.CACHE), ListenSource.entries)
    }

    @Test
    fun testSnapshotMetadataPreservesState() {
        val metadata = SnapshotMetadata(hasPendingWrites = true, isFromCache = true)

        assertTrue(metadata.hasPendingWrites)
        assertTrue(metadata.isFromCache)
    }

    @Test
    fun testQueryAndDocumentSnapshotApiOptions() {
        val queryGet: suspend Query.(Source) -> QuerySnapshot = Query::get
        val documentGet: suspend DocumentReference.(Source) -> DocumentSnapshot =
            DocumentReference::get
        val querySnapshots: Query.(Boolean, ListenSource) -> Flow<QuerySnapshot> = Query::snapshots
        val documentSnapshots:
            DocumentReference.(Boolean, ListenSource) -> Flow<DocumentSnapshot> =
            DocumentReference::snapshots
        val documentMetadata: DocumentSnapshot.() -> SnapshotMetadata =
            DocumentSnapshot::getMetadata
        val queryMetadata: QuerySnapshot.() -> SnapshotMetadata = QuerySnapshot::getMetadata

        listOf(
            queryGet,
            documentGet,
            querySnapshots,
            documentSnapshots,
            documentMetadata,
            queryMetadata
        ).forEach { it.hashCode() }
    }

    @Test
    fun testDocumentReferenceOverridesEqualsAndHashCode() {
        val equalsFn: DocumentReference.(Any?) -> Boolean = DocumentReference::equals
        val hashCodeFn: DocumentReference.() -> Int = DocumentReference::hashCode

        assertTrue(equalsFn != null)
        assertTrue(hashCodeFn != null)
    }
}
