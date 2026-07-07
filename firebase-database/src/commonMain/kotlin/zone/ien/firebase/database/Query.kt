package zone.ien.firebase.database

import kotlinx.coroutines.flow.Flow

public expect open class Query {
    public suspend fun get(): DataSnapshot
    public fun snapshots(): Flow<DataSnapshot>

    public fun orderByChild(path: String): Query
    public fun orderByKey(): Query
    public fun orderByValue(): Query
    public fun limitToFirst(limit: Int): Query
    public fun limitToLast(limit: Int): Query
    public fun equalTo(value: String): Query
    public fun equalTo(value: Double): Query
    public fun equalTo(value: Boolean): Query
}
