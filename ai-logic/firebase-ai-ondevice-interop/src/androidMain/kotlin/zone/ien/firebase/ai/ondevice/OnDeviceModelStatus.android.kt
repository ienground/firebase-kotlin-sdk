package zone.ien.firebase.ai.ondevice

import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.OnDeviceModelStatus as AndroidOnDeviceModelStatus

@OptIn(PublicPreviewAPI::class)
public actual enum class OnDeviceModelStatus(internal val androidStatus: AndroidOnDeviceModelStatus) {
    AVAILABLE(AndroidOnDeviceModelStatus.AVAILABLE),
    DOWNLOADABLE(AndroidOnDeviceModelStatus.DOWNLOADABLE),
    DOWNLOADING(AndroidOnDeviceModelStatus.DOWNLOADING),
    UNAVAILABLE(AndroidOnDeviceModelStatus.UNAVAILABLE)
}
