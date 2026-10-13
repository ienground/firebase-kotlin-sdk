package zone.ien.firebase.ai

public expect fun FirebaseAI.generativeModel(
    modelName: String,
    onDeviceConfig: OnDeviceConfig
): GenerativeModel
