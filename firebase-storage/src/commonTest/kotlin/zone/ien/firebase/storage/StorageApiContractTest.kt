package zone.ien.firebase.storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
    fun FirebaseStorage_reference_확장함수_참조가_가능하다() {
        val refFn = FirebaseStorage::reference
        assertEquals("reference", refFn.name)
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

    @Test
    fun putFile_및_putBytes가_suspend_함수로_선언되어_있다() {
        val members = StorageReference::class.members
        val putBytesMember = members.find { it.name == "putBytes" }
        val putFileMember = members.find { it.name == "putFile" }

        assertTrue(putBytesMember != null, "StorageReference must declare putBytes")
        assertTrue(putBytesMember.isSuspend, "putBytes must be a suspend function (gitlive style)")
        assertTrue(putFileMember != null, "StorageReference must declare putFile")
        assertTrue(putFileMember.isSuspend, "putFile must be a suspend function (gitlive style)")
    }
}

    @Test
    fun StorageReference는_equals와_hashCode를_재정의한다() {
        val equalsMethod = StorageReference::class.members.find { it.name == "equals" }
        val hashCodeMethod = StorageReference::class.members.find { it.name == "hashCode" }

        assertTrue(equalsMethod != null, "StorageReference must override equals")
        assertTrue(hashCodeMethod != null, "StorageReference must override hashCode")
    }
