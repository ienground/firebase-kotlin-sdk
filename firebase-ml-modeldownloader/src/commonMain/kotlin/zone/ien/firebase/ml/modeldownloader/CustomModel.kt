package zone.ien.firebase.ml.modeldownloader

public expect class CustomModel {
    public val name: String
    public val size: Long
    public val modelHash: String
    public val path: String
}
