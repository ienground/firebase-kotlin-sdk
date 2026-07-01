package zone.ien.firebase.database

import com.google.firebase.database.DataSnapshot as AndroidDataSnapshot

public actual class DataSnapshot(private val androidSnapshot: AndroidDataSnapshot) {

    public actual val key: String?
        get() = androidSnapshot.key

    public actual val value: Any?
        get() = androidSnapshot.value

    public actual fun child(path: String): DataSnapshot {
        return DataSnapshot(androidSnapshot.child(path))
    }

    public actual fun exists(): Boolean {
        return androidSnapshot.exists()
    }

    public actual fun hasChild(path: String): Boolean {
        return androidSnapshot.hasChild(path)
    }
}
