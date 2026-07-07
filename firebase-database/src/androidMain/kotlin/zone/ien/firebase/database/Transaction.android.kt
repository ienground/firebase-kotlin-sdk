package zone.ien.firebase.database

import com.google.firebase.database.MutableData as AndroidMutableData

public actual class MutableData(private val androidData: AndroidMutableData) {
    public actual val key: String?
        get() = androidData.key
    public actual val value: Any?
        get() = androidData.value
    public actual fun setValue(value: Any?) {
        androidData.value = value
    }
}
