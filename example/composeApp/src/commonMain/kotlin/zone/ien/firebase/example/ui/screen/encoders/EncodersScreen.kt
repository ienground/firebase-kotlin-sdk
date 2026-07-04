package zone.ien.firebase.example.ui.screen.encoders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zone.ien.firebase.encoders.FieldDescriptor
import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ObjectEncoderContext
import zone.ien.firebase.encoders.json.JsonDataEncoderBuilder
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.utils.ui.wrapper.M3RootWrapper

import zone.ien.firebase.encoders.annotations.Encodable

// Mock Annotation to verify FieldDescriptor property generic constraint
annotation class ProtoDescriptor(val tag: Int)

@Encodable
data class UserProfile(val username: String, val age: Int, val isPremium: Boolean, val hobby: String?)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncodersScreen(
    onBack: () -> Unit
) {
    val logs = remember { mutableStateListOf<String>() }

    fun log(msg: String) {
        logs.add(msg)
    }

    // Standard ObjectEncoder implementation
    val userProfileEncoder = remember {
        object : ObjectEncoder<UserProfile> {
            override fun encode(value: UserProfile, context: ObjectEncoderContext) {
                context.add("username", value.username)
                context.add("age", value.age)
                context.add("isPremium", value.isPremium)
                context.add("hobby", value.hobby)
            }
        }
    }

    M3RootWrapper {
        AppTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Encoders Core Contract") },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Text(
                                    text = "←",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "JSON Serialization Verification",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "This screen executes the newly created 'firebase-encoders-json' KMP module. It registers a custom object encoder, builds the JSON DataEncoder, and materializes real outputs dynamically.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Button(
                        onClick = {
                            try {
                                log("--- Initiating JSON Serialization ---")
                                log("1. Creating FieldDescriptor with metadata tag...")
                                val desc = FieldDescriptor.builder("profile")
                                    .withProperty(ProtoDescriptor(tag = 77))
                                    .build()
                                val tagVal = desc.getProperty(ProtoDescriptor::class)?.tag
                                log("Schema tag resolved successfully: Tag=$tagVal")

                                val profile = UserProfile("kmp_developer", 25, true, null)
                                log("2. Payload setup: $profile")

                                log("3. Building JsonDataEncoder (keeping null fields)...")
                                val encoderWithNull = JsonDataEncoderBuilder()
                                    .registerEncoder(UserProfile::class, userProfileEncoder)
                                    .ignoreNullValues(false)
                                    .build()
                                val jsonResult1 = encoderWithNull.encode(profile)
                                log("Result 1 (Standard): $jsonResult1")

                                log("4. Building JsonDataEncoder (ignoring null fields)...")
                                val encoderIgnoreNull = JsonDataEncoderBuilder()
                                    .registerEncoder(UserProfile::class, userProfileEncoder)
                                    .ignoreNullValues(true)
                                    .build()
                                val jsonResult2 = encoderIgnoreNull.encode(profile)
                                log("Result 2 (IgnoreNull): $jsonResult2")
                                
                                log("--- Verification finished successfully ---")
                            } catch (e: Exception) {
                                log("Serialization failed: ${e.message}")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Run JSON Encoder Test")
                    }

                    Text(
                        text = "Simulation Console Logs",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.05f))
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (logs.isEmpty()) {
                            Text("Press Run to initiate JSON output simulation.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        } else {
                            logs.forEach { logLine ->
                                Text("> $logLine", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    Button(
                        onClick = { logs.clear() },
                        colors = ButtonDefaults.textButtonColors(),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Clear Logs")
                    }
                }
            }
        }
    }
}
