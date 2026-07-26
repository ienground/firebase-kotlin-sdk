package zone.ien.firebase.example.ui.screen.storage
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.kyant.capsule.ContinuousRoundedRectangle
import firebase_kotlin_sdk.example.composeapp.generated.resources.Res
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.storage.FirebaseStorage

@Composable
fun StorageScreen(onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    val initError = remember {
        if (!FirebaseApp.isInitialized) {
            "Firebase Core must be initialized first. Go to 'Firebase Init' screen."
        } else {
            runCatching { FirebaseStorage.getInstance() }.exceptionOrNull()?.message
        }
    }
    
    var pathInput by remember { mutableStateOf("images/sample.jpg") }
    var uploadInput by remember { mutableStateOf("Hello Firebase Storage KMP!") }
    var logText by remember { mutableStateOf("Ready to inspect Firebase Storage.") }

    // Core Reference Metadata properties derived reactively from pathInput
    val reference = remember(pathInput, initError) {
        if (initError == null) {
            runCatching {
                val storage = FirebaseStorage.getInstance()
                storage.reference.child(pathInput)
            }.getOrNull()
        } else {
            null
        }
    }

    val refName = reference?.name ?: "-"
    val refPath = reference?.path ?: "-"
    val refBucket = reference?.bucket ?: "-"
    val hasParent = reference?.parent != null

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Cloud Storage Demo") },
                navigationIcon = { IenBackButton(onClick = onBack) })
        }
    ) { padding ->
        if (initError != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    IenText(
                        text = initError,
                        style = IenTheme.typography.body1,
                        color = IenTheme.colors.danger,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    IenButton(onClick = onBack) {
                        IenText("Go Back")
                    }
                }
            }
        } else {
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
                IenSurface(
                    color = IenTheme.colors.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IenText("Storage Reference Meta", style = IenTheme.typography.title2, color = IenTheme.colors.brand)
                        IenDivider(color = IenTheme.colors.textSecondary.copy(alpha = 0.2f))
                        MetadataRow("Reference Name", refName)
                        MetadataRow("Full Path", refPath)
                        MetadataRow("Bucket Name", refBucket)
                        MetadataRow("Has Parent Reference", if (hasParent) "Yes" else "No (Root)")
                    }
                }

                // Path configuration input
                IenTextField(
                    value = pathInput,
                    onValueChange = { pathInput = it },
                    label = "Reference Child Path",
                    placeholder = "e.g. images/sample.jpg",
                    modifier = Modifier.fillMaxWidth()
                )

                // Content payload input
                IenTextField(
                    value = uploadInput,
                    onValueChange = { uploadInput = it },
                    label = "Content to Upload",
                    modifier = Modifier.fillMaxWidth()
                )

                // Upload action button
                IenButton(
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
                    IenText("Upload Data")
                }

                // Upload Sample Image button
                IenButton(
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
                    
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IenText("Upload Sample Image")
                }

                // Storage Operations
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IenButton(
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
                        IenText("Get URL")
                    }

                    IenButton(
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
                        
                        modifier = Modifier.weight(1f)
                    ) {
                        IenText("Delete")
                    }
                }

                // Logging window
                IenSurface(color = IenTheme.colors.surfaceVariant, 
                    shape = ContinuousRoundedRectangle(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        IenText(
                            text = "Console Output Log",
                            style = IenTheme.typography.label2,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        IenText(
                            text = logText,
                            style = IenTheme.typography.body1,
                            color = Color.Green
                        )
                    }
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
        IenText(text = label, style = IenTheme.typography.body1, color = IenTheme.colors.textSecondary)
        IenText(text = value, style = IenTheme.typography.body1, color = IenTheme.colors.textPrimary)
    }
}
