package zone.ien.firebase.functions

import kotlinx.cinterop.ExperimentalForeignApi
import zone.ien.firebase.FirebaseApp
import swiftPMImport.zone.ien.firebase.firebase.functions.FIRFunctions
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseFunctions private constructor(private val iosFunctions: FIRFunctions) {
    actual fun getHttpsCallable(name: String): HttpsCallableReference {
        return HttpsCallableReference(iosFunctions.HTTPSCallableWithName(name))
    }

    actual fun getHttpsCallableFromUrl(url: String): HttpsCallableReference {
        return HttpsCallableReference(iosFunctions.HTTPSCallableWithURL(NSURL(string = url)))
    }

    actual companion object {
        actual fun getInstance(): FirebaseFunctions {
            return FirebaseFunctions(FIRFunctions.functions())
        }

        actual fun getInstance(app: FirebaseApp): FirebaseFunctions {
            return FirebaseFunctions(FIRFunctions.functionsForApp(app.iosApp))
        }

        actual fun getInstance(region: String): FirebaseFunctions {
            return FirebaseFunctions(FIRFunctions.functionsForRegion(region))
        }

        actual fun getInstance(app: FirebaseApp, region: String): FirebaseFunctions {
            return FirebaseFunctions(FIRFunctions.functionsForApp(app.iosApp, region = region))
        }
    }
}
