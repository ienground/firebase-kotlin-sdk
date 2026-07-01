package zone.ien.firebase.appcheck.debug.testing

import com.google.firebase.appcheck.debug.testing.DebugAppCheckTestHelper as AndroidDebugAppCheckTestHelper

import zone.ien.firebase.FirebaseApp

public actual class DebugAppCheckTestHelper private constructor(
    private val androidHelper: AndroidDebugAppCheckTestHelper
) {
    public actual fun withDebugProvider(runnable: () -> Unit) {
        androidHelper.withDebugProvider<Throwable> {
            runnable()
        }
    }

    public actual fun withDebugProvider(app: FirebaseApp, runnable: () -> Unit) {
        androidHelper.withDebugProvider<Throwable>(app.androidApp) {
            runnable()
        }
    }

    public actual companion object {
        public actual fun fromInstrumentationArgs(): DebugAppCheckTestHelper {
            return DebugAppCheckTestHelper(AndroidDebugAppCheckTestHelper.fromInstrumentationArgs())
        }
    }
}
