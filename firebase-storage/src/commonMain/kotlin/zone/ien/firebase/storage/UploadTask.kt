package zone.ien.firebase.storage

import kotlinx.coroutines.flow.Flow

public expect class UploadTask {
    public suspend fun await()
    public fun snapshots(): Flow<UploadTaskSnapshot>
}

public expect class UploadTaskSnapshot {
    public val bytesTransferred: Long
    public val totalByteCount: Long
}
