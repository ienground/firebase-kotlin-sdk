package zone.ien.firebase.installations.interop

import com.google.firebase.installations.internal.FidListener as PlatformFidListener

public class AndroidFidListener(
    private val listener: FidListener
) : PlatformFidListener {
    override fun onFidChanged(fid: String) {
        listener.onFidChanged(fid)
    }
}
