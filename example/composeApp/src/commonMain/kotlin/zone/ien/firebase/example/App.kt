package zone.ien.firebase.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import zone.ien.utils.ui.screen.IenScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import zone.ien.firebase.FirebasePlatformContext
import zone.ien.firebase.transport.runtime.TransportRuntime
import zone.ien.firebase.example.ui.navigation.ScreenNavigationGraph
import zone.ien.firebase.example.ui.navigation.ScreenRoute
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.utils.navigation.rememberNavBackStack
import zone.ien.utils.ui.wrapper.IenRootWrapper

import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.example.data.AppStateManager
import zone.ien.firebase.example.data.FirebaseInitState
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme

@Composable
fun App(context: FirebasePlatformContext) {
    remember(context) {
        AppStateManager.context = context
        if (FirebaseApp.isInitialized) {
            AppStateManager.initState = FirebaseInitState.Initialized
        }
    }

    // Initialize Datatransport Runtime using unified context
    remember(context) {
        TransportRuntime.initialize(context)
    }

    // Explicit Backstack key-based Navigation3 State Framework
    val backStack = rememberNavBackStack<ScreenRoute>(ScreenRoute.Home)

    IenAdaptiveTheme(
        target = Theme.Material3
    ) {
        IenRootWrapper {
            IenScaffold(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ScreenNavigationGraph(
                        context = context,
                        backStack = backStack
                    )
                }
            }
        }
    }
}