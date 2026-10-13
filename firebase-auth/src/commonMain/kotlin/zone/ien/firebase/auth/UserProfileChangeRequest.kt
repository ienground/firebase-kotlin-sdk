package zone.ien.firebase.auth

public expect class UserProfileChangeRequest {
    public val displayName: String?
    public val photoUrl: String?

    public class Builder {
        public constructor()
        public fun setDisplayName(displayName: String?): Builder
        public fun setPhotoUri(photoUri: String?): Builder
        public fun build(): UserProfileChangeRequest
    }
}
