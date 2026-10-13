package zone.ien.firebase.firestore

expect class FirebaseFirestoreSettings {
    val host: String
    val isSslEnabled: Boolean
    val isPersistenceEnabled: Boolean
}

class FirebaseFirestoreSettingsBuilder {
    var host: String = "firestore.googleapis.com"
    var isSslEnabled: Boolean = true
    var isPersistenceEnabled: Boolean = true

    fun build(): FirebaseFirestoreSettings = createSettings(host, isSslEnabled, isPersistenceEnabled)
}

internal expect fun createSettings(host: String, isSslEnabled: Boolean, isPersistenceEnabled: Boolean): FirebaseFirestoreSettings

fun firestoreSettings(builder: FirebaseFirestoreSettingsBuilder.() -> Unit): FirebaseFirestoreSettings {
    return FirebaseFirestoreSettingsBuilder().apply(builder).build()
}
