package zone.ien.firebase.appcheck.debug.testing

import zone.ien.firebase.FirebaseApp

public actual class DebugAppCheckTestHelper private actual constructor() {
    public actual fun withDebugProvider(runnable: () -> Unit) {
        throw UnsupportedOperationException("DebugAppCheckTestHelper is not supported on iOS.")
    }

    public actual fun withDebugProvider(app: FirebaseApp, runnable: () -> Unit) {
        throw UnsupportedOperationException("DebugAppCheckTestHelper is not supported on iOS.")
    }

    public actual companion object {
        public actual fun fromInstrumentationArgs(): DebugAppCheckTestHelper {
            throw UnsupportedOperationException("DebugAppCheckTestHelper is not supported on iOS.")
        }
    }
}
