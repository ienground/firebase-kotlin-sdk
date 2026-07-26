package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFirestoreSource
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRListenSource
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRSnapshotListenOptions
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRSnapshotMetadata

@OptIn(ExperimentalForeignApi::class)
internal fun Source.toIosSource(): FIRFirestoreSource = when (this) {
    Source.DEFAULT -> FIRFirestoreSource.FIRFirestoreSourceDefault
    Source.SERVER -> FIRFirestoreSource.FIRFirestoreSourceServer
    Source.CACHE -> FIRFirestoreSource.FIRFirestoreSourceCache
}

@OptIn(ExperimentalForeignApi::class)
internal fun snapshotListenOptions(
    includeMetadataChanges: Boolean,
    source: ListenSource
): FIRSnapshotListenOptions {
    val iosSource = when (source) {
        ListenSource.DEFAULT -> FIRListenSource.FIRListenSourceDefault
        ListenSource.CACHE -> FIRListenSource.FIRListenSourceCache
    }
    return FIRSnapshotListenOptions()
        .optionsWithIncludeMetadataChanges(includeMetadataChanges)
        .optionsWithSource(iosSource)
}

@OptIn(ExperimentalForeignApi::class)
internal fun FIRSnapshotMetadata.toCommonMetadata(): SnapshotMetadata = SnapshotMetadata(
    hasPendingWrites = pendingWrites,
    isFromCache = fromCache
)
