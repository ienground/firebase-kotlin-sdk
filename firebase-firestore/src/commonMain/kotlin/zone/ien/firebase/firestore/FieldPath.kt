package zone.ien.firebase.firestore

/**
 * A [FieldPath] refers to a field in a document. The path may consist of a single field name
 * (referring to a top-level field in the document), or a list of field names (referring to a
 * nested field in the document).
 */
expect class FieldPath {
    internal fun nativePath(): Any

    companion object {
        fun of(vararg fieldNames: String): FieldPath
        fun documentId(): FieldPath
    }
}

val FieldPath.Companion.documentId: FieldPath
    get() = documentId()
