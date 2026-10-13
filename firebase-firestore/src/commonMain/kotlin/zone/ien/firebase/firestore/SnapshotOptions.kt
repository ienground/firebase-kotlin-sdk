package zone.ien.firebase.firestore

enum class Source {
    DEFAULT,
    SERVER,
    CACHE
}

enum class ListenSource {
    DEFAULT,
    CACHE
}

data class SnapshotMetadata(
    val hasPendingWrites: Boolean,
    val isFromCache: Boolean
)
