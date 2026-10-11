package zone.ien.firebase.example.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import zone.ien.firebase.FirebasePlatformContext
import zone.ien.firebase.example.ui.navigation.ScreenRoute
import zone.ien.firebase.example.ui.toast.showToast

public enum class FirebaseInitState {
    NotInitialized,
    Initializing,
    Initialized,
    InitializationFailed
}

public data class HomeFeatureItem(
    val title: String,
    val subtitle: String,
    val indicatorColor: Color,
    val route: ScreenRoute,
    val supportsAndroid: Boolean,
    val supportsIos: Boolean,
    val requiresInitialization: Boolean = true,
    val isIosSimulated: Boolean = false
)

public object AppStateManager {
    public var context: FirebasePlatformContext? = null
    public var initState: FirebaseInitState by mutableStateOf(FirebaseInitState.NotInitialized)

    public fun handleFeatureNavigation(
        item: HomeFeatureItem,
        onNavigate: (ScreenRoute) -> Unit
    ) {
        if (item.requiresInitialization && initState != FirebaseInitState.Initialized) {
            showToast("Please initialize Firebase first.")
        } else {
            onNavigate(item.route)
        }
    }
}
