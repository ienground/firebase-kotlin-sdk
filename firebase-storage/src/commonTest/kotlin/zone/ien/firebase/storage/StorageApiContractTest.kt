package zone.ien.firebase.storage

import kotlin.test.Test
import kotlin.test.assertEquals

class StorageApiContractTest {
    @Test
    fun 저장소_타입이_공통_API에_노출된다() {
        assertEquals("FirebaseStorage", FirebaseStorage::class.simpleName)
        assertEquals("StorageReference", StorageReference::class.simpleName)
        assertEquals("UploadTask", UploadTask::class.simpleName)
    }
}
