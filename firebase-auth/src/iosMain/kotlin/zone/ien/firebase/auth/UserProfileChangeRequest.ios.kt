package zone.ien.firebase.auth

public actual class UserProfileChangeRequest internal constructor(
    public actual val displayName: String?,
    public actual val photoUrl: String?
) {
    public actual class Builder {
        private var displayName: String? = null
        private var photoUrl: String? = null

        public actual fun setDisplayName(displayName: String?): Builder {
            this.displayName = displayName
            return this
        }

        public actual fun setPhotoUri(photoUri: String?): Builder {
            this.photoUrl = photoUri
            return this
        }

        public actual fun build(): UserProfileChangeRequest {
            return UserProfileChangeRequest(displayName, photoUrl)
        }
    }
}
