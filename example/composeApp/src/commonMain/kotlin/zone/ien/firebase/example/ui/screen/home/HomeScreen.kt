package zone.ien.firebase.example.ui.screen.home
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.interactive.IenTextFieldState
import zone.ien.utils.ui.interactive.IenSwitch
import zone.ien.utils.ui.interactive.IenCircleCheckbox
import zone.ien.utils.ui.interactive.IenDotCheckbox
import zone.ien.utils.ui.feedback.IenCircularProgressIndicator
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenTopAppBar
import zone.ien.utils.ui.screen.IenBackButton
import zone.ien.utils.ui.wrapper.IenRootWrapper
import zone.ien.utils.ui.dialog.IenConfirmDialog
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.TextStyle

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import firebase_kotlin_sdk.example.composeapp.generated.resources.Res
import firebase_kotlin_sdk.example.composeapp.generated.resources.firebase_kmp
import org.jetbrains.compose.resources.painterResource
import zone.ien.firebase.annotations.PreviewApi
import zone.ien.firebase.example.data.AppStateManager
import zone.ien.firebase.example.data.HomeFeatureItem
import zone.ien.firebase.example.icon.AndroidFill
import zone.ien.firebase.example.icon.AppleFill
import zone.ien.firebase.example.icon.GithubFill
import zone.ien.firebase.example.icon.Icons
import zone.ien.firebase.example.ui.navigation.ScreenRoute
import zone.ien.firebase.example.util.isIos
import zone.ien.firebase.example.util.libraryVersion
import com.kyant.capsule.ContinuousRoundedRectangle

@PreviewApi
@Composable
public fun HomeScreen(
    onNavigate: (ScreenRoute) -> Unit
) {
    val uriHandler = LocalUriHandler.current

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
            subtitle = "Document Read/Write & where, orderBy, limit query test.",
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
            supportsIos = true,
            route = ScreenRoute.ModelDownloader,
            isIosSimulated = true
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
            supportsIos = true,
            route = ScreenRoute.AiLogic,
            isIosSimulated = true
        ),
        HomeFeatureItem(
            title = "AI On-Device",
            subtitle = "Hybrid on-device Gemini inference and fallback.",
            indicatorColor = Color(0xFFE040FB),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.AiLogicOnDevice,
            isIosSimulated = true
        ),
        HomeFeatureItem(
            title = "Datatransport",
            subtitle = "Telemetry datatransport priority & scheduling contract test.",
            indicatorColor = Color(0xFF3F51B5),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Datatransport,
            isIosSimulated = true
        ),
        HomeFeatureItem(
            title = "App Distribution",
            subtitle = "Check prerelease availability and trigger in-app updates.",
            indicatorColor = Color(0xFFF58220),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.AppDistribution
        ),
        HomeFeatureItem(
            title = "Data Connect",
            subtitle = "Configure Postgres database GraphQL query connector bootstrap.",
            indicatorColor = Color(0xFF673AB7),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.DataConnect,
            isIosSimulated = true
        ),
        HomeFeatureItem(
            title = "In-App Messaging",
            subtitle = "Trigger contextual campaigns and control display suppression.",
            indicatorColor = Color(0xFFE91E63),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.InAppMessaging
        ),
        HomeFeatureItem(
            title = "Encoders",
            subtitle = "Verify foundational encoding interface serialization contract.",
            indicatorColor = Color(0xFF4CAF50),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Encoders
        ),
        HomeFeatureItem(
            title = "A/B Testing",
            subtitle = "Verify experiment controllers and lifecycle events.",
            indicatorColor = Color(0xFF9C27B0),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.Abt
        ),
        HomeFeatureItem(
            title = "Sessions",
            subtitle = "Track background session telemetry automatically (Internal-only). Click to verify on Init screen.",
            indicatorColor = Color(0xFF607D8B),
            supportsAndroid = true,
            supportsIos = true,
            route = ScreenRoute.FirebaseInit
        )
    )

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = {
                    IenText(
                        text = "Firebase KMP Examples",
                        style = IenTheme.typography.title1,
                        color = IenTheme.colors.textPrimary
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
                // 1. Library Info Card spanning across 2 columns
                item(span = { GridItemSpan(2) }) {
                    IenSurface(color = IenTheme.colors.surfaceVariant, 
                        
                        
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ContinuousRoundedRectangle(16.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.firebase_kmp),
                                contentDescription = "Firebase KMP Logo",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            IenText(
                                text = "Firebase Kotlin SDK",
                                style = IenTheme.typography.title1,
                                fontWeight = FontWeight.Bold,
                                color = IenTheme.colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                             IenText(
                                text = "v$libraryVersion",
                                style = IenTheme.typography.body1,
                                color = IenTheme.colors.textPrimary.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            IenButton(
                                onClick = {
                                    uriHandler.openUri("https://github.com/ienground/firebase-kotlin-sdk")
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IenIcon(
                                        imageVector = Icons.GithubFill,
                                        contentDescription = "Github Logo",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    IenText("Github")
                                }
                            }
                        }
                    }
                }

                // 2. Grid Items
                items(items) { item ->
                    DemoIenSurface(
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
public fun DemoIenSurface(
    item: HomeFeatureItem,
    onClick: () -> Unit
) {
    IenSurface(
        color = IenTheme.colors.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(ContinuousRoundedRectangle(16.dp))
            .clickable { onClick() }
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
                    .clip(ContinuousRoundedRectangle(7.dp))
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
                    IenIcon(
                        imageVector = Icons.AndroidFill,
                        contentDescription = "Android supported",
                        modifier = Modifier.size(18.dp),
                        tint = if (item.supportsAndroid) IenTheme.colors.brand else IenTheme.colors.textPrimary.copy(alpha = 0.2f)
                    )
                }

                // Apple/iOS badge
                Box(
                    modifier = Modifier
                        .alpha(if (item.supportsIos) 1.0f else 0.2f)
                        .semantics {
                            contentDescription = if (item.supportsIos) {
                                if (item.isIosSimulated) "iOS simulated support" else "iOS supported"
                            } else {
                                "iOS not supported"
                            }
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        IenIcon(
                            imageVector = Icons.AppleFill,
                            contentDescription = "iOS supported",
                            modifier = Modifier.size(18.dp),
                            tint = if (item.supportsIos) {
                                if (item.isIosSimulated) Color(0xFFFFB300) else IenTheme.colors.brand
                            } else {
                                IenTheme.colors.textPrimary.copy(alpha = 0.2f)
                            }
                        )
                        if (item.supportsIos && item.isIosSimulated) {
                            IenText(
                                text = "Sim",
                                style = IenTheme.typography.label2,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFB300)
                            )
                        }
                    }
                }
            }

            // Bottom titles layout
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                IenText(
                    text = item.title,
                    style = IenTheme.typography.title2,
                    color = IenTheme.colors.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                IenText(
                    text = item.subtitle,
                    style = IenTheme.typography.body2,
                    color = IenTheme.colors.textPrimary.copy(alpha = 0.6f)
                )
            }
        }
    }
}
