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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

@PreviewApi
@Composable
public fun HomeScreen(onNavigate: (ScreenRoute) -> Unit) {
    val items = listOf(
        HomeFeatureItem(
            title = "Firebase Init",
            subtitle = "Setup and check core configuration status.",
            indicatorColor = Color(0xFFF58220),
            route = ScreenRoute.FirebaseInit,
            supportsAndroid = true,
            supportsIos = true,
            requiresInitialization = false
        ),
        HomeFeatureItem(
            title = "Firestore DB",
            subtitle = "Real-time read/write collection stream.",
            indicatorColor = Color(0xFFFFCA28),
            route = ScreenRoute.Firestore,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Analytics",
            subtitle = "Placeholder feature.",
            indicatorColor = Color(0xFF039BE5),
            route = ScreenRoute.AnalyticsPlaceholder,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Messaging",
            subtitle = "FCM token, topic subscribe & client delegation.",
            indicatorColor = Color(0xFF80CBC4),
            route = ScreenRoute.MessagingPlaceholder,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Cloud Storage",
            subtitle = "Upload, download, delete and retrieve URLs.",
            indicatorColor = Color(0xFF00B0FF),
            route = ScreenRoute.Storage,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Cloud Functions",
            subtitle = "Invoke serverless HTTPS callable trigger function.",
            indicatorColor = Color(0xFFFF5722),
            route = ScreenRoute.Functions,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Realtime Database",
            subtitle = "Write, push, remove and update database nodes.",
            indicatorColor = Color(0xFFFFCA28),
            route = ScreenRoute.Database,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Database Collection",
            subtitle = "Test local ImmutableSortedMap insertion, sorting and removal.",
            indicatorColor = Color(0xFF4CAF50),
            route = ScreenRoute.DatabaseCollection,
            supportsAndroid = true,
            supportsIos = true,
            requiresInitialization = false
        ),
        HomeFeatureItem(
            title = "Play Integrity",
            subtitle = "Android Play Integrity App Check provider verification.",
            indicatorColor = Color(0xFFE91E63),
            route = ScreenRoute.PlayIntegrity,
            supportsAndroid = true,
            supportsIos = false
        ),
        HomeFeatureItem(
            title = "Crashlytics",
            subtitle = "Log, set metadata, non-fatal and test fatal app crashes.",
            indicatorColor = Color(0xFFFF5722),
            route = ScreenRoute.Crashlytics,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Authentication",
            subtitle = "Anonymous & Email/Password session management.",
            indicatorColor = Color(0xFF00BCD4),
            route = ScreenRoute.Auth,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Performance",
            subtitle = "Manual custom trace and HTTP metric logging.",
            indicatorColor = Color(0xFF673AB7),
            route = ScreenRoute.Performance,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Installations",
            subtitle = "Query unique installation ID and auth token.",
            indicatorColor = Color(0xFF009688),
            route = ScreenRoute.Installations,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Model Downloader",
            subtitle = "Retrieve custom models and monitor status.",
            indicatorColor = Color(0xFF4CAF50),
            route = ScreenRoute.ModelDownloader,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Remote Config",
            subtitle = "Fetch, activate, and listen for dynamic parameters.",
            indicatorColor = Color(0xFFFF9800),
            route = ScreenRoute.RemoteConfig,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "AI Logic",
            subtitle = "Generate content using Gemini model prompts.",
            indicatorColor = Color(0xFF9C27B0),
            route = ScreenRoute.AiLogic,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "AI On-Device",
            subtitle = "Hybrid on-device Gemini inference and fallback.",
            indicatorColor = Color(0xFFE040FB),
            route = ScreenRoute.AiLogicOnDevice,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Datatransport",
            subtitle = "Telemetry datatransport priority & scheduling contract test.",
            indicatorColor = Color(0xFF3F51B5),
            route = ScreenRoute.Datatransport,
            supportsAndroid = true,
            supportsIos = true,
            requiresInitialization = false
        ),
        HomeFeatureItem(
            title = "App Distribution",
            subtitle = "Check prerelease availability and trigger in-app updates.",
            indicatorColor = Color(0xFFF58220),
            route = ScreenRoute.AppDistribution,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Data Connect",
            subtitle = "Configure Postgres database GraphQL query connector bootstrap.",
            indicatorColor = Color(0xFF673AB7),
            route = ScreenRoute.DataConnect,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "In-App Messaging",
            subtitle = "Trigger contextual campaigns and control display suppression.",
            indicatorColor = Color(0xFFE91E63),
            route = ScreenRoute.InAppMessaging,
            supportsAndroid = true,
            supportsIos = true
        ),
        HomeFeatureItem(
            title = "Encoders",
            subtitle = "Verify foundational encoding interface serialization contract.",
            indicatorColor = Color(0xFF4CAF50),
            route = ScreenRoute.Encoders,
            supportsAndroid = true,
            supportsIos = true,
            requiresInitialization = false
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Firebase KMP Hub",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Compose Multiplatform Demo Framework",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items) { item ->
                    DemoCard(
                        item = item,
                        onClick = { AppStateManager.handleFeatureNavigation(item, onNavigate) }
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
            containerColor = MaterialTheme.colorScheme.surface
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
                        contentDescription = null,
                        modifier = Modifier
                            .background(
                                color = if (item.supportsAndroid) Color(0xFFE8F5E9) else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .size(14.dp)
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
                        contentDescription = null,
                        modifier = Modifier
                            .background(
                                color = if (item.supportsIos) Color(0xFFECEFF1) else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .size(14.dp)
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
