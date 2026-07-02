package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRAuthCredential

@OptIn(ExperimentalForeignApi::class)
public actual class AuthCredential private actual constructor() {
    internal lateinit var iosCredential: FIRAuthCredential

    internal constructor(iosCredential: FIRAuthCredential) : this() {
        this.iosCredential = iosCredential
    }

    public actual val provider: String
        get() = iosCredential.provider
}
