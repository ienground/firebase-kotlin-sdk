package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlin.jvm.JvmName

expect class DocumentReference {
    val id: String
    val path: String
    val parent: CollectionReference
    val firestore: FirebaseFirestore

    suspend fun set(data: Map<String, Any?>)
    suspend fun set(data: Map<String, Any?>, merge: Boolean)
    suspend fun set(data: Map<String, Any?>, mergeFields: List<String>)
    @JvmName("setMergeFieldPaths")
    suspend fun set(data: Map<String, Any?>, mergeFieldPaths: List<FieldPath>)
    suspend fun update(data: Map<String, Any?>)
    suspend fun update(field: String, value: Any?, vararg moreFieldsAndValues: Any?)
    suspend fun update(field: FieldPath, value: Any?, vararg moreFieldsAndValues: Any?)
    suspend fun delete()

    suspend fun get(): DocumentSnapshot
    suspend fun get(source: Source): DocumentSnapshot

    fun snapshots(): Flow<DocumentSnapshot?>
    fun snapshots(
        includeMetadataChanges: Boolean,
        source: ListenSource
    ): Flow<DocumentSnapshot?>
}

fun DocumentReference.getId(): String = id
fun DocumentReference.getPath(): String = path

fun DocumentReference.getSnapshots(cache: Boolean = true): Flow<DocumentSnapshot?> =
    snapshots(includeMetadataChanges = !cache, source = ListenSource.DEFAULT)
        .filter { doc -> doc == null || !doc.metadata.isFromCache || cache }


fun DocumentReference.snapshots(includeMetadataChanges: Boolean): Flow<DocumentSnapshot?> =
    snapshots(includeMetadataChanges = includeMetadataChanges, source = ListenSource.DEFAULT)

fun DocumentReference.snapshots(source: ListenSource): Flow<DocumentSnapshot?> =
    snapshots(includeMetadataChanges = false, source = source)
