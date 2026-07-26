package zone.ien.firebase.firestore

import com.google.firebase.firestore.ListenSource as AndroidListenSource
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.SnapshotListenOptions as AndroidSnapshotListenOptions
import com.google.firebase.firestore.Source as AndroidSource

internal fun Source.toAndroidSource(): AndroidSource = when (this) {
    Source.DEFAULT -> AndroidSource.DEFAULT
    Source.SERVER -> AndroidSource.SERVER
    Source.CACHE -> AndroidSource.CACHE
}

internal fun snapshotListenOptions(
    includeMetadataChanges: Boolean,
    source: ListenSource
): AndroidSnapshotListenOptions = AndroidSnapshotListenOptions.Builder()
    .setMetadataChanges(
        if (includeMetadataChanges) MetadataChanges.INCLUDE else MetadataChanges.EXCLUDE
    )
    .setSource(
        when (source) {
            ListenSource.DEFAULT -> AndroidListenSource.DEFAULT
            ListenSource.CACHE -> AndroidListenSource.CACHE
        }
    )
    .build()

internal fun com.google.firebase.firestore.SnapshotMetadata.toCommonMetadata(): SnapshotMetadata =
    SnapshotMetadata(
        hasPendingWrites = hasPendingWrites(),
        isFromCache = isFromCache
    )
