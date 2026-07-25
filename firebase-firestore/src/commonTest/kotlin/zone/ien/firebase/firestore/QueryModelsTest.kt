package zone.ien.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals

class QueryModelsTest {
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
}
