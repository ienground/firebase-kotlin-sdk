package zone.ien.firebase.example.ui.test

import zone.ien.firebase.sessions.FirebaseSessions

public object SessionsTest {
    public fun verifyCompilation(): Boolean {
        // Minimal compilation reference proving class path visibility
        val sessionsClass = FirebaseSessions::class
        return true
    }
}
