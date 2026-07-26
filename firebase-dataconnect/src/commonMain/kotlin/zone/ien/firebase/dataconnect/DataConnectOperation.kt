package zone.ien.firebase.dataconnect

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

public enum class DataConnectFetchPolicy {
    PREFER_CACHE,
    CACHE_ONLY,
    SERVER_ONLY
}

public enum class DataConnectDataSource {
    CACHE,
    SERVER,
    MEMORY
}

public enum class DataConnectFailureKind {
    OPERATION,
    TRANSPORT,
    BRIDGE_REQUIRED
}

public enum class DataConnectRequestStatus {
    PENDING,
    SUCCEEDED,
    FAILED,
    UNSUPPORTED,
    CANCELLED
}

public sealed interface DataConnectErrorPathSegment {
    public data class Field(public val name: String) : DataConnectErrorPathSegment
    public data class ListIndex(public val index: Int) : DataConnectErrorPathSegment
}

public data class DataConnectError(
    public val message: String,
    public val path: List<DataConnectErrorPathSegment>
)

public data class DataConnectFailureResponse(
    public val rawData: Map<String, Any?>?,
    public val data: Any?,
    public val errors: List<DataConnectError>
)

public data class DataConnectRequestState<Variables>(
    public val executionId: Long,
    public val operationName: String,
    public val variables: Variables,
    public val fetchPolicy: DataConnectFetchPolicy?,
    public val status: DataConnectRequestStatus,
    public val failure: DataConnectOperationException? = null
)

public data class DataConnectQueryResult<Data, Variables>(
    public val operationName: String,
    public val variables: Variables,
    public val data: Data,
    public val dataSource: DataConnectDataSource,
    public val executionId: Long = 0
)

public data class DataConnectMutationResult<Data, Variables>(
    public val operationName: String,
    public val variables: Variables,
    public val data: Data,
    public val executionId: Long = 0
)

public class DataConnectOperationException(
    public val operationName: String,
    public val kind: DataConnectFailureKind,
    public val response: DataConnectFailureResponse? = null,
    cause: Throwable? = null
) : Exception(
    "Data Connect operation '$operationName' failed (${kind.name.lowercase()}): " +
        (cause?.message ?: response?.errors?.firstOrNull()?.message ?: "unknown error"),
    cause
) {
    public constructor(operationName: String, cause: Throwable) : this(
        operationName = operationName,
        kind = DataConnectFailureKind.TRANSPORT,
        cause = cause
    )
}

public interface DataConnectQuery<Data, Variables> {
    public val operationName: String
    public fun ref(variables: Variables): DataConnectQueryReference<Data, Variables>
}

/** 생성 코드나 플랫폼 adapter가 factory에 전달하는 공통 query 설명자입니다. */
public interface DataConnectQueryDescriptor<Data, Variables> : DataConnectQuery<Data, Variables>

public interface DataConnectMutation<Data, Variables> {
    public val operationName: String
    public fun ref(variables: Variables): DataConnectMutationReference<Data, Variables>
}

/** 생성 코드나 플랫폼 adapter가 factory에 전달하는 공통 mutation 설명자입니다. */
public interface DataConnectMutationDescriptor<Data, Variables> : DataConnectMutation<Data, Variables>

public interface DataConnectQueryReference<Data, Variables> {
    public val operationName: String
    public val variables: Variables
    public val state: StateFlow<DataConnectRequestState<Variables>>

    public suspend fun execute(
        fetchPolicy: DataConnectFetchPolicy = DataConnectFetchPolicy.PREFER_CACHE
    ): DataConnectQueryResult<Data, Variables>

    public fun subscribe(): Flow<Result<DataConnectQueryResult<Data, Variables>>>
}

public interface DataConnectMutationReference<Data, Variables> {
    public val operationName: String
    public val variables: Variables
    public val state: StateFlow<DataConnectRequestState<Variables>>

    public suspend fun execute(): DataConnectMutationResult<Data, Variables>
}
