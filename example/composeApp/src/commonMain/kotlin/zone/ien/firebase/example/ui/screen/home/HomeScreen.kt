package zone.ien.firebase.example.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zone.ien.firebase.abt.FirebaseABTesting
import zone.ien.firebase.annotations.PreviewApi
import zone.ien.firebase.example.ui.navigation.ScreenRoute
import zone.ien.firebase.storage.FirebaseStorage

@PreviewApi
@Composable
fun HomeScreen(onNavigate: (ScreenRoute) -> Unit) {
    val verifyAbtImport: FirebaseABTesting? = null
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
        modifier = Modifier
            .fillMaxSize()
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
                item {
                    DemoCard(
                        title = "Firebase Init",
                        subtitle = "Setup and check core configuration status.",
                        indicatorColor = Color(0xFFF58220),
                        onClick = { onNavigate(ScreenRoute.FirebaseInit) }
                    )
                }
                item {
                    DemoCard(
                        title = "Firestore DB",
                        subtitle = "Real-time read/write collection stream.",
                        indicatorColor = Color(0xFFFFCA28),
                        onClick = { onNavigate(ScreenRoute.Firestore) }
                    )
                }
                item {
                    DemoCard(
                        title = "Analytics",
                        subtitle = "Placeholder feature.",
                        indicatorColor = Color(0xFF039BE5),
                        onClick = { onNavigate(ScreenRoute.AnalyticsPlaceholder) }
                    )
                }
                item {
                    DemoCard(
                        title = "Messaging",
                        subtitle = "Placeholder feature.",
                        indicatorColor = Color(0xFF80CBC4),
                        onClick = { onNavigate(ScreenRoute.MessagingPlaceholder) }
                    )
                }
                item {
                    DemoCard(
                        title = "Cloud Storage",
                        subtitle = "Upload, download, delete and retrieve URLs.",
                        indicatorColor = Color(0xFF00B0FF),
                        onClick = { onNavigate(ScreenRoute.Storage) }
                    )
                }
                item {
                    DemoCard(
                        title = "Cloud Functions",
                        subtitle = "Invoke serverless HTTPS callable trigger function.",
                        indicatorColor = Color(0xFFFF5722),
                        onClick = { onNavigate(ScreenRoute.Functions) }
                    )
                }
                item {
                    DemoCard(
                        title = "Realtime Database",
                        subtitle = "Write, push, remove and update database nodes.",
                        indicatorColor = Color(0xFFFFCA28),
                        onClick = { onNavigate(ScreenRoute.Database) }
                    )
                }
                item {
                    DemoCard(
                        title = "Database Collection",
                        subtitle = "Test local ImmutableSortedMap insertion, sorting and removal.",
                        indicatorColor = Color(0xFF4CAF50),
                        onClick = { onNavigate(ScreenRoute.DatabaseCollection) }
                    )
                }
                item {
                    DemoCard(
                        title = "Play Integrity",
                        subtitle = "Android Play Integrity App Check provider verification.",
                        indicatorColor = Color(0xFFE91E63),
                        onClick = { onNavigate(ScreenRoute.PlayIntegrity) }
                    )
                }
            }
        }
    }
}

@Composable
fun DemoCard(
    title: String,
    subtitle: String,
    indicatorColor: Color,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(indicatorColor)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
