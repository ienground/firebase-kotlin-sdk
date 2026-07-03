package zone.ien.firebase.installations.interop

import com.google.firebase.installations.internal.FidListener as AndroidFidListener

public class AndroidFidListener(
    private val listener: FidListener
) : AndroidFidListener {
    override fun onFidChanged(fid: String) {
        listener.onFidChanged(fid)
    }
}
