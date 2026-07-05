package zone.ien.firebase.ml.modeldownloader

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import zone.ien.firebase.FirebaseApp

public actual class FirebaseModelDownloader private constructor() {
    private val downloadedModels = mutableMapOf<String, CustomModel>()
    private val mutex = Mutex()

    public actual suspend fun getModel(
        modelName: String,
        downloadType: DownloadType,
        conditions: CustomModelDownloadConditions?
    ): CustomModel = mutex.withLock {
        val mockModel = CustomModel(
            name = modelName,
            size = 1024L * 1024L * 15L, // 15MB
            modelHash = "mock_hash_${modelName.hashCode()}",
            path = "/mock/path/to/$modelName.tflite"
        )
        downloadedModels[modelName] = mockModel
        mockModel
    }

    public actual suspend fun listDownloadedModels(): Set<CustomModel> = mutex.withLock {
        downloadedModels.values.toSet()
    }

    public actual suspend fun deleteDownloadedModel(modelName: String): Unit = mutex.withLock {
        downloadedModels.remove(modelName)
    }

    public actual companion object {
        private val lazyInstance = lazy { FirebaseModelDownloader() }

        public actual val instance: FirebaseModelDownloader
            get() = lazyInstance.value

        public actual fun getInstance(app: FirebaseApp): FirebaseModelDownloader {
            return lazyInstance.value
        }
    }
}
