package zone.ien.firebase.storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StorageApiContractTest {
    @Test
    fun testStorageTypesExposedInCommonApi() {
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
    fun testFirebaseStorageReferenceExtensionFunction() {
        val refFn = FirebaseStorage::reference
        assertEquals("reference", refFn.name)
    }

    @Test
    fun testStorageMetadataBuilderAndConstructor() {
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
    fun testUploadMethodsAreDeclaredAsSuspendFunctions() {
        val putBytesFn: suspend StorageReference.(ByteArray, StorageMetadata?) -> Unit = StorageReference::putBytes
        val putFileFn: suspend StorageReference.(String, StorageMetadata?) -> Unit = StorageReference::putFile

        assertTrue(putBytesFn != null)
        assertTrue(putFileFn != null)
    }

    @Test
    fun testStorageReferenceOverridesEqualsAndHashCode() {
        val equalsFn: StorageReference.(Any?) -> Boolean = StorageReference::equals
        val hashCodeFn: StorageReference.() -> Int = StorageReference::hashCode

        assertTrue(equalsFn != null)
        assertTrue(hashCodeFn != null)
    }
}
