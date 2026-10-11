package zone.ien.firebase.appcheck

import com.google.firebase.appcheck.AppCheckProviderFactory as AndroidAppCheckProviderFactory

public actual interface AppCheckProviderFactory {
    public val androidFactory: AndroidAppCheckProviderFactory
}
