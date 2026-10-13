package zone.ien.firebase.appcheck.interop

import zone.ien.firebase.appcheck.AppCheckTokenResult

public expect interface AppCheckTokenListener {
    public fun onAppCheckTokenChanged(tokenResult: AppCheckTokenResult)
}
