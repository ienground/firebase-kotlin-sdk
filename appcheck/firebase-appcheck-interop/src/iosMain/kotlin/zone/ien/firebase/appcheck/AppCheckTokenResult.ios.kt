package zone.ien.firebase.appcheck

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.appcheck.firebase.appcheck.interop.FIRAppCheckTokenResultInteropProtocol

@OptIn(ExperimentalForeignApi::class)
public actual class AppCheckTokenResult private actual constructor() {
    private var iosResult: FIRAppCheckTokenResultInteropProtocol? = null

    public constructor(iosResult: FIRAppCheckTokenResultInteropProtocol?) : this() {
        this.iosResult = iosResult
    }

    public actual val token: String
        get() = iosResult?.token ?: ""
    public actual val error: Exception?
        get() = iosResult?.error?.let { Exception(it.localizedDescription) }
}
