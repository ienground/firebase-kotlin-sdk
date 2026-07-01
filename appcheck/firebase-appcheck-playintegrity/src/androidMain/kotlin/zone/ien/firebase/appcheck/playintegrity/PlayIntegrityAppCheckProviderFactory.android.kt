package zone.ien.firebase.appcheck.playintegrity

import zone.ien.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.AppCheckProviderFactory as AndroidAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory as AndroidPlayIntegrityAppCheckProviderFactory

public actual class PlayIntegrityAppCheckProviderFactory private actual constructor() : AppCheckProviderFactory {
    private lateinit var androidPlayIntegrityFactory: AndroidPlayIntegrityAppCheckProviderFactory
    internal constructor(androidPlayIntegrityFactory: AndroidPlayIntegrityAppCheckProviderFactory) : this() {
        this.androidPlayIntegrityFactory = androidPlayIntegrityFactory
    }
    override val androidFactory: AndroidAppCheckProviderFactory
        get() = androidPlayIntegrityFactory
    public actual companion object {
        public actual fun getInstance(): PlayIntegrityAppCheckProviderFactory {
            return PlayIntegrityAppCheckProviderFactory(AndroidPlayIntegrityAppCheckProviderFactory.getInstance())
        }
    }
}
