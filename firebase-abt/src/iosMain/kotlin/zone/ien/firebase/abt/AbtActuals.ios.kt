package zone.ien.firebase.abt

import kotlinx.cinterop.ExperimentalForeignApi

actual class AbtException(message: String?, cause: Throwable?) : Exception(message, cause) {
    constructor(message: String?) : this(message, null)
}

actual class AbtExperimentInfo

@OptIn(ExperimentalForeignApi::class)
actual typealias FirebaseABTesting = swiftPMImport.zone.ien.firebase.firebase.abt.FIRExperimentController
