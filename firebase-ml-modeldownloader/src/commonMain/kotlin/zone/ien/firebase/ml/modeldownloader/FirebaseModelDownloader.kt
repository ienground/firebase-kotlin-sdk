package zone.ien.firebase.ml.modeldownloader

import zone.ien.firebase.FirebaseApp

public expect class FirebaseModelDownloader {
    public suspend fun getModel(
        modelName: String,
        downloadType: DownloadType,
        conditions: CustomModelDownloadConditions?
    ): CustomModel

    public suspend fun listDownloadedModels(): Set<CustomModel>

    public suspend fun deleteDownloadedModel(modelName: String)

    public companion object {
        public val instance: FirebaseModelDownloader
        public fun getInstance(app: FirebaseApp): FirebaseModelDownloader
    }
}
