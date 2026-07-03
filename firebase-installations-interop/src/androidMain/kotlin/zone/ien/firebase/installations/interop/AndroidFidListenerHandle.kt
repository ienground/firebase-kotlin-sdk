package zone.ien.firebase.installations.interop

import com.google.firebase.installations.internal.FidListenerHandle as AndroidFidListenerHandle

public class AndroidFidListenerHandle(
    private val androidHandle: AndroidFidListenerHandle
) : FidListenerHandle {
    override fun unregister() {
        androidHandle.unregister()
    }
}
