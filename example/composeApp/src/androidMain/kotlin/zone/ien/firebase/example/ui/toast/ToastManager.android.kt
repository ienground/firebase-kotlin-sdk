package zone.ien.firebase.example.ui.toast

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import zone.ien.firebase.example.data.AppStateManager

import zone.ien.firebase.FirebasePlatformContext

public actual fun showToast(message: String) {
    val context = AppStateManager.context?.androidContext?.applicationContext
        ?: if (zone.ien.firebase.FirebaseApp.isInitialized) {
            com.google.firebase.FirebaseApp.getInstance().applicationContext
        } else null

    if (context == null) {
        println("showToast failed: Context is null. Message: $message")
        return
    }

    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
