package zone.ien.firebase.functions

import kotlin.test.Test
import kotlin.test.assertEquals

class FirebaseFunctionsCodeTest {
    @Test
    fun testFunctionsErrorCodeProvidesAllGrpcStatuses() {
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

    @Test
    fun effectiveCode_프로퍼티가_정상_참조된다() {
        val prop = FirebaseFunctionsException::effectiveCode
        assertEquals("effectiveCode", prop.name)
    }

    @Test
    fun Functions_확장_함수_및_오퍼레이터가_정상_참조된다() {
        val callableExtFn = FirebaseFunctions::httpsCallable
        val getOpFn = FirebaseFunctions::get
        val invokeOpFn = HttpsCallableReference::invoke
        val dataExtFn: HttpsCallableResult.() -> Any? = { data<Any?>() }

        assertEquals("httpsCallable", callableExtFn.name)
        assertEquals("get", getOpFn.name)
        assertEquals("invoke", invokeOpFn.name)
    }
}
