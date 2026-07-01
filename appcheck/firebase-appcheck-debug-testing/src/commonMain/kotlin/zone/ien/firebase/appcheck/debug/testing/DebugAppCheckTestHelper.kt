package zone.ien.firebase.appcheck.debug.testing

import zone.ien.firebase.FirebaseApp

public expect class DebugAppCheckTestHelper {
    public fun withDebugProvider(runnable: () -> Unit)
    public fun withDebugProvider(app: FirebaseApp, runnable: () -> Unit)

    public companion object {
        public fun fromInstrumentationArgs(): DebugAppCheckTestHelper
    }
}
