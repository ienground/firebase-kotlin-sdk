package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRAggregateField
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentSnapshot
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFieldPath
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRQuery

@OptIn(ExperimentalForeignApi::class)
actual open class Query(private val iosQuery: FIRQuery) {
    actual val firestore: FirebaseFirestore
        get() = FirebaseFirestore(iosQuery.firestore)

    actual suspend fun get(): QuerySnapshot = get(Source.DEFAULT)

    actual suspend fun get(source: Source): QuerySnapshot = suspendCancellableCoroutine { cont ->
        iosQuery.getDocumentsWithSource(source.toIosSource()) { snapshot, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (snapshot != null) {
                cont.resume(QuerySnapshot(snapshot))
            } else {
                cont.resumeWithException(RuntimeException("Snapshot was null"))
            }
        }
    }

    actual fun snapshots(): Flow<QuerySnapshot> = snapshots(
        includeMetadataChanges = false,
        source = ListenSource.DEFAULT
    )

    actual fun snapshots(
        includeMetadataChanges: Boolean,
        source: ListenSource
    ): Flow<QuerySnapshot> = callbackFlow {
        val options = snapshotListenOptions(includeMetadataChanges, source)
        val handle = iosQuery.addSnapshotListenerWithOptions(options) { snapshot, error ->
            if (error != null) {
                close(RuntimeException(error.localizedDescription))
                return@addSnapshotListenerWithOptions
            }
            if (snapshot != null) {
                trySend(QuerySnapshot(snapshot))
            }
        }
        awaitClose {
            handle.remove()
        }
    }

    actual fun where(field: String, operator: WhereOperator, value: Any?): Query {
        val filteredQuery = when (operator) {
            WhereOperator.EQUAL -> iosQuery.queryWhereField(field, isEqualTo = value.toIosValue())
            WhereOperator.NOT_EQUAL ->
                iosQuery.queryWhereField(field, isNotEqualTo = value.toIosValue())
            WhereOperator.LESS_THAN ->
                iosQuery.queryWhereField(field, isLessThan = requireNotNull(value).toIosValue())
            WhereOperator.LESS_THAN_OR_EQUAL ->
                iosQuery.queryWhereField(field, isLessThanOrEqualTo = requireNotNull(value).toIosValue())
            WhereOperator.GREATER_THAN ->
                iosQuery.queryWhereField(field, isGreaterThan = requireNotNull(value).toIosValue())
            WhereOperator.GREATER_THAN_OR_EQUAL ->
                iosQuery.queryWhereField(field, isGreaterThanOrEqualTo = requireNotNull(value).toIosValue())
            WhereOperator.ARRAY_CONTAINS ->
                iosQuery.queryWhereField(field, arrayContains = requireNotNull(value).toIosValue())
            WhereOperator.ARRAY_CONTAINS_ANY ->
                iosQuery.queryWhereField(field, arrayContainsAny = requireNotNull(value).asNativeList())
            WhereOperator.IN -> iosQuery.queryWhereField(field, `in` = requireNotNull(value).asNativeList())
            WhereOperator.NOT_IN -> iosQuery.queryWhereField(field, notIn = requireNotNull(value).asNativeList())
        }
        return Query(filteredQuery)
    }

    actual fun orderBy(field: String, direction: QueryDirection): Query {
        return Query(
            iosQuery.queryOrderedByField(
                field = field,
                descending = direction == QueryDirection.DESCENDING
            )
        )
    }

    actual fun where(field: FieldPath, operator: WhereOperator, value: Any?): Query {
        val fp = field.nativePath() as FIRFieldPath
        val filteredQuery = when (operator) {
            WhereOperator.EQUAL -> iosQuery.queryWhereFieldPath(fp, isEqualTo = value.toIosValue())
            WhereOperator.NOT_EQUAL ->
                iosQuery.queryWhereFieldPath(fp, isNotEqualTo = value.toIosValue())
            WhereOperator.LESS_THAN ->
                iosQuery.queryWhereFieldPath(fp, isLessThan = requireNotNull(value).toIosValue())
            WhereOperator.LESS_THAN_OR_EQUAL ->
                iosQuery.queryWhereFieldPath(fp, isLessThanOrEqualTo = requireNotNull(value).toIosValue())
            WhereOperator.GREATER_THAN ->
                iosQuery.queryWhereFieldPath(fp, isGreaterThan = requireNotNull(value).toIosValue())
            WhereOperator.GREATER_THAN_OR_EQUAL ->
                iosQuery.queryWhereFieldPath(fp, isGreaterThanOrEqualTo = requireNotNull(value).toIosValue())
            WhereOperator.ARRAY_CONTAINS ->
                iosQuery.queryWhereFieldPath(fp, arrayContains = requireNotNull(value).toIosValue())
            WhereOperator.ARRAY_CONTAINS_ANY ->
                iosQuery.queryWhereFieldPath(fp, arrayContainsAny = requireNotNull(value).asNativeList())
            WhereOperator.IN -> iosQuery.queryWhereFieldPath(fp, `in` = requireNotNull(value).asNativeList())
            WhereOperator.NOT_IN -> iosQuery.queryWhereFieldPath(fp, notIn = requireNotNull(value).asNativeList())
        }
        return Query(filteredQuery)
    }

    actual fun orderBy(field: FieldPath, direction: QueryDirection): Query {
        val fp = field.nativePath() as FIRFieldPath
        return Query(
            iosQuery.queryOrderedByFieldPath(
                path = fp,
                descending = direction == QueryDirection.DESCENDING
            )
        )
    }

    actual fun limit(limit: Long): Query = Query(iosQuery.queryLimitedTo(limit.toLong()))
    actual fun limitToLast(limit: Long): Query = Query(iosQuery.queryLimitedToLast(limit.toLong()))
    actual fun startAt(document: DocumentSnapshot): Query =
        Query(iosQuery.queryStartingAtDocument(document.nativeSnapshot() as FIRDocumentSnapshot))
    actual fun startAt(vararg fieldValues: Any?): Query {
        val list = fieldValues.map { it.toIosValue() }
        return Query(iosQuery.queryStartingAtValues(list))
    }
    actual fun startAfter(document: DocumentSnapshot): Query =
        Query(iosQuery.queryStartingAfterDocument(document.nativeSnapshot() as FIRDocumentSnapshot))
    actual fun startAfter(vararg fieldValues: Any?): Query {
        val list = fieldValues.map { it.toIosValue() }
        return Query(iosQuery.queryStartingAfterValues(list))
    }
    actual fun endAt(document: DocumentSnapshot): Query =
        Query(iosQuery.queryEndingAtDocument(document.nativeSnapshot() as FIRDocumentSnapshot))
    actual fun endAt(vararg fieldValues: Any?): Query {
        val list = fieldValues.map { it.toIosValue() }
        return Query(iosQuery.queryEndingAtValues(list))
    }
    actual fun endBefore(document: DocumentSnapshot): Query =
        Query(iosQuery.queryEndingBeforeDocument(document.nativeSnapshot() as FIRDocumentSnapshot))
    actual fun endBefore(vararg fieldValues: Any?): Query {
        val list = fieldValues.map { it.toIosValue() }
        return Query(iosQuery.queryEndingBeforeValues(list))
    }
    actual fun count(): AggregateQuery = AggregateQuery(iosQuery.count())
    actual fun sum(field: String): AggregateQuery =
        AggregateQuery(iosQuery.aggregate(listOf(FIRAggregateField.aggregateFieldForSumOfField(field))))
    actual fun sum(field: FieldPath): AggregateQuery =
        AggregateQuery(iosQuery.aggregate(listOf(FIRAggregateField.aggregateFieldForSumOfFieldPath(field.nativePath() as FIRFieldPath))))
    actual fun average(field: String): AggregateQuery =
        AggregateQuery(iosQuery.aggregate(listOf(FIRAggregateField.aggregateFieldForAverageOfField(field))))
    actual fun average(field: FieldPath): AggregateQuery =
        AggregateQuery(iosQuery.aggregate(listOf(FIRAggregateField.aggregateFieldForAverageOfFieldPath(field.nativePath() as FIRFieldPath))))
    actual fun aggregate(field: AggregateField, vararg fields: AggregateField): AggregateQuery {
        val list = buildList {
            add(field.iosField)
            fields.forEach { add(it.iosField) }
        }
        return AggregateQuery(iosQuery.aggregate(list))
    }

    private fun Any.asNativeList(): List<Any> =
        (this as? List<*>)
            ?.map {
                requireNotNull(it) {
                    "Firestore query values must not contain null."
                }.toIosValue()
            }
            ?: throw IllegalArgumentException("Firestore query operator requires a List value.")
}
