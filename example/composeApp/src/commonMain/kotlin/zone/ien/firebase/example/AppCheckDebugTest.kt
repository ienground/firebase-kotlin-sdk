package zone.ien.firebase.example

import zone.ien.firebase.appcheck.FirebaseAppCheck
import zone.ien.firebase.appcheck.debug.DebugAppCheckProviderFactory

public object AppCheckDebugTest {
    public fun initializeDebugAppCheck() {
        // 1. Create a debug provider factory mapping to native platform debuggers
        // Android: DebugAppCheckProviderFactory (com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory)
        // iOS: FIRAppCheckDebugProviderFactory (from FirebaseAppCheck SwiftPM framework)
        val debugFactory = DebugAppCheckProviderFactory()
        
        // 2. Obtain AppCheck instance
        val appCheck = FirebaseAppCheck.getInstance()
        
        // 3. Install debug provider factory
        // NOTE: In production code, this should be called BEFORE initializing FirebaseApp if custom provider setups are required,
        // or during the early app startup phase. Never commit actual debug tokens to repository!
        appCheck.installAppCheckProviderFactory(debugFactory)
        
        println("AppCheck Debug Provider Factory successfully installed!")
    }
}
