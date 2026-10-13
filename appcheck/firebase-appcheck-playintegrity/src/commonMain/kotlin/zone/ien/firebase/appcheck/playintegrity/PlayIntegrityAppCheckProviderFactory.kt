package zone.ien.firebase.appcheck.playintegrity

import zone.ien.firebase.appcheck.AppCheckProviderFactory

public expect class PlayIntegrityAppCheckProviderFactory : AppCheckProviderFactory {
    private constructor()
    public companion object {
        public fun getInstance(): PlayIntegrityAppCheckProviderFactory
    }
}
