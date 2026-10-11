package zone.ien.firebase.example

import zone.ien.firebase.appcheck.debug.testing.DebugAppCheckTestHelper

public object AppCheckDebugTestingTest {
    public fun setupAppCheckDebugToken(envToken: String?) {
        // 1. Get debug token safely
        // Never hardcode active secrets inside repository.
        // It's recommended to pull token dynamically via CI environment variables or local properties.
        val token = envToken ?: "PLACEHOLDER_DEBUG_TOKEN"
        
        try {
            // 2. Obtain instrumentation test helper instance
            val helperInstance = DebugAppCheckTestHelper.fromInstrumentationArgs()
            // 3. Run test code block under debug provider environment
            helperInstance.withDebugProvider {
                println("Code block is now running with AppCheck debug token installed.")
            }
        } catch (e: UnsupportedOperationException) {
            // Safe fallback on iOS where runtime helper is unsupported
            println("Debug token helper fallback: ${e.message}")
        }
    }
}
