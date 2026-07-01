package zone.ien.firebase.appcheck

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.timeIntervalSince1970
import swiftPMImport.zone.ien.firebase.appcheck.firebase.appcheck.FIRAppCheckToken

@OptIn(ExperimentalForeignApi::class)
public actual class AppCheckToken(private val iosToken: FIRAppCheckToken) {
    public actual val token: String
        get() = iosToken.token()
    public actual val expireTimeMillis: Long
        get() = ((iosToken.expirationDate()?.timeIntervalSince1970() ?: 0.0) * 1000).toLong()
}
