package zone.ien.firebase.database

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.database.FIRMutableData

@OptIn(ExperimentalForeignApi::class)
public actual class MutableData(private val iosData: FIRMutableData) {
    public actual val key: String?
        get() = iosData.key
    public actual val value: Any?
        get() = iosData.value
    public actual fun setValue(value: Any?) {
        iosData.setValue(value)
    }
}
