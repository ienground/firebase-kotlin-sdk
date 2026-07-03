package zone.ien.firebase.installations

import zone.ien.firebase.FirebaseApp

public expect class FirebaseInstallations {
    public suspend fun getId(): String
    public suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult
    
    /**
     * Deletes this Firebase Installation and all associated data from the client and the Firebase backend.
     * Note: This is a high side-effect GDPR / Instance reset operation. Calling this will invalidate
     * all authentication tokens and push tokens associated with this installation instance.
     */
    public suspend fun delete()

    public companion object {
        public val instance: FirebaseInstallations
        public fun getInstance(app: FirebaseApp): FirebaseInstallations
    }
}
