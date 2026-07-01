package zone.ien.firebase.example

import zone.ien.firebase.database.FirebaseDatabase
import zone.ien.firebase.database.DatabaseException

public object DatabaseTest {
    public suspend fun runTest() {
        try {
            // Get database instance
            val database = FirebaseDatabase.getInstance()
            
            // Get reference
            val myRef = database.reference("test_path")
            
            // Set value
            myRef.setValue("Hello, Realtime Database KMP!")
            
            // Push sub-child and set value
            val childRef = myRef.child("items").push()
            childRef.setValue(mapOf(
                "title" to "KMP Reference Book",
                "timestamp" to 1234567890L
            ))
            
            // Delete value
            childRef.removeValue()
            
            // Update children
            myRef.updateChildren(mapOf(
                "last_active" to "2026-07-01",
                "status" to "online"
            ))
            
        } catch (e: DatabaseException) {
            println("Database test failed: ${e.message}")
        }
    }
}
