package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow

expect open class Query {
    suspend fun get(): QuerySnapshot
    fun snapshots(): Flow<QuerySnapshot>
}
