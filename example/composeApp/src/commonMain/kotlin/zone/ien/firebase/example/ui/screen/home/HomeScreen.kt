package zone.ien.firebase.example.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import zone.ien.firebase.annotations.PreviewApi
import zone.ien.firebase.example.data.AppStateManager
import zone.ien.firebase.example.data.HomeFeatureItem
import zone.ien.firebase.example.icon.AndroidFill
import zone.ien.firebase.example.icon.AppleFill
import zone.ien.firebase.example.icon.Icons
import zone.ien.firebase.example.ui.navigation.ScreenRoute
import zone.ien.firebase.example.util.isIos

@OptIn(ExperimentalMaterial3Api::class)
@PreviewApi
@Composable
public fun HomeScreen(
    onNavigate: (ScreenRoute) -> Unit
) {
    val items = listOf(
        HomeFeatureItem(
            title = "Firebase Init",
            subtitle = "Initialize core Firebase SDK configuration.",
            indicatorColor = Color(0xFFFFC107),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.FirebaseInit
        ),
        HomeFeatureItem(
            title = "Auth",
            subtitle = "Authentication using Google, Email/Password, etc.",
            indicatorColor = Color(0xFF4CAF50),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Auth
        ),
        HomeFeatureItem(
            title = "Firestore",
            subtitle = "Real-time document storage and queries.",
            indicatorColor = Color(0xFFFF9800),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Firestore
        ),
        HomeFeatureItem(
            title = "Storage",
            subtitle = "Upload and download binary large objects.",
            indicatorColor = Color(0xFF03A9F4),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Storage
        ),
        HomeFeatureItem(
            title = "Functions",
            subtitle = "Invoke secure, serverless cloud functions.",
            indicatorColor = Color(0xFFE91E63),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Functions
        ),
        HomeFeatureItem(
            title = "Performance",
            subtitle = "Manual custom trace and HTTP metric logging.",
            indicatorColor = Color(0xFF673AB7),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Performance
        ),
        HomeFeatureItem(
            title = "Installations",
            subtitle = "Query unique installation ID and auth token.",
            indicatorColor = Color(0xFF009688),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Installations
        ),
        HomeFeatureItem(
            title = "Model Downloader",
            subtitle = "Retrieve custom models and monitor status.",
            indicatorColor = Color(0xFF4CAF50),
            supportsAndroid = true,
            supportsIos = false,
            route = ScreenRoute.ModelDownloader
        ),
        HomeFeatureItem(
            title = "Remote Config",
            subtitle = "Fetch, activate, and listen for dynamic parameters.",
            indicatorColor = Color(0xFFFF9800),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.RemoteConfig
        ),
        HomeFeatureItem(
            title = "AI Logic",
            subtitle = "Generate content using Gemini model prompts.",
            indicatorColor = Color(0xFF9C27B0),
            supportsAndroid = true,
            supportsIos = false,
            route = ScreenRoute.AiLogic
        ),
        HomeFeatureItem(
            title = "AI On-Device",
            subtitle = "Hybrid on-device Gemini inference and fallback.",
            indicatorColor = Color(0xFFE040FB),
            supportsAndroid = true,
            supportsIos = false,
            route = ScreenRoute.AiLogicOnDevice
        ),
        HomeFeatureItem(
            title = "Datatransport",
            subtitle = "Telemetry datatransport priority & scheduling contract test.",
            indicatorColor = Color(0xFF3F51B5),
            supportsAndroid = true,
            supportsIos = false,
            route = ScreenRoute.Datatransport
        ),
        HomeFeatureItem(
            title = "App Distribution",
            subtitle = "Check prerelease availability and trigger in-app updates.",
            indicatorColor = Color(0xFFF58220),
            supportsAndroid = true,
            supportsIos = false,
            route = ScreenRoute.AppDistribution
        ),
        HomeFeatureItem(
            title = "Data Connect",
            subtitle = "Configure Postgres database GraphQL query connector bootstrap.",
            indicatorColor = Color(0xFF673AB7),
            supportsAndroid = true,
            supportsIos = false,
            route = ScreenRoute.DataConnect
        ),
        HomeFeatureItem(
            title = "In-App Messaging",
            subtitle = "Trigger contextual campaigns and control display suppression.",
            indicatorColor = Color(0xFFE91E63),
            supportsAndroid = true,
            supportsIos = false,
            route = ScreenRoute.InAppMessaging
        ),
        HomeFeatureItem(
            title = "Encoders",
            subtitle = "Verify foundational encoding interface serialization contract.",
            indicatorColor = Color(0xFF4CAF50),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Encoders
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Firebase KMP Examples",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items) { item ->
                    DemoCard(
                        item = item,
                        onClick = {
                            AppStateManager.handleFeatureNavigation(item, onNavigate)
                        }
                    )
                }
            }
        }
    }
}

@Composable
public fun DemoCard(
    item: HomeFeatureItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Main left/bottom metadata indicator color
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(item.indicatorColor)
                    .align(Alignment.TopStart)
            )

            // Platform Availability Badges (Top Right corner)
            Row(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Android badge
                Box(
                    modifier = Modifier
                        .alpha(if (item.supportsAndroid) 1.0f else 0.2f)
                        .semantics {
                            contentDescription = if (item.supportsAndroid) "Android supported" else "Android not supported"
                        }
                ) {
                    Icon(
                        imageVector = Icons.AndroidFill,
                        contentDescription = "Android supported",
                        modifier = Modifier.size(18.dp),
                        tint = if (item.supportsAndroid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                }

                // Apple/iOS badge
                Box(
                    modifier = Modifier
                        .alpha(if (item.supportsIos) 1.0f else 0.2f)
                        .semantics {
                            contentDescription = if (item.supportsIos) "iOS supported" else "iOS not supported"
                        }
                ) {
                    Icon(
                        imageVector = Icons.AppleFill,
                        contentDescription = "iOS supported",
                        modifier = Modifier.size(18.dp),
                        tint = if (item.supportsIos) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                }
            }

            // Bottom titles layout
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
