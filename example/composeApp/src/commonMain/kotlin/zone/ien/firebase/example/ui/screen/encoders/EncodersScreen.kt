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
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.utils.ui.wrapper.M3RootWrapper

// Mock Annotation to verify FieldDescriptor property generic constraint
annotation class ProtoDescriptor(val tag: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncodersScreen(
    onBack: () -> Unit
) {
    val logs = remember { mutableStateListOf<String>() }

    fun log(msg: String) {
        logs.add(msg)
    }

    // Mock Payload Model to test serialization contract
    data class UserProfile(val username: String, val age: Int, val isPremium: Boolean)

    // Standard ObjectEncoder implementation
    val userProfileEncoder = remember {
        object : ObjectEncoder<UserProfile> {
            override fun encode(value: UserProfile, context: ObjectEncoderContext) {
                context.add("username", value.username)
                context.add("age", value.age)
                context.add("isPremium", value.isPremium)
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
                        text = "Core API Contract Simulation",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "This screen verifies that foundational serialization models (FieldDescriptor, ObjectEncoder, and Contexts) compile and execute correctly across shared Kotlin Multiplatform targets.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Button(
                        onClick = {
                            try {
                                log("--- Starting Encoder Sim ---")
                                log("1. Constructing FieldDescriptor schema metadata...")
                                
                                val desc = FieldDescriptor.builder("profile")
                                    .withProperty(ProtoDescriptor(tag = 42))
                                    .build()
                                    
                                val tagVal = desc.getProperty(ProtoDescriptor::class)?.tag
                                log("Descriptor tag created: ID=${desc.name}, ProtoTag=$tagVal")

                                val profile = UserProfile("kmp_developer", 25, true)
                                log("2. Initializing mock encoder context...")
                                
                                val mockContext = object : ObjectEncoderContext {
                                    override fun add(name: String, value: Any?): ObjectEncoderContext {
                                        log("Context -> Add Object [key=$name, value=$value]")
                                        return this
                                    }
                                    override fun add(name: String, value: Double): ObjectEncoderContext = add(name, value)
                                    override fun add(name: String, value: Int): ObjectEncoderContext = add(name, value)
                                    override fun add(name: String, value: Long): ObjectEncoderContext = add(name, value)
                                    override fun add(name: String, value: Boolean): ObjectEncoderContext = add(name, value)
                                    override fun add(name: String, value: Float): ObjectEncoderContext = add(name, value)

                                    override fun add(field: FieldDescriptor, value: Any?): ObjectEncoderContext = add(field.name, value)
                                    override fun add(field: FieldDescriptor, value: Double): ObjectEncoderContext = add(field.name, value)
                                    override fun add(field: FieldDescriptor, value: Int): ObjectEncoderContext = add(field.name, value)
                                    override fun add(field: FieldDescriptor, value: Long): ObjectEncoderContext = add(field.name, value)
                                    override fun add(field: FieldDescriptor, value: Boolean): ObjectEncoderContext = add(field.name, value)
                                    override fun add(field: FieldDescriptor, value: Float): ObjectEncoderContext = add(field.name, value)

                                    override fun inline(value: Any?): ObjectEncoderContext {
                                        log("Context -> Inline Object [value=$value]")
                                        return this
                                    }

                                    override fun nested(name: String): ObjectEncoderContext {
                                        log("Context -> Nested Object by name: $name")
                                        return this
                                    }

                                    override fun nested(field: FieldDescriptor): ObjectEncoderContext {
                                        log("Context -> Nested Object by field: ${field.name}")
                                        return this
                                    }
                                }

                                log("3. Dispatched UserProfile structure to Encoder...")
                                userProfileEncoder.encode(profile, mockContext)
                                log("--- Simulation successfully finished ---")
                            } catch (e: Exception) {
                                log("Simulation failed: ${e.message}")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Run Serialization Sim")
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
                            Text("Press Run to initiate simulation.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
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
