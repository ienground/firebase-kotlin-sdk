package zone.ien.firebase.dataconnect.connectors

import com.google.firebase.dataconnect.DataConnectOperationException as AndroidOperationException
import com.google.firebase.dataconnect.DataConnectOperationFailureResponse as AndroidFailureResponse
import com.google.firebase.dataconnect.DataConnectPathSegment as AndroidPathSegment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlinx.coroutines.CancellationException
import zone.ien.firebase.dataconnect.DataConnectErrorPathSegment
import zone.ien.firebase.dataconnect.DataConnectFailureKind

class AndroidDataConnectErrorMappingTest {
    @Test
    fun operation_response의_partial_data와_error_path를_보존한다() {
        val response = TestFailureResponse(
            rawData = mapOf("movie" to "partial"),
            data = "partial",
            errors = listOf(
                TestErrorInfo(
                    message = "rating 오류",
                    path = listOf(AndroidPathSegment.Field("movie"), AndroidPathSegment.ListIndex(0))
                )
            )
        )
        val mapped = assertIs<zone.ien.firebase.dataconnect.DataConnectOperationException>(
            mapAndroidFailure(
                operationName = "GetMovie",
                throwable = AndroidOperationException("partial", response = response)
            )
        )

        assertEquals(DataConnectFailureKind.OPERATION, mapped.kind)
        assertEquals("partial", mapped.response?.data)
        assertEquals(DataConnectErrorPathSegment.ListIndex(0), mapped.response?.errors?.single()?.path?.last())
    }

    @Test
    fun cancellation은_래핑하지_않는다() {
        val cancellation = CancellationException("취소")

        assertSame(cancellation, mapAndroidFailure("GetMovie", cancellation))
    }

    private data class TestFailureResponse(
        override val rawData: Map<String, Any?>?,
        override val data: String?,
        override val errors: List<AndroidFailureResponse.ErrorInfo>
    ) : AndroidFailureResponse<String>

    private data class TestErrorInfo(
        override val message: String,
        override val path: List<AndroidPathSegment>
    ) : AndroidFailureResponse.ErrorInfo
}
