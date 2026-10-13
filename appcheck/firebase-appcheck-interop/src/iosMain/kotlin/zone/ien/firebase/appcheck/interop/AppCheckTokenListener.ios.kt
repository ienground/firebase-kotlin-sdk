package zone.ien.firebase.appcheck.interop

import zone.ien.firebase.appcheck.AppCheckTokenResult

public actual interface AppCheckTokenListener {
    public actual fun onAppCheckTokenChanged(tokenResult: AppCheckTokenResult)
}
