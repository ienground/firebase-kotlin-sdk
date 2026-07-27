package zone.ien.firebase.firestore

import kotlin.jvm.JvmName

expect class DocumentSnapshot {
    @get:JvmName("id")
    val id: String
    @get:JvmName("exists")
    val exists: Boolean
    @get:JvmName("metadata")
    val metadata: SnapshotMetadata
    val reference: DocumentReference
    fun getId(): String
    fun getExists(): Boolean
    fun getData(): Map<String, Any?>?
    fun <T> get(field: String): T
    fun <T> get(field: FieldPath): T
    fun getMetadata(): SnapshotMetadata
    internal fun nativeSnapshot(): Any
}

val DocumentSnapshot.metadata: SnapshotMetadata
    get() = metadata

val DocumentSnapshot.id: String
    get() = id

val DocumentSnapshot.exists: Boolean
    get() = exists

@Suppress("UNCHECKED_CAST")
inline fun <reified T> castValue(value: Any?): T {
    if (value == null) return null as T
    if (value is T) return value

    if (value is Number) {
        val converted: Any = when (T::class) {
            Int::class -> value.toInt()
            Long::class -> value.toLong()
            Double::class -> value.toDouble()
            Float::class -> value.toFloat()
            Short::class -> value.toShort()
            Byte::class -> value.toByte()
            else -> value
        }
        return converted as T
    }

    return value as T
}

inline fun <reified T> DocumentSnapshot.get(field: String): T =
    castValue(get<Any?>(field))

inline fun <reified T> DocumentSnapshot.get(field: FieldPath): T =
    castValue(get<Any?>(field))
