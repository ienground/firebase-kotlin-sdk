package zone.ien.firebase.appcheck.debug

import zone.ien.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.AppCheckProviderFactory as AndroidAppCheckProviderFactory
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory as AndroidDebugAppCheckProviderFactory

public actual class DebugAppCheckProviderFactory : AppCheckProviderFactory {
    private val androidDebugFactory = AndroidDebugAppCheckProviderFactory.getInstance()
    override val androidFactory: AndroidAppCheckProviderFactory
        get() = androidDebugFactory
}
