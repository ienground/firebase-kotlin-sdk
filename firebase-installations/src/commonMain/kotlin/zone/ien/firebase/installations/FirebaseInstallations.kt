package zone.ien.firebase.installations

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import zone.ien.firebase.Firebase
import zone.ien.firebase.FirebaseApp

import zone.ien.firebase.installations.interop.FirebaseInstallationsApi
import zone.ien.firebase.installations.interop.FidListener
import zone.ien.firebase.installations.interop.FidListenerHandle

public expect class FirebaseInstallations : FirebaseInstallationsApi {
    public override suspend fun getId(): String
    public override suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult
    public override suspend fun delete()
    public override fun clearFidCache()
    public override fun registerFidListener(listener: FidListener): FidListenerHandle

    public companion object {
        public val instance: FirebaseInstallations
        public fun getInstance(app: FirebaseApp): FirebaseInstallations
    }
}

public val Firebase.installations: FirebaseInstallations
    get() = FirebaseInstallations.instance

public fun Firebase.installations(app: FirebaseApp): FirebaseInstallations =
    FirebaseInstallations.getInstance(app)

@OptIn(ExperimentalAtomicApi::class)
internal class FidMemoryState {
    private data class State(
        val cachedFid: String? = null,
        val nextListenerId: Long = 0L,
        val listeners: Map<Long, FidListener> = emptyMap()
    )

    private val state: AtomicReference<State> = AtomicReference(State())

    fun recordFid(fid: String) {
        var listenerSnapshot: List<FidListener>
        while (true) {
            val current = state.load()
            if (current.cachedFid == fid) return

            if (state.compareAndSet(current, current.copy(cachedFid = fid))) {
                listenerSnapshot = current.listeners.values.toList()
                break
            }
        }

        listenerSnapshot.forEach { listener ->
            runCatching { listener.onFidChanged(fid) }
        }
    }

    fun clearFidCache() {
        while (true) {
            val current = state.load()
            if (current.cachedFid == null) return
            if (state.compareAndSet(current, current.copy(cachedFid = null))) return
        }
    }

    fun registerFidListener(listener: FidListener): FidListenerHandle {
        var listenerId: Long
        while (true) {
            val current = state.load()
            val updated = current.copy(
                nextListenerId = current.nextListenerId + 1,
                listeners = current.listeners + (current.nextListenerId to listener)
            )
            if (state.compareAndSet(current, updated)) {
                listenerId = current.nextListenerId
                break
            }
        }

        return object : FidListenerHandle {
            override fun unregister() {
                unregisterFidListener(listenerId)
            }
        }
    }

    private fun unregisterFidListener(listenerId: Long) {
        while (true) {
            val current = state.load()
            if (listenerId !in current.listeners) return
            if (state.compareAndSet(current, current.copy(listeners = current.listeners - listenerId))) return
        }
    }
}

@OptIn(ExperimentalAtomicApi::class)
internal class IdentityInstanceCache<T : Any> {
    private val instances: AtomicReference<Map<String, T>> = AtomicReference(emptyMap())

    fun getOrCreate(key: String, factory: () -> T): T {
        while (true) {
            val current = instances.load()
            current[key]?.let { return it }

            val created = factory()
            if (instances.compareAndSet(current, current + (key to created))) {
                return created
            }
        }
    }
}
