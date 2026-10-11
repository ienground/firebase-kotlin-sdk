package zone.ien.firebase.firestore

import com.google.firebase.firestore.DocumentReference as AndroidDocumentReference
import com.google.firebase.firestore.FieldPath as AndroidFieldPath
import com.google.firebase.firestore.SetOptions as AndroidSetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.jvm.JvmName

actual class DocumentReference(internal val androidDocument: AndroidDocumentReference) {
    actual val id: String
        get() = androidDocument.id
    actual val path: String
        get() = androidDocument.path
    actual val parent: CollectionReference
        get() = CollectionReference(androidDocument.parent)
    actual val firestore: FirebaseFirestore
        get() = FirebaseFirestore(androidDocument.firestore)

    actual val snapshots: Flow<DocumentSnapshot>
        get() = snapshots()

    actual override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DocumentReference) return false
        return androidDocument == other.androidDocument
    }

    actual override fun hashCode(): Int {
        return androidDocument.hashCode()
    }

    actual suspend fun set(data: Map<String, Any?>) {
        androidDocument.set(data.toAndroidData()).await()
    }

    actual suspend fun set(data: Map<String, Any?>, merge: Boolean) {
        if (merge) {
            androidDocument.set(data.toAndroidData(), AndroidSetOptions.merge()).await()
        } else {
            androidDocument.set(data.toAndroidData()).await()
        }
    }

    actual suspend fun set(data: Map<String, Any?>, mergeFields: List<String>) {
        androidDocument.set(data.toAndroidData(), AndroidSetOptions.mergeFields(mergeFields)).await()
    }

    @JvmName("setMergeFieldPaths")
    actual suspend fun set(data: Map<String, Any?>, mergeFieldPaths: List<FieldPath>) {
        val paths = mergeFieldPaths.map { it.nativePath() as AndroidFieldPath }
        androidDocument.set(data.toAndroidData(), AndroidSetOptions.mergeFieldPaths(paths)).await()
    }

    actual suspend fun update(data: Map<String, Any?>) {
        androidDocument.update(data.toAndroidData()).await()
    }

    actual suspend fun update(field: String, value: Any?, vararg moreFieldsAndValues: Any?) {
        val data = buildUpdateData(field, value, *moreFieldsAndValues)
        androidDocument.update(data).await()
    }

    actual suspend fun update(field: FieldPath, value: Any?, vararg moreFieldsAndValues: Any?) {
        val data = buildUpdateData(field.nativePath().toString(), value, *moreFieldsAndValues)
        androidDocument.update(data).await()
    }

    actual suspend fun delete() {
        androidDocument.delete().await()
    }

    actual fun collection(collectionPath: String): CollectionReference {
        return CollectionReference(androidDocument.collection(collectionPath))
    }

    actual suspend fun get(): DocumentSnapshot = get(Source.DEFAULT)

    actual suspend fun get(source: Source): DocumentSnapshot {
        return DocumentSnapshot(androidDocument.get(source.toAndroidSource()).await())
    }

    actual fun snapshots(): Flow<DocumentSnapshot> = snapshots(
        includeMetadataChanges = false,
        source = ListenSource.DEFAULT
    )

    actual fun snapshots(
        includeMetadataChanges: Boolean,
        source: ListenSource
    ): Flow<DocumentSnapshot> = callbackFlow {
        val options = snapshotListenOptions(includeMetadataChanges, source)
        val listener = androidDocument.addSnapshotListener(options) { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                trySend(DocumentSnapshot(snapshot))
            }
        }
        awaitClose {
            listener.remove()
        }
    }
}

internal fun buildUpdateData(field: String, value: Any?, vararg moreFieldsAndValues: Any?): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    map[field] = value.toAndroidValue()
    var i = 0
    while (i < moreFieldsAndValues.size) {
        val f = moreFieldsAndValues[i].toString()
        val v = if (i + 1 < moreFieldsAndValues.size) moreFieldsAndValues[i + 1] else null
        map[f] = v.toAndroidValue()
        i += 2
    }
    return map
}
