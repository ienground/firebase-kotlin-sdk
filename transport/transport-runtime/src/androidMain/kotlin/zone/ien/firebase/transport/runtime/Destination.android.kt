package zone.ien.firebase.transport.runtime

public interface AndroidDestinationProvider {
    public val androidDestination: com.google.android.datatransport.runtime.Destination
}

public val Destination.androidDestination: com.google.android.datatransport.runtime.Destination
    get() = if (this is AndroidDestinationProvider) {
        this.androidDestination
    } else {
        object : com.google.android.datatransport.runtime.Destination {
            override fun getName(): String = name
            override fun getExtras(): ByteArray? = extras
        }
    }
