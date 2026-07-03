package zone.ien.firebase.installations.interop

import com.google.firebase.installations.internal.FidListenerHandle as PlatformFidListenerHandle

public class AndroidFidListenerHandle(
    private val androidHandle: PlatformFidListenerHandle
) : FidListenerHandle {
    override fun unregister() {
        androidHandle.unregister()
    }
}
