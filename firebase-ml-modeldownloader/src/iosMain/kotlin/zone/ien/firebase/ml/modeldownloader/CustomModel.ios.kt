package zone.ien.firebase.ml.modeldownloader

public actual class CustomModel(
    public actual val name: String,
    public actual val size: Long,
    public actual val modelHash: String,
    public actual val path: String
)
