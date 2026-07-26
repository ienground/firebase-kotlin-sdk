package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentSnapshot

@OptIn(ExperimentalForeignApi::class)
actual class DocumentSnapshot(private val iosSnapshot: FIRDocumentSnapshot) {
    actual fun getId(): String = iosSnapshot.documentID
    actual fun getExists(): Boolean = iosSnapshot.exists
    @Suppress("UNCHECKED_CAST")
    actual fun getData(): Map<String, Any>? =
        iosSnapshot.data()?.toCommonValue() as? Map<String, Any>
    actual fun get(field: String): Any? = iosSnapshot.valueForField(field).toCommonValue()
    actual fun get(field: FieldPath): Any? =
        iosSnapshot.valueForField(field.nativePath()).toCommonValue()
    actual fun getMetadata(): SnapshotMetadata = iosSnapshot.metadata.toCommonMetadata()
    actual internal fun nativeSnapshot(): Any = iosSnapshot
}
