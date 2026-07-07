package zone.ien.firebase.database

import zone.ien.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase as AndroidFirebaseDatabase

public actual class FirebaseDatabase(private val androidDatabase: AndroidFirebaseDatabase) {

    public actual fun reference(): DatabaseReference {
        return DatabaseReference(androidDatabase.reference)
    }

    public actual fun reference(path: String): DatabaseReference {
        return DatabaseReference(androidDatabase.getReference(path))
    }

    public actual fun useEmulator(host: String, port: Int) {
        androidDatabase.useEmulator(host, port)
    }

    public actual fun setPersistenceEnabled(enabled: Boolean) {
        androidDatabase.setPersistenceEnabled(enabled)
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseDatabase {
            return FirebaseDatabase(AndroidFirebaseDatabase.getInstance())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseDatabase {
            val androidApp = app.androidApp
            return FirebaseDatabase(AndroidFirebaseDatabase.getInstance(androidApp))
        }

        public actual fun getInstance(url: String): FirebaseDatabase {
            return FirebaseDatabase(AndroidFirebaseDatabase.getInstance(url))
        }
    }
}
