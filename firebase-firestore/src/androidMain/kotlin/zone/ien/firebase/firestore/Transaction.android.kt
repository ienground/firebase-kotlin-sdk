package zone.ien.firebase.firestore

import com.google.firebase.firestore.Transaction as AndroidTransaction
import com.google.firebase.firestore.SetOptions as AndroidSetOptions

actual class Transaction(private val androidTransaction: AndroidTransaction) {
    actual fun get(documentRef: DocumentReference): DocumentSnapshot {
        val androidSnapshot = androidTransaction.get(documentRef.androidDocument)
        return DocumentSnapshot(androidSnapshot)
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any>): Transaction {
        androidTransaction.set(documentRef.androidDocument, data.toAndroidData())
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any>, merge: Boolean): Transaction {
        if (merge) {
            androidTransaction.set(documentRef.androidDocument, data.toAndroidData(), AndroidSetOptions.merge())
        } else {
            androidTransaction.set(documentRef.androidDocument, data.toAndroidData())
        }
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any>): Transaction {
        androidTransaction.update(documentRef.androidDocument, data.toAndroidData())
        return this
    }

    actual fun delete(documentRef: DocumentReference): Transaction {
        androidTransaction.delete(documentRef.androidDocument)
        return this
    }
}
