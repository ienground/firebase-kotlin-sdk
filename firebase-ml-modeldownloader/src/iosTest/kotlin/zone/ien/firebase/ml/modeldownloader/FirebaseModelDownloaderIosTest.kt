package zone.ien.firebase.ml.modeldownloader

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FirebaseModelDownloaderIosTest {
    @Test
    fun testSavesDownloadedModelToMemoryList() = runBlocking {
        val downloader = FirebaseModelDownloader.instance
        val modelName = "ios-test-model"
        downloader.deleteDownloadedModel(modelName)

        val model = downloader.getModel(modelName, DownloadType.LATEST_MODEL, null)

        assertEquals(modelName, model.name)
        assertEquals(15L * 1024L * 1024L, model.size)
        assertTrue(downloader.listDownloadedModels().any { it.name == modelName })
    }

    @Test
    fun testDeletesSavedModel() = runBlocking {
        val downloader = FirebaseModelDownloader.instance
        val modelName = "ios-delete-model"
        downloader.getModel(modelName, DownloadType.LOCAL_MODEL, null)

        downloader.deleteDownloadedModel(modelName)

        assertTrue(downloader.listDownloadedModels().none { it.name == modelName })
    }
}
