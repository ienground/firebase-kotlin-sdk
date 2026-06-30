package zone.ien.firebase.functions

import platform.Foundation.NSError

actual class FirebaseFunctionsException(private val iosError: NSError) : Exception(iosError.localizedDescription) {
    actual val code: Code
        get() {
            // FIRFunctionsErrorCode mappings
            return when (iosError.code) {
                0L -> Code.OK
                1L -> Code.CANCELLED
                2L -> Code.UNKNOWN
                3L -> Code.INVALID_ARGUMENT
                4L -> Code.DEADLINE_EXCEEDED
                5L -> Code.NOT_FOUND
                6L -> Code.ALREADY_EXISTS
                7L -> Code.PERMISSION_DENIED
                8L -> Code.RESOURCE_EXHAUSTED
                9L -> Code.FAILED_PRECONDITION
                10L -> Code.ABORTED
                11L -> Code.OUT_OF_RANGE
                12L -> Code.UNIMPLEMENTED
                13L -> Code.INTERNAL
                14L -> Code.UNAVAILABLE
                15L -> Code.DATA_LOSS
                16L -> Code.UNAUTHENTICATED
                else -> Code.UNKNOWN
            }
        }

    actual val details: Any?
        get() = iosError.userInfo

    actual enum class Code {
        OK, CANCELLED, UNKNOWN, INVALID_ARGUMENT, DEADLINE_EXCEEDED,
        NOT_FOUND, ALREADY_EXISTS, PERMISSION_DENIED, RESOURCE_EXHAUSTED,
        FAILED_PRECONDITION, ABORTED, OUT_OF_RANGE, UNIMPLEMENTED,
        INTERNAL, UNAVAILABLE, DATA_LOSS, UNAUTHENTICATED
    }
}
