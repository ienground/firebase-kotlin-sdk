package zone.ien.firebase.firestore

enum class LoadBundleTaskState {
    ERROR,
    RUNNING,
    SUCCESS
}

data class LoadBundleTaskProgress(
    val documentsLoaded: Int,
    val totalDocuments: Int,
    val bytesLoaded: Long,
    val totalBytes: Long,
    val taskState: LoadBundleTaskState
)
