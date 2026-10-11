package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFieldPath

@OptIn(ExperimentalForeignApi::class)
actual class FieldPath(private val iosFieldPath: FIRFieldPath) {
    actual internal fun nativePath(): Any = iosFieldPath

    actual companion object {
        actual fun of(vararg fieldNames: String): FieldPath {
            return FieldPath(FIRFieldPath(fields = fieldNames.toList()))
        }

        actual fun documentId(): FieldPath {
            return FieldPath(FIRFieldPath.documentID())
        }
    }
}
