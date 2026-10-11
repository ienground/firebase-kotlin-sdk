package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class QueryModelsTest {
    @Test
    fun testQueryAndDocumentReferenceGetAndSnapshotsDefaults() {
        val queryGet: suspend (Query) -> QuerySnapshot = { it.get() }
        val querySnapshots: (Query) -> Flow<QuerySnapshot> = { it.snapshots() }
        val documentGet: suspend (DocumentReference) -> DocumentSnapshot = { it.get() }
        val documentSnapshots: (DocumentReference) -> Flow<DocumentSnapshot> = { it.snapshots() }

        assertNotNull(queryGet)
        assertNotNull(querySnapshots)
        assertNotNull(documentGet)
        assertNotNull(documentSnapshots)
    }

    @Test
    fun testQueryDirectionEnumValues() {
        assertEquals(listOf("ASCENDING", "DESCENDING"), QueryDirection.entries.map { it.name })
        assertEquals(Direction.ASCENDING, QueryDirection.ASCENDING)
        assertEquals(Direction.DESCENDING, QueryDirection.DESCENDING)
    }

    @Test
    fun testFilterOperatorEnumValues() {
        assertEquals(
            listOf(
                "EQUAL",
                "NOT_EQUAL",
                "LESS_THAN",
                "LESS_THAN_OR_EQUAL",
                "GREATER_THAN",
                "GREATER_THAN_OR_EQUAL",
                "ARRAY_CONTAINS",
                "ARRAY_CONTAINS_ANY",
                "IN",
                "NOT_IN"
            ),
            WhereOperator.entries.map { it.name }
        )
    }

    @Test
    fun FieldPath_of는_여러_세그먼트로_생성된다() {
        val fp = FieldPath.of("address", "city", "street")
        assertNotNull(fp)
    }

    @Test
    fun FieldPath_documentId는_특별_센티널을_반환한다() {
        val fp = FieldPath.documentId()
        assertNotNull(fp)
    }

    @Test
    fun FieldPath_documentId_프로퍼티_접근이_가능하다() {
        val fp = FieldPath.documentId
        assertNotNull(fp)
    }

    @Test
    fun snapshots_단일_인자_오버로드가_정상_참조된다() {
        val querySnapshotsOneArg: (Query, Boolean) -> Flow<QuerySnapshot> = { q, b -> q.snapshots(b) }
        val docSnapshotsOneArg: (DocumentReference, Boolean) -> Flow<DocumentSnapshot> = { d, b -> d.snapshots(b) }
        assertNotNull(querySnapshotsOneArg)
        assertNotNull(docSnapshotsOneArg)
    }

    @Test
    fun snapshots_프로퍼티_접근이_가능하다() {
        val querySnapshotsProp: (Query) -> Flow<QuerySnapshot> = { it.snapshots }
        val docSnapshotsProp: (DocumentReference) -> Flow<DocumentSnapshot> = { it.snapshots }
        assertNotNull(querySnapshotsProp)
        assertNotNull(docSnapshotsProp)
    }

    @Test
    fun DocumentReference_collection_호출이_가능하다() {
        val docRefColl: (DocumentReference, String) -> CollectionReference = DocumentReference::collection
        assertNotNull(docRefColl)
    }

    @Test
    fun DocumentSnapshot_get_제네릭_호출이_가능하다() {
        val getStr: DocumentSnapshot.(String) -> Any? = { get<Any?>(it) }
        val getFp: DocumentSnapshot.(FieldPath) -> Any? = { get<Any?>(it) }
        assertNotNull(getStr)
        assertNotNull(getFp)
    }
}
