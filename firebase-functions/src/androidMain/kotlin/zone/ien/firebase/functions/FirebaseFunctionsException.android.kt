package zone.ien.firebase.functions

import com.google.firebase.functions.FirebaseFunctionsException as AndroidFirebaseFunctionsException

actual class FirebaseFunctionsException(private val androidException: AndroidFirebaseFunctionsException) : Exception(androidException.message, androidException) {
    actual val code: Code
        get() = try {
            Code.valueOf(androidException.code.name)
        } catch (e: Exception) {
            Code.UNKNOWN
        }

    actual val details: Any?
        get() = androidException.details

    actual enum class Code {
        OK, CANCELLED, UNKNOWN, INVALID_ARGUMENT, DEADLINE_EXCEEDED,
        NOT_FOUND, ALREADY_EXISTS, PERMISSION_DENIED, RESOURCE_EXHAUSTED,
        FAILED_PRECONDITION, ABORTED, OUT_OF_RANGE, UNIMPLEMENTED,
        INTERNAL, UNAVAILABLE, DATA_LOSS, UNAUTHENTICATED
    }
}
