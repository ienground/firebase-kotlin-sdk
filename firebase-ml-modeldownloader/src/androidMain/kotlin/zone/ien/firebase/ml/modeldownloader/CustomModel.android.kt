package zone.ien.firebase.ml.modeldownloader

import com.google.firebase.ml.modeldownloader.CustomModel as AndroidCustomModel

public actual class CustomModel(
    internal val androidCustomModel: AndroidCustomModel
) {
    public actual val name: String
        get() = androidCustomModel.name

    public actual val size: Long
        get() = androidCustomModel.size

    public actual val modelHash: String
        get() = androidCustomModel.modelHash

    public actual val path: String
        get() = androidCustomModel.file?.absolutePath ?: ""
}
