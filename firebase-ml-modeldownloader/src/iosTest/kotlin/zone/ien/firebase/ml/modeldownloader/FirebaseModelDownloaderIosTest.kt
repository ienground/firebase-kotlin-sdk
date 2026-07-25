package zone.ien.firebase.ml.modeldownloader

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FirebaseModelDownloaderIosTest {
    @Test
    fun 모델을_받으면_메모리_목록에_저장한다() = runBlocking {
        val downloader = FirebaseModelDownloader.instance
        val modelName = "ios-test-model"
        downloader.deleteDownloadedModel(modelName)

        val model = downloader.getModel(modelName, DownloadType.LATEST_MODEL, null)

        assertEquals(modelName, model.name)
        assertEquals(15L * 1024L * 1024L, model.size)
        assertTrue(downloader.listDownloadedModels().any { it.name == modelName })
    }

    @Test
    fun 저장된_모델을_삭제한다() = runBlocking {
        val downloader = FirebaseModelDownloader.instance
        val modelName = "ios-delete-model"
        downloader.getModel(modelName, DownloadType.LOCAL_MODEL, null)

        downloader.deleteDownloadedModel(modelName)

        assertTrue(downloader.listDownloadedModels().none { it.name == modelName })
    }
}
