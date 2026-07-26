package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FirestoreValueModelsTest {
    @Test
    fun Timestamp는_초와_나노초_순서로_비교한다() {
        assertTrue(Timestamp(10, 1) > Timestamp(9, 999_999_999))
        assertTrue(Timestamp(10, 2) > Timestamp(10, 1))
    }

    @Test
    fun Timestamp는_나노초_범위를_검증한다() {
        assertFailsWith<IllegalArgumentException> { Timestamp(0, -1) }
        assertFailsWith<IllegalArgumentException> { Timestamp(0, 1_000_000_000) }
    }

    @Test
    fun FieldValue는_쓰기_변환에_필요한_연산값을_보존한다() {
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
    fun 조회와_리스너_소스는_네이티브_SDK_지원범위를_구분한다() {
        assertEquals(listOf(Source.DEFAULT, Source.SERVER, Source.CACHE), Source.entries)
        assertEquals(listOf(ListenSource.DEFAULT, ListenSource.CACHE), ListenSource.entries)
    }

    @Test
    fun SnapshotMetadata는_캐시와_대기중_쓰기_상태를_보존한다() {
        val metadata = SnapshotMetadata(hasPendingWrites = true, isFromCache = true)

        assertTrue(metadata.hasPendingWrites)
        assertTrue(metadata.isFromCache)
    }

    @Test
    fun 조회와_스냅샷_API는_소스와_메타데이터_옵션을_노출한다() {
        val queryGet: suspend Query.(Source) -> QuerySnapshot = Query::get
        val documentGet: suspend DocumentReference.(Source) -> DocumentSnapshot =
            DocumentReference::get
        val querySnapshots: Query.(Boolean, ListenSource) -> Flow<QuerySnapshot> = Query::snapshots
        val documentSnapshots:
            DocumentReference.(Boolean, ListenSource) -> Flow<DocumentSnapshot?> =
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
}
