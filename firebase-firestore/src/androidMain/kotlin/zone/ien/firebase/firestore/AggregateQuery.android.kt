package zone.ien.firebase.firestore

import com.google.firebase.firestore.AggregateField as AndroidAggregateField
import com.google.firebase.firestore.AggregateQuery as AndroidAggregateQuery
import com.google.firebase.firestore.AggregateQuerySnapshot as AndroidAggregateQuerySnapshot
import com.google.firebase.firestore.AggregateSource as AndroidAggregateSource
import com.google.firebase.firestore.FieldPath as AndroidFieldPath

actual class AggregateField(internal val androidField: AndroidAggregateField) {
    actual companion object {
        actual fun count(): AggregateField = AggregateField(AndroidAggregateField.count())
        actual fun sum(field: String): AggregateField = AggregateField(AndroidAggregateField.sum(field))
        actual fun sum(field: FieldPath): AggregateField =
            AggregateField(AndroidAggregateField.sum(field.nativePath() as AndroidFieldPath))
        actual fun average(field: String): AggregateField = AggregateField(AndroidAggregateField.average(field))
        actual fun average(field: FieldPath): AggregateField =
            AggregateField(AndroidAggregateField.average(field.nativePath() as AndroidFieldPath))
    }
}

actual class AggregateQuery(private val androidAggregateQuery: AndroidAggregateQuery) {
    actual suspend fun get(source: AggregateSource): AggregateQuerySnapshot {
        val androidSource = when (source) {
            AggregateSource.SERVER -> AndroidAggregateSource.SERVER
        }
        val snapshot = androidAggregateQuery.get(androidSource).await()
        return AggregateQuerySnapshot(snapshot)
    }
}

actual class AggregateQuerySnapshot(private val androidSnapshot: AndroidAggregateQuerySnapshot) {
    actual val count: Long
        get() = androidSnapshot.count

    actual fun get(field: AggregateField): Any? = androidSnapshot.get(field.androidField)
    actual fun getLong(field: AggregateField): Long? = androidSnapshot.get(field.androidField) as? Long
    actual fun getDouble(field: AggregateField): Double? = (androidSnapshot.get(field.androidField) as? Number)?.toDouble()
}
