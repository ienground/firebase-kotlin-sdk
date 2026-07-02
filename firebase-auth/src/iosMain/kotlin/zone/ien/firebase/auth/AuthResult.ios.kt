package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRAuthDataResult

@OptIn(ExperimentalForeignApi::class)
public actual class AuthResult private actual constructor() {
    private var iosResult: FIRAuthDataResult? = null

    public constructor(iosResult: FIRAuthDataResult?) : this() {
        this.iosResult = iosResult
    }

    public actual val user: FirebaseUser?
        get() = iosResult?.user?.let { FirebaseUser(it) }
}
