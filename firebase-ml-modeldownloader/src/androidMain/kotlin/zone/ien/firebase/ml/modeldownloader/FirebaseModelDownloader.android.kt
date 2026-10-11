package zone.ien.firebase.ml.modeldownloader

import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader as AndroidFirebaseModelDownloader
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions as AndroidConditions
import com.google.firebase.ml.modeldownloader.DownloadType as AndroidDownloadType
import kotlinx.coroutines.tasks.await
import zone.ien.firebase.FirebaseApp

public actual class FirebaseModelDownloader(
    internal val androidModelDownloader: AndroidFirebaseModelDownloader
) {
    public actual suspend fun getModel(
        modelName: String,
        downloadType: DownloadType,
        conditions: CustomModelDownloadConditions?
    ): CustomModel {
        val androidConditions = conditions?.let {
            val builder = AndroidConditions.Builder()
            if (it.requireWifi) builder.requireWifi()
            if (it.requireDeviceIdle) builder.requireDeviceIdle()
            if (it.requireCharging) builder.requireCharging()
            builder.build()
        }
        val androidDownloadType = when (downloadType) {
            DownloadType.LOCAL_MODEL -> AndroidDownloadType.LOCAL_MODEL
            DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND -> AndroidDownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND
            DownloadType.LATEST_MODEL -> AndroidDownloadType.LATEST_MODEL
        }
        val result = androidModelDownloader.getModel(modelName, androidDownloadType, androidConditions).await()
        return CustomModel(result)
    }

    public actual suspend fun listDownloadedModels(): Set<CustomModel> {
        val results = androidModelDownloader.listDownloadedModels().await()
        return results.map { CustomModel(it) }.toSet()
    }

    public actual suspend fun deleteDownloadedModel(modelName: String) {
        androidModelDownloader.deleteDownloadedModel(modelName).await()
    }

    public actual companion object {
        public actual val instance: FirebaseModelDownloader
            get() = FirebaseModelDownloader(AndroidFirebaseModelDownloader.getInstance())

        public actual fun getInstance(app: FirebaseApp): FirebaseModelDownloader {
            return FirebaseModelDownloader(AndroidFirebaseModelDownloader.getInstance(app.androidApp))
        }
    }
}
