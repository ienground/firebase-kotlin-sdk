package zone.ien.firebase.ai

public expect class OnDeviceConfig {
    public val mode: InferenceMode
    public constructor(mode: InferenceMode)
}
