package zone.ien.firebase.ai

import com.google.firebase.ai.InferenceMode as AndroidInferenceMode
import com.google.firebase.ai.OnDeviceConfig as AndroidOnDeviceConfig
import com.google.firebase.ai.type.PublicPreviewAPI
import zone.ien.firebase.InternalFirebaseApi

@OptIn(PublicPreviewAPI::class)
public actual enum class InferenceMode(internal val androidMode: AndroidInferenceMode) {
    PREFER_ON_DEVICE(AndroidInferenceMode.PREFER_ON_DEVICE),
    PREFER_IN_CLOUD(AndroidInferenceMode.PREFER_IN_CLOUD),
    ONLY_ON_DEVICE(AndroidInferenceMode.ONLY_ON_DEVICE)
}

@OptIn(PublicPreviewAPI::class)
public actual class OnDeviceConfig actual constructor(
    public actual val mode: InferenceMode
) {
    internal val androidConfig = AndroidOnDeviceConfig(mode.androidMode)
}

@OptIn(PublicPreviewAPI::class, InternalFirebaseApi::class)
public actual fun FirebaseAI.generativeModel(
    modelName: String,
    onDeviceConfig: OnDeviceConfig
): GenerativeModel {
    val androidModel = this.androidAI.generativeModel(
        modelName = modelName,
        onDeviceConfig = onDeviceConfig.androidConfig
    )
    return GenerativeModel(androidModel)
}
