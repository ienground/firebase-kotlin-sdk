package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class QueryModelsTest {
    @Test
    fun 공통_계약에서_기존_무인자_get과_snapshots를_호출할_수_있다() {
        val queryGet: suspend (Query) -> QuerySnapshot = { it.get() }
        val querySnapshots: (Query) -> Flow<QuerySnapshot> = { it.snapshots() }
        val documentGet: suspend (DocumentReference) -> DocumentSnapshot = { it.get() }
        val documentSnapshots: (DocumentReference) -> Flow<DocumentSnapshot?> = { it.snapshots() }

        assertNotNull(queryGet)
        assertNotNull(querySnapshots)
        assertNotNull(documentGet)
        assertNotNull(documentSnapshots)
    }

    @Test
    fun 정렬_방향은_오름차순과_내림차순을_제공한다() {
        assertEquals(listOf("ASCENDING", "DESCENDING"), QueryDirection.entries.map { it.name })
    }

    @Test
    fun 조건_연산자는_지원하는_항목을_모두_제공한다() {
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
}
