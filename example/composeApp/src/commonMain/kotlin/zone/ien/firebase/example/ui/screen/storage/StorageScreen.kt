package zone.ien.firebase.example.ui.screen.storage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import firebase_kotlin_sdk.example.composeapp.generated.resources.Res
import kotlinx.coroutines.launch
import zone.ien.firebase.storage.FirebaseStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageScreen(onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    var pathInput by remember { mutableStateOf("images/sample.jpg") }
    var uploadInput by remember { mutableStateOf("Hello Firebase Storage KMP!") }
    var logText by remember { mutableStateOf("Ready to inspect Firebase Storage.") }
    
    // Core Reference Metadata properties
    var refName by remember { mutableStateOf("-") }
    var refPath by remember { mutableStateOf("-") }
    var refBucket by remember { mutableStateOf("-") }
    var hasParent by remember { mutableStateOf(false) }

    fun updateMetadata(path: String) {
        try {
            val storage = FirebaseStorage.getInstance()
            val reference = storage.reference.child(path)
            refName = reference.name
            refPath = reference.path
            refBucket = reference.bucket
            hasParent = reference.parent != null
            logText = "Successfully retrieved metadata for reference at path: '$path'"
        } catch (e: Exception) {
            logText = "Error during metadata lookup: ${e.message}"
            refName = "-"
            refPath = "-"
            refBucket = "-"
            hasParent = false
        }
    }

    // Perform initial metadata lookup on start
    LaunchedEffect(pathInput) {
        updateMetadata(pathInput)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cloud Storage Demo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Reference metadata card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Storage Reference Meta", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                    MetadataRow("Reference Name", refName)
                    MetadataRow("Full Path", refPath)
                    MetadataRow("Bucket Name", refBucket)
                    MetadataRow("Has Parent Reference", if (hasParent) "Yes" else "No (Root)")
                }
            }

            // Path configuration input
            OutlinedTextField(
                value = pathInput,
                onValueChange = { pathInput = it },
                label = { Text("Reference Child Path") },
                placeholder = { Text("e.g. images/sample.jpg") },
                modifier = Modifier.fillMaxWidth()
            )

            // Content payload input
            OutlinedTextField(
                value = uploadInput,
                onValueChange = { uploadInput = it },
                label = { Text("Content to Upload") },
                modifier = Modifier.fillMaxWidth()
            )

            // Upload action button
            Button(
                onClick = {
                    coroutineScope.launch {
                        logText = "Uploading payload data..."
                        try {
                            val storage = FirebaseStorage.getInstance()
                            val ref = storage.reference.child(pathInput)
                            ref.putBytes(uploadInput.encodeToByteArray())
                            logText = "Successfully uploaded payload to '$pathInput'!"
                        } catch (e: Exception) {
                            logText = "Upload failed: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Data")
            }

            // Upload Sample Image button
            Button(
                onClick = {
                    coroutineScope.launch {
                        logText = "Loading sample_image.png from app resources..."
                        try {
                            val imageBytes = Res.readBytes("files/sample_image.png")
                            logText = "Successfully loaded image (${imageBytes.size} bytes). Uploading..."
                            
                            val storage = FirebaseStorage.getInstance()
                            val ref = storage.reference.child("images/uploaded_sample.png")
                            ref.putBytes(imageBytes)
                            logText = "Successfully uploaded sample image to 'images/uploaded_sample.png'!"
                        } catch (e: Exception) {
                            logText = "Image upload failed:\n${e.message}\n\n(Please ensure 'sample_image.png' is placed inside 'composeResources/files/')"
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Sample Image")
            }

            // Storage Operations
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            logText = "Fetching download URL..."
                            try {
                                val storage = FirebaseStorage.getInstance()
                                val ref = storage.reference.child(pathInput)
                                val url = ref.getDownloadUrl()
                                logText = "Download URL: $url"
                            } catch (e: Exception) {
                                logText = "Failed to fetch download URL:\n${e.message}"
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Get URL")
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            logText = "Deleting file..."
                            try {
                                val storage = FirebaseStorage.getInstance()
                                val ref = storage.reference.child(pathInput)
                                ref.delete()
                                logText = "Successfully deleted file at '$pathInput'"
                            } catch (e: Exception) {
                                logText = "Failed to delete file:\n${e.message}"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }
            }

            // Logging window
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Console Output Log",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = logText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Green
                    )
                }
            }
        }
    }
}

@Composable
private fun MetadataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}
