package zone.ien.firebase.database

import zone.ien.firebase.FirebaseApp
import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.database.FIRDatabase

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseDatabase private constructor(private val iosDatabase: FIRDatabase) {

    public actual fun reference(): DatabaseReference {
        return DatabaseReference(iosDatabase.reference())
    }

    public actual fun reference(path: String): DatabaseReference {
        return DatabaseReference(iosDatabase.referenceWithPath(path))
    }

    public actual fun useEmulator(host: String, port: Int) {
        iosDatabase.useEmulatorWithHost(host, port.toLong())
    }

    public actual fun setPersistenceEnabled(enabled: Boolean) {
        iosDatabase.persistenceEnabled = enabled
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseDatabase {
            return FirebaseDatabase(FIRDatabase.database())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseDatabase {
            val iosApp = app.iosApp
            return FirebaseDatabase(FIRDatabase.databaseForApp(iosApp as objcnames.classes.FIRApp))
        }

        public actual fun getInstance(url: String): FirebaseDatabase {
            return FirebaseDatabase(FIRDatabase.databaseWithURL(url))
        }
    }
}
