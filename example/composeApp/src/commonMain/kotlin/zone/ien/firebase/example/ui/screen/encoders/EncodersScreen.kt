package zone.ien.firebase.example.ui.screen.encoders
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
import androidx.compose.foundation.verticalScroll
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
import zone.ien.firebase.encoders.annotations.Encodable
import zone.ien.firebase.encoders.json.JsonDataEncoderBuilder
import zone.ien.firebase.encoders.reflective.ReflectiveObjectEncoder
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.utils.ui.wrapper.IenRootWrapper
import com.kyant.capsule.ContinuousRoundedRectangle

// Mock Annotation to verify FieldDescriptor property generic constraint
annotation class ProtoDescriptor(val tag: Int)

@Encodable
data class UserProfile(val username: String, val age: Int, val isPremium: Boolean, val hobby: String?)

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

    IenRootWrapper {
        AppTheme {
            IenScaffold(
                topBar = {
                    IenTopAppBar(
                        title = { IenText("Encoders Core Contract") },
                        navigationIcon = { IenBackButton(onClick = onBack) }
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
                    IenText(
                        text = "JSON & Reflective Serialization Verification",
                        style = IenTheme.typography.title2
                    )

                    IenText(
                        text = "This screen executes the newly created KMP modules. It registers standard builders, verifies reflective field discovery on JVM, and tests explicit registration fallback on iOS.",
                        style = IenTheme.typography.body1
                    )

                    IenButton(
                        onClick = {
                            try {
                                log("--- Initiating Serialization Tests ---")
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

                                log("5. Executing ReflectiveObjectEncoder with explicit fallback registration...")
                                // Registering manual encoder for iOS capability limits fallback
                                ReflectiveObjectEncoder.registerEncoderExplicit(UserProfile::class, userProfileEncoder)

                                val reflectiveEncoder = ReflectiveObjectEncoder<UserProfile>()
                                val reflectiveJsonEncoder = JsonDataEncoderBuilder()
                                    .registerEncoder(UserProfile::class, reflectiveEncoder)
                                    .build()
                                
                                val reflectiveJsonResult = reflectiveJsonEncoder.encode(profile)
                                log("Result 3 (Reflective Auto/Fallback): $reflectiveJsonResult")
                                
                                log("--- Verification finished successfully ---")
                            } catch (e: Exception) {
                                log("Serialization failed: ${e.message}")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Run Serialization Sim")
                    }

                    IenText(
                        text = "Simulation Console Logs",
                        style = IenTheme.typography.title3
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(ContinuousRoundedRectangle(8.dp))
                            .background(Color.Black.copy(alpha = 0.05f))
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (logs.isEmpty()) {
                            IenText("Press Run to initiate JSON output simulation.", color = Color.Gray, style = IenTheme.typography.body2)
                        } else {
                            logs.forEach { logLine ->
                                IenText("> $logLine", color = IenTheme.colors.brand, style = IenTheme.typography.body2)
                            }
                        }
                    }

                    IenButton(
                        onClick = { logs.clear() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        IenText("Clear Logs")
                    }
                }
            }
        }
    }
}
