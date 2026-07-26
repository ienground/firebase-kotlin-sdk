package zone.ien.firebase.storage

import kotlinx.coroutines.flow.Flow

public expect class DownloadTask {
    public suspend fun await()
    public fun snapshots(): Flow<DownloadTaskSnapshot>
}

public expect class DownloadTaskSnapshot {
    public val bytesTransferred: Long
    public val totalByteCount: Long
}
