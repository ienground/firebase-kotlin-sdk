package zone.ien.firebase.database

import com.google.firebase.database.DatabaseReference as AndroidDatabaseReference

public actual class DatabaseReference(private val androidReference: AndroidDatabaseReference) : Query(androidReference) {

    public actual val key: String?
        get() = androidReference.key

    public actual fun child(pathString: String): DatabaseReference {
        return DatabaseReference(androidReference.child(pathString))
    }

    public actual fun push(): DatabaseReference {
        return DatabaseReference(androidReference.push())
    }

    public actual suspend fun setValue(value: Any?) {
        androidReference.setValue(value).await()
    }

    public actual suspend fun removeValue() {
        androidReference.removeValue().await()
    }

    public actual suspend fun updateChildren(update: Map<String, Any?>) {
        androidReference.updateChildren(update).await()
    }
}
