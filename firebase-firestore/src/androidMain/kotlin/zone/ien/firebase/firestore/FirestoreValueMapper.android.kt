package zone.ien.firebase.firestore

import com.google.firebase.Timestamp as AndroidTimestamp
import com.google.firebase.firestore.FieldValue as AndroidFieldValue

internal fun Any.toAndroidValue(): Any = when (this) {
    is Timestamp -> AndroidTimestamp(seconds, nanoseconds)
    is FieldValue -> when (val operation = operation) {
        FieldValue.Operation.Delete -> AndroidFieldValue.delete()
        FieldValue.Operation.ServerTimestamp -> AndroidFieldValue.serverTimestamp()
        is FieldValue.Operation.ArrayUnion -> AndroidFieldValue.arrayUnion(
            *operation.elements.map { it.toAndroidValue() }.toTypedArray()
        )
        is FieldValue.Operation.ArrayRemove -> AndroidFieldValue.arrayRemove(
            *operation.elements.map { it.toAndroidValue() }.toTypedArray()
        )
        is FieldValue.Operation.IncrementLong -> AndroidFieldValue.increment(operation.value)
        is FieldValue.Operation.IncrementDouble -> AndroidFieldValue.increment(operation.value)
    }
    is Map<*, *> -> entries.associate { (key, value) ->
        require(key is String) { "Firestore map keys must be strings." }
        key to value?.toAndroidValue()
    }
    is List<*> -> map { it?.toAndroidValue() }
    else -> this
}

internal fun Map<String, Any>.toAndroidData(): Map<String, Any> =
    mapValues { (_, value) -> value.toAndroidValue() }

internal fun Any?.toCommonValue(): Any? = when (this) {
    is AndroidTimestamp -> Timestamp(seconds, nanoseconds)
    is Map<*, *> -> entries.associate { (key, value) ->
        require(key is String) { "Firestore map keys must be strings." }
        key to value.toCommonValue()
    }
    is List<*> -> map { it.toCommonValue() }
    else -> this
}
