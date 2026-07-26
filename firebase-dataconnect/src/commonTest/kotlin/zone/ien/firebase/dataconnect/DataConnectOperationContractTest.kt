package zone.ien.firebase.dataconnect

import kotlin.test.Test
import kotlin.test.assertEquals

class DataConnectOperationContractTest {
    @Test
    fun 쿼리_결과가_요청과_데이터_출처를_보존한다() {
        val result = DataConnectQueryResult(
            operationName = "GetMovie",
            variables = mapOf("id" to "movie-1"),
            data = listOf("Arrival"),
            dataSource = DataConnectDataSource.SERVER
        )

        assertEquals("GetMovie", result.operationName)
        assertEquals(mapOf("id" to "movie-1"), result.variables)
        assertEquals(listOf("Arrival"), result.data)
        assertEquals(DataConnectDataSource.SERVER, result.dataSource)
    }

    @Test
    fun mutation_결과가_요청과_응답을_보존한다() {
        val result = DataConnectMutationResult(
            operationName = "AddMovie",
            variables = mapOf("title" to "Arrival"),
            data = "movie-1"
        )

        assertEquals("AddMovie", result.operationName)
        assertEquals(mapOf("title" to "Arrival"), result.variables)
        assertEquals("movie-1", result.data)
    }

    @Test
    fun operation_오류가_partial_response와_실패_종류를_보존한다() {
        val response = DataConnectFailureResponse(
            rawData = mapOf("movie" to mapOf("title" to "Arrival")),
            data = "partial",
            errors = listOf(
                DataConnectError(
                    message = "rating을 읽을 수 없음",
                    path = listOf(
                        DataConnectErrorPathSegment.Field("movie"),
                        DataConnectErrorPathSegment.Field("rating")
                    )
                )
            )
        )

        val exception = DataConnectOperationException(
            operationName = "GetMovie",
            kind = DataConnectFailureKind.OPERATION,
            response = response,
            cause = IllegalStateException("partial response")
        )

        assertEquals(DataConnectFailureKind.OPERATION, exception.kind)
        assertEquals("partial", exception.response?.data)
        assertEquals("rating을 읽을 수 없음", exception.response?.errors?.single()?.message)
        assertEquals(
            DataConnectErrorPathSegment.Field("rating"),
            exception.response?.errors?.single()?.path?.last()
        )
    }
}
