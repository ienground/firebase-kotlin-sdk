package zone.ien.firebase.storage

import kotlin.test.Test
import kotlin.test.assertEquals

class StorageApiContractTest {
    @Test
    fun 저장소_타입이_공통_API에_노출된다() {
        assertEquals("FirebaseStorage", FirebaseStorage::class.simpleName)
        assertEquals("StorageReference", StorageReference::class.simpleName)
        assertEquals("UploadTask", UploadTask::class.simpleName)
        assertEquals("UploadTaskSnapshot", UploadTaskSnapshot::class.simpleName)
        assertEquals("DownloadTask", DownloadTask::class.simpleName)
        assertEquals("DownloadTaskSnapshot", DownloadTaskSnapshot::class.simpleName)
        assertEquals("StorageMetadata", StorageMetadata::class.simpleName)
        assertEquals("StorageMetadata", FirebaseStorageMetadata::class.simpleName)
        assertEquals("File", File::class.simpleName)
        assertEquals("Data", Data::class.simpleName)
    }

    @Test
    fun StorageMetadata_빌더_및_생성자_동작을_확인한다() {
        val metadata = storageMetadata {
            contentType = "image/png"
            cacheControl = "max-age=3600"
            setCustomMetadata("key1", "value1")
        }

        assertEquals("image/png", metadata.contentType)
        assertEquals("max-age=3600", metadata.cacheControl)
        assertEquals(mapOf("key1" to "value1"), metadata.customMetadata)
    }
}
