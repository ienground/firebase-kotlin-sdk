package zone.ien.firebase.transport.runtime

import android.content.Context
import zone.ien.firebase.transport.TransportFactory
import zone.ien.firebase.transport.AndroidTransportFactoryWrapper

public actual class TransportRuntime internal constructor(
    internal val androidRuntime: com.google.android.datatransport.runtime.TransportRuntime
) {
    public actual fun newFactory(destinationName: String): TransportFactory {
        val androidFactory = androidRuntime.newFactory(destinationName)
        return AndroidTransportFactoryWrapper(androidFactory)
    }

    public actual fun newFactory(destination: Destination): TransportFactory {
        val androidFactory = androidRuntime.newFactory(destination.androidDestination)
        return AndroidTransportFactoryWrapper(androidFactory)
    }

    public actual companion object {
        public actual fun initialize(context: Any) {
            val appCtx = when {
                context is Context -> context.applicationContext
                context.javaClass.name == "zone.ien.firebase.FirebasePlatformContext" -> {
                    try {
                        val getAndroidContext = context.javaClass.getMethod("getAndroidContext")
                        val androidContext = getAndroidContext.invoke(context) as Context
                        androidContext.applicationContext
                    } catch (e: Exception) {
                        // fallback to field reflection if getter is not present
                        val field = context.javaClass.getDeclaredField("androidContext")
                        field.isAccessible = true
                        val androidContext = field.get(context) as Context
                        androidContext.applicationContext
                    }
                }
                else -> throw IllegalArgumentException("Android Context or FirebasePlatformContext is required to initialize TransportRuntime.")
            }
            com.google.android.datatransport.runtime.TransportRuntime.initialize(appCtx)
        }

        public actual fun getInstance(): TransportRuntime {
            val android = com.google.android.datatransport.runtime.TransportRuntime.getInstance()
            return TransportRuntime(android)
        }
    }
}
