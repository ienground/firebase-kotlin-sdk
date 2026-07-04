package zone.ien.firebase.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import zone.ien.firebase.FirebasePlatformContext
import zone.ien.firebase.transport.runtime.TransportRuntime
import zone.ien.firebase.example.ui.navigation.ScreenNavigationGraph
import zone.ien.firebase.example.ui.navigation.ScreenRoute
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.utils.navigation.rememberNavBackStack
import zone.ien.utils.ui.wrapper.M3RootWrapper

@Composable
fun App(context: FirebasePlatformContext) {
    // Initialize Datatransport Runtime using unified context
    remember(context) {
        TransportRuntime.initialize(context)
    }

    // Explicit Backstack key-based Navigation3 State Framework
    val backStack = rememberNavBackStack<ScreenRoute>(ScreenRoute.Home)

    M3RootWrapper {
        AppTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ScreenNavigationGraph(
                    context = context,
                    backStack = backStack
                )
            }
        }
    }
}