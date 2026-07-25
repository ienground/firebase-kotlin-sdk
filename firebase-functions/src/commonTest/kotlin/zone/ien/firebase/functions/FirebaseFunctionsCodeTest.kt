package zone.ien.firebase.functions

import kotlin.test.Test
import kotlin.test.assertEquals

class FirebaseFunctionsCodeTest {
    @Test
    fun 함수_오류_코드는_gRPC_상태를_모두_제공한다() {
        assertEquals(
            listOf(
                "OK",
                "CANCELLED",
                "UNKNOWN",
                "INVALID_ARGUMENT",
                "DEADLINE_EXCEEDED",
                "NOT_FOUND",
                "ALREADY_EXISTS",
                "PERMISSION_DENIED",
                "RESOURCE_EXHAUSTED",
                "FAILED_PRECONDITION",
                "ABORTED",
                "OUT_OF_RANGE",
                "UNIMPLEMENTED",
                "INTERNAL",
                "UNAVAILABLE",
                "DATA_LOSS",
                "UNAUTHENTICATED"
            ),
            FirebaseFunctionsException.Code.entries.map { it.name }
        )
    }
}
