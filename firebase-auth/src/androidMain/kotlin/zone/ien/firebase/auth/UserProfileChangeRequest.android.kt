package zone.ien.firebase.auth

import android.net.Uri
import com.google.firebase.auth.UserProfileChangeRequest as AndroidUserProfileChangeRequest

public actual class UserProfileChangeRequest internal constructor(
    internal val androidRequest: AndroidUserProfileChangeRequest
) {
    public actual val displayName: String?
        get() = androidRequest.displayName

    public actual val photoUrl: String?
        get() = androidRequest.photoUri?.toString()

    public actual class Builder {
        private val builder = AndroidUserProfileChangeRequest.Builder()

        public actual fun setDisplayName(displayName: String?): Builder {
            builder.setDisplayName(displayName)
            return this
        }

        public actual fun setPhotoUri(photoUri: String?): Builder {
            builder.setPhotoUri(photoUri?.let { Uri.parse(it) })
            return this
        }

        public actual fun build(): UserProfileChangeRequest {
            return UserProfileChangeRequest(builder.build())
        }
    }
}
