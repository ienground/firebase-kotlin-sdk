package zone.ien.firebase.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.firebase.FirebasePlatformContext
import zone.ien.firebase.example.ui.core.FirebaseInitScreen
import zone.ien.firebase.example.ui.firestore.FirestoreScreen
import zone.ien.firebase.example.ui.home.HomeScreen
import zone.ien.firebase.example.ui.navigation.ScreenNavigationGraph
import zone.ien.firebase.example.ui.navigation.ScreenRoute
import zone.ien.firebase.example.ui.placeholder.PlaceholderScreen
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.utils.navigation.rememberNavBackStack

@Composable
fun App(context: FirebasePlatformContext) {
    // Explicit Backstack key-based Navigation3 State Framework
    val backStack = rememberNavBackStack<ScreenRoute>(ScreenRoute.Home)
    AppTheme {
        Scaffold(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ScreenNavigationGraph(
                    context = context,
                    backStack = backStack
                )
            }
        }
    }
}