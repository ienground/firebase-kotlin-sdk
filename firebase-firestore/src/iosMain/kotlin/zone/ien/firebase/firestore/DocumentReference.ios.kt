package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentReference
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFieldPath

@OptIn(ExperimentalForeignApi::class)
actual class DocumentReference(internal val iosDocument: FIRDocumentReference) {
    actual val id: String
        get() = iosDocument.documentID
    actual val path: String
        get() = iosDocument.path
    actual val parent: CollectionReference
        get() = CollectionReference(iosDocument.parent)
    actual val firestore: FirebaseFirestore
        get() = FirebaseFirestore(iosDocument.firestore)

    actual suspend fun set(data: Map<String, Any?>) = suspendCancellableCoroutine<Unit> { cont ->
        iosDocument.setData(data.toIosData()) { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun set(data: Map<String, Any?>, merge: Boolean) = suspendCancellableCoroutine<Unit> { cont ->
        iosDocument.setData(data.toIosData(), merge) { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun set(data: Map<String, Any?>, mergeFields: List<String>) = suspendCancellableCoroutine<Unit> { cont ->
        iosDocument.setData(data.toIosData(), mergeFields = mergeFields) { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun set(data: Map<String, Any?>, mergeFieldPaths: List<FieldPath>) = suspendCancellableCoroutine<Unit> { cont ->
        val paths = mergeFieldPaths.map { it.nativePath() as FIRFieldPath }
        iosDocument.setData(data.toIosData(), mergeFields = paths) { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun update(data: Map<String, Any?>) = suspendCancellableCoroutine<Unit> { cont ->
        iosDocument.updateData(data.toIosData()) { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun update(field: String, value: Any?, vararg moreFieldsAndValues: Any?) {
        val data = buildUpdateData(field, value, *moreFieldsAndValues)
        update(data)
    }

    actual suspend fun update(field: FieldPath, value: Any?, vararg moreFieldsAndValues: Any?) {
        val data = buildUpdateData(field.nativePath().toString(), value, *moreFieldsAndValues)
        update(data)
    }

    actual suspend fun delete() = suspendCancellableCoroutine<Unit> { cont ->
        iosDocument.deleteDocumentWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun get(): DocumentSnapshot = get(Source.DEFAULT)

    actual suspend fun get(source: Source): DocumentSnapshot = suspendCancellableCoroutine { cont ->
        iosDocument.getDocumentWithSource(source.toIosSource()) { snapshot, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (snapshot != null) {
                cont.resume(DocumentSnapshot(snapshot))
            } else {
                cont.resumeWithException(RuntimeException("Snapshot was null"))
            }
        }
    }

    actual fun snapshots(): Flow<DocumentSnapshot?> = snapshots(
        includeMetadataChanges = false,
        source = ListenSource.DEFAULT
    )

    actual fun snapshots(
        includeMetadataChanges: Boolean,
        source: ListenSource
    ): Flow<DocumentSnapshot?> = callbackFlow {
        val options = snapshotListenOptions(includeMetadataChanges, source)
        val handle = iosDocument.addSnapshotListenerWithOptions(options) { snapshot, error ->
            if (error != null) {
                close(RuntimeException(error.localizedDescription))
                return@addSnapshotListenerWithOptions
            }
            trySend(snapshot?.let { DocumentSnapshot(it) })
        }
        awaitClose {
            handle.remove()
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
internal fun buildUpdateData(field: String, value: Any?, vararg moreFieldsAndValues: Any?): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    map[field] = value
    var i = 0
    while (i < moreFieldsAndValues.size) {
        val f = moreFieldsAndValues[i].toString()
        val v = if (i + 1 < moreFieldsAndValues.size) moreFieldsAndValues[i + 1] else null
        map[f] = v
        i += 2
    }
    return map
}
