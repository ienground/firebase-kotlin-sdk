package zone.ien.firebase.auth

import android.net.Uri
import com.google.firebase.auth.UserProfileChangeRequest as AndroidUserProfileChangeRequest

public actual class UserProfileChangeRequest internal constructor(
    internal val androidRequest: AndroidUserProfileChangeRequest?,
    private val fallbackDisplayName: String? = null,
    private val fallbackPhotoUrl: String? = null
) {
    internal constructor(androidRequest: AndroidUserProfileChangeRequest) : this(
        androidRequest = androidRequest,
        fallbackDisplayName = null,
        fallbackPhotoUrl = null
    )

    public actual val displayName: String?
        get() = androidRequest?.displayName ?: fallbackDisplayName

    public actual val photoUrl: String?
        get() = androidRequest?.photoUri?.toString() ?: fallbackPhotoUrl

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
            val androidReq = try {
                AndroidUserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(photoUrl?.let { Uri.parse(it) })
                    .build()
            } catch (_: Throwable) {
                null
            }
            return if (androidReq != null) {
                UserProfileChangeRequest(androidReq)
            } else {
                UserProfileChangeRequest(null, displayName, photoUrl)
            }
        }
    }
}
