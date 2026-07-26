package zone.ien.firebase.firestore

class FieldValue private constructor(internal val operation: Operation) {
    internal sealed interface Operation {
        data object Delete : Operation
        data object ServerTimestamp : Operation
        data class ArrayUnion(val elements: List<Any>) : Operation
        data class ArrayRemove(val elements: List<Any>) : Operation
        data class IncrementLong(val value: Long) : Operation
        data class IncrementDouble(val value: Double) : Operation
    }

    companion object {
        fun delete(): FieldValue = FieldValue(Operation.Delete)

        fun serverTimestamp(): FieldValue = FieldValue(Operation.ServerTimestamp)

        fun arrayUnion(vararg elements: Any): FieldValue =
            FieldValue(Operation.ArrayUnion(elements.toList()))

        fun arrayRemove(vararg elements: Any): FieldValue =
            FieldValue(Operation.ArrayRemove(elements.toList()))

        fun increment(value: Long): FieldValue = FieldValue(Operation.IncrementLong(value))

        fun increment(value: Double): FieldValue = FieldValue(Operation.IncrementDouble(value))
    }
}
