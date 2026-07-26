package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.common.FIRTimestamp
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFieldValue

@OptIn(ExperimentalForeignApi::class)
internal fun Any.toIosValue(): Any = when (this) {
    is Timestamp -> FIRTimestamp(seconds = seconds, nanoseconds = nanoseconds)
    is FieldValue -> when (val operation = operation) {
        FieldValue.Operation.Delete -> FIRFieldValue.fieldValueForDelete()
        FieldValue.Operation.ServerTimestamp -> FIRFieldValue.fieldValueForServerTimestamp()
        is FieldValue.Operation.ArrayUnion -> FIRFieldValue.fieldValueForArrayUnion(
            operation.elements.map { it.toIosValue() }
        )
        is FieldValue.Operation.ArrayRemove -> FIRFieldValue.fieldValueForArrayRemove(
            operation.elements.map { it.toIosValue() }
        )
        is FieldValue.Operation.IncrementLong ->
            FIRFieldValue.fieldValueForIntegerIncrement(operation.value)
        is FieldValue.Operation.IncrementDouble ->
            FIRFieldValue.fieldValueForDoubleIncrement(operation.value)
    }
    is Map<*, *> -> entries.associate { (key, value) ->
        require(key is String) { "Firestore map keys must be strings." }
        key to value?.toIosValue()
    }
    is List<*> -> map { it?.toIosValue() }
    else -> this
}

@OptIn(ExperimentalForeignApi::class)
internal fun Map<String, Any>.toIosData(): Map<Any?, *> =
    mapValues { (_, value) -> value.toIosValue() }

@OptIn(ExperimentalForeignApi::class)
internal fun Any?.toCommonValue(): Any? = when (this) {
    is FIRTimestamp -> Timestamp(seconds, nanoseconds)
    is Map<*, *> -> entries.associate { (key, value) ->
        require(key is String) { "Firestore map keys must be strings." }
        key to value.toCommonValue()
    }
    is List<*> -> map { it.toCommonValue() }
    else -> this
}
