package zone.ien.firebase.ml.modeldownloader

import zone.ien.firebase.FirebaseApp

public actual class FirebaseModelDownloader {
    public actual suspend fun getModel(
        modelName: String,
        downloadType: DownloadType,
        conditions: CustomModelDownloadConditions?
    ): CustomModel {
        throw UnsupportedOperationException("Firebase Model Downloader is not supported on iOS yet.")
    }

    public actual suspend fun listDownloadedModels(): Set<CustomModel> {
        throw UnsupportedOperationException("Firebase Model Downloader is not supported on iOS yet.")
    }

    public actual suspend fun deleteDownloadedModel(modelName: String) {
        throw UnsupportedOperationException("Firebase Model Downloader is not supported on iOS yet.")
    }

    public actual companion object {
        public actual val instance: FirebaseModelDownloader
            get() = FirebaseModelDownloader()

        public actual fun getInstance(app: FirebaseApp): FirebaseModelDownloader {
            return FirebaseModelDownloader()
        }
    }
}
