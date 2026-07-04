package zone.ien.firebase.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import kotlinx.serialization.Serializable
import zone.ien.firebase.FirebasePlatformContext
import zone.ien.firebase.example.ui.screen.core.FirebaseInitScreen
import zone.ien.firebase.example.ui.screen.firestore.FirestoreScreen
import zone.ien.firebase.example.ui.screen.home.HomeScreen
import zone.ien.firebase.example.ui.screen.placeholder.PlaceholderScreen
import zone.ien.utils.navigation.BaseNavDisplay

@Serializable
sealed interface ScreenRoute: NavKey {
    @Serializable data object Home: ScreenRoute
    @Serializable data object FirebaseInit: ScreenRoute
    @Serializable data object Firestore: ScreenRoute
    @Serializable data object AnalyticsPlaceholder: ScreenRoute
    @Serializable data object MessagingPlaceholder: ScreenRoute
    @Serializable data object Storage: ScreenRoute
    @Serializable data object Functions: ScreenRoute
    @Serializable data object Database: ScreenRoute
    @Serializable data object DatabaseCollection: ScreenRoute
    @Serializable data object PlayIntegrity: ScreenRoute
    @Serializable data object Crashlytics: ScreenRoute
    @Serializable data object Auth: ScreenRoute
    @Serializable data object Performance: ScreenRoute
    @Serializable data object Installations: ScreenRoute
    @Serializable data object ModelDownloader: ScreenRoute
    @Serializable data object RemoteConfig: ScreenRoute
    @Serializable data object AiLogic: ScreenRoute
    @Serializable data object AiLogicOnDevice: ScreenRoute
}

@Composable
fun ScreenNavigationGraph(
    modifier: Modifier = Modifier,
    context: FirebasePlatformContext,
    backStack: NavBackStack<ScreenRoute>
) {
    BaseNavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {
            entry<ScreenRoute.Home> {
                HomeScreen(
                    onNavigate = { backStack.add(it) }
                )
            }
            entry<ScreenRoute.FirebaseInit> {
                FirebaseInitScreen(
                    context = context,
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.Firestore> {
                FirestoreScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.Storage> {
                zone.ien.firebase.example.ui.screen.storage.StorageScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.Functions> {
                zone.ien.firebase.example.ui.screen.functions.FunctionsScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.AnalyticsPlaceholder> {
                PlaceholderScreen(
                    title = "Firebase Analytics",
                    description = "Google Analytics for Firebase wrapper is coming soon to KMP SDK.",
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.MessagingPlaceholder> {
                PlaceholderScreen(
                    title = "Cloud Messaging",
                    description = "Firebase Cloud Messaging (FCM) push notification handler wrapper is coming soon to KMP SDK.",
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.Database> {
                zone.ien.firebase.example.ui.screen.database.DatabaseScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.DatabaseCollection> {
                zone.ien.firebase.example.ui.screen.database.DatabaseCollectionScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.PlayIntegrity> {
                zone.ien.firebase.example.PlayIntegrityScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.Crashlytics> {
                zone.ien.firebase.example.ui.screen.crashlytics.CrashlyticsScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.Auth> {
                zone.ien.firebase.example.ui.screen.auth.AuthScreen(
                    onBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.Performance> {
                zone.ien.firebase.example.ui.screen.perf.PerformanceScreen(
                    onNavigateBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.Installations> {
                zone.ien.firebase.example.ui.screen.installations.InstallationsScreen(
                    onNavigateBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.ModelDownloader> {
                zone.ien.firebase.example.ui.screen.ml.ModelDownloaderScreen(
                    onNavigateBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.RemoteConfig> {
                zone.ien.firebase.example.ui.screen.config.RemoteConfigScreen(
                    onNavigateBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.AiLogic> {
                zone.ien.firebase.example.ui.screen.ai.AiLogicScreen(
                    onNavigateBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
            entry<ScreenRoute.AiLogicOnDevice> {
                zone.ien.firebase.example.ui.screen.ai.AiLogicOnDeviceScreen(
                    onNavigateBack = { backStack.removeAt(backStack.lastIndex) }
                )
            }
        }
    )
}