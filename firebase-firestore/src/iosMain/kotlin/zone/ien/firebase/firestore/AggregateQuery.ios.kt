package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSNumber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRAggregateField
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRAggregateQuery
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRAggregateQuerySnapshot
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRAggregateSource
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFieldPath

@OptIn(ExperimentalForeignApi::class)
actual class AggregateField(internal val iosField: FIRAggregateField) {
    actual companion object {
        actual fun count(): AggregateField = AggregateField(FIRAggregateField.aggregateFieldForCount())
        actual fun sum(field: String): AggregateField = AggregateField(FIRAggregateField.aggregateFieldForSumOfField(field))
        actual fun sum(field: FieldPath): AggregateField =
            AggregateField(FIRAggregateField.aggregateFieldForSumOfFieldPath(field.nativePath() as FIRFieldPath))
        actual fun average(field: String): AggregateField =
            AggregateField(FIRAggregateField.aggregateFieldForAverageOfField(field))
        actual fun average(field: FieldPath): AggregateField =
            AggregateField(FIRAggregateField.aggregateFieldForAverageOfFieldPath(field.nativePath() as FIRFieldPath))
    }
}

@OptIn(ExperimentalForeignApi::class)
actual class AggregateQuery(private val iosAggregateQuery: FIRAggregateQuery) {
    actual suspend fun get(source: AggregateSource): AggregateQuerySnapshot = suspendCancellableCoroutine { cont ->
        val iosSource = when (source) {
            AggregateSource.SERVER -> FIRAggregateSource.FIRAggregateSourceServer
        }
        iosAggregateQuery.aggregationWithSource(iosSource) { snapshot, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (snapshot != null) {
                cont.resume(AggregateQuerySnapshot(snapshot))
            } else {
                cont.resumeWithException(RuntimeException("AggregateQuerySnapshot was null"))
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
actual class AggregateQuerySnapshot(private val iosSnapshot: FIRAggregateQuerySnapshot) {
    actual val count: Long
        get() = iosSnapshot.count.longValue

    actual fun get(field: AggregateField): Any? = iosSnapshot.valueForAggregateField(field.iosField)
    actual fun getLong(field: AggregateField): Long? =
        (iosSnapshot.valueForAggregateField(field.iosField) as? NSNumber)?.longLongValue
    actual fun getDouble(field: AggregateField): Double? =
        (iosSnapshot.valueForAggregateField(field.iosField) as? NSNumber)?.doubleValue
}
