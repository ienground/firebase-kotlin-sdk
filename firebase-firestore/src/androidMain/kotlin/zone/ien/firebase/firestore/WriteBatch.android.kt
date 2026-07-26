package zone.ien.firebase.firestore

import com.google.firebase.firestore.WriteBatch as AndroidWriteBatch
import com.google.firebase.firestore.SetOptions as AndroidSetOptions

actual class WriteBatch(private val androidBatch: AndroidWriteBatch) {
    actual fun set(documentRef: DocumentReference, data: Map<String, Any>): WriteBatch {
        androidBatch.set(documentRef.androidDocument, data.toAndroidData())
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any>, merge: Boolean): WriteBatch {
        if (merge) {
            androidBatch.set(documentRef.androidDocument, data.toAndroidData(), AndroidSetOptions.merge())
        } else {
            androidBatch.set(documentRef.androidDocument, data.toAndroidData())
        }
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any>): WriteBatch {
        androidBatch.update(documentRef.androidDocument, data.toAndroidData())
        return this
    }

    actual fun delete(documentRef: DocumentReference): WriteBatch {
        androidBatch.delete(documentRef.androidDocument)
        return this
    }

    actual suspend fun commit() {
        androidBatch.commit().await()
    }
}
