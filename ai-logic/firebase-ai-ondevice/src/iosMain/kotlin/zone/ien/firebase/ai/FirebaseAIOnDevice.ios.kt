package zone.ien.firebase.ai

public actual enum class InferenceMode {
    PREFER_ON_DEVICE,
    PREFER_IN_CLOUD,
    ONLY_ON_DEVICE
}

public actual class OnDeviceConfig actual constructor(
    public actual val mode: InferenceMode
)

public actual fun FirebaseAI.generativeModel(
    modelName: String,
    onDeviceConfig: OnDeviceConfig
): GenerativeModel {
    throw UnsupportedOperationException("Firebase AI On-Device is not supported on iOS yet.")
}
