package zone.ien.firebase.functions

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.functions.FIRHTTPSCallableResult

@OptIn(ExperimentalForeignApi::class)
actual class HttpsCallableResult(private val iosResult: FIRHTTPSCallableResult) {
    actual val data: Any?
        get() = iosResult.data()
}
