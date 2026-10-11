package zone.ien.firebase.firestore

import com.google.firebase.firestore.FieldPath as AndroidFieldPath

actual class FieldPath(private val androidFieldPath: AndroidFieldPath) {
    actual internal fun nativePath(): Any = androidFieldPath

    actual companion object {
        actual fun of(vararg fieldNames: String): FieldPath {
            return FieldPath(AndroidFieldPath.of(*fieldNames))
        }

        actual fun documentId(): FieldPath {
            return FieldPath(AndroidFieldPath.documentId())
        }
    }
}
