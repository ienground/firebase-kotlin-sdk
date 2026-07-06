package zone.ien.firebase.database

import com.google.firebase.database.Query as AndroidQuery
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot as AndroidDataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

public actual open class Query(private val androidQuery: AndroidQuery) {
    public actual suspend fun get(): DataSnapshot {
        return DataSnapshot(androidQuery.get().await())
    }

    public actual fun snapshots(): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: AndroidDataSnapshot) {
                trySend(DataSnapshot(snapshot))
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        androidQuery.addValueEventListener(listener)
        awaitClose {
            androidQuery.removeEventListener(listener)
        }
    }

    public actual fun orderByChild(path: String): Query = Query(androidQuery.orderByChild(path))
    public actual fun orderByKey(): Query = Query(androidQuery.orderByKey())
    public actual fun orderByValue(): Query = Query(androidQuery.orderByValue())
    public actual fun limitToFirst(limit: Int): Query = Query(androidQuery.limitToFirst(limit))
    public actual fun limitToLast(limit: Int): Query = Query(androidQuery.limitToLast(limit))
    public actual fun equalTo(value: String): Query = Query(androidQuery.equalTo(value))
    public actual fun equalTo(value: Double): Query = Query(androidQuery.equalTo(value))
    public actual fun equalTo(value: Boolean): Query = Query(androidQuery.equalTo(value))
}
