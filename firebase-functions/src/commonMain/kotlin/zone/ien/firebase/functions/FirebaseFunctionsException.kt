package zone.ien.firebase.functions

expect class FirebaseFunctionsException : Exception {
    val code: Code
    val details: Any?

    enum class Code {
        OK, CANCELLED, UNKNOWN, INVALID_ARGUMENT, DEADLINE_EXCEEDED,
        NOT_FOUND, ALREADY_EXISTS, PERMISSION_DENIED, RESOURCE_EXHAUSTED,
        FAILED_PRECONDITION, ABORTED, OUT_OF_RANGE, UNIMPLEMENTED,
        INTERNAL, UNAVAILABLE, DATA_LOSS, UNAUTHENTICATED
    }
}
