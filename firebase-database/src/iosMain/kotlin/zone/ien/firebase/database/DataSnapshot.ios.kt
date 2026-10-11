package zone.ien.firebase.database

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.database.FIRDataSnapshot

@OptIn(ExperimentalForeignApi::class)
public actual class DataSnapshot(private val iosSnapshot: FIRDataSnapshot) {

    public actual val key: String?
        get() = iosSnapshot.key()

    public actual val value: Any?
        get() = iosSnapshot.value()

    public actual fun child(path: String): DataSnapshot {
        return DataSnapshot(iosSnapshot.childSnapshotForPath(path))
    }

    public actual fun exists(): Boolean {
        return iosSnapshot.exists()
    }

    public actual fun hasChild(path: String): Boolean {
        return iosSnapshot.hasChild(path)
    }
}
