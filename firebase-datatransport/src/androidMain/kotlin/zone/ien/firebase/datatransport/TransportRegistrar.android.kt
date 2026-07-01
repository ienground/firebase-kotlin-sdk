package zone.ien.firebase.datatransport

import com.google.firebase.datatransport.TransportRegistrar as AndroidTransportRegistrar

public actual class TransportRegistrar {
    private val androidRegistrar = AndroidTransportRegistrar()
}
