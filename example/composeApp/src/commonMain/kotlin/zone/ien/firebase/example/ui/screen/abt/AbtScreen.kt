package zone.ien.firebase.example.ui.screen.abt
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zone.ien.firebase.abt.AbtException
import zone.ien.firebase.abt.FirebaseABTesting

@Composable
fun AbtScreen(
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    var logMessage by remember { mutableStateOf("Ready to verify A/B Testing components.") }

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("A/B Testing", fontWeight = FontWeight.Bold) },
                navigationIcon = { IenBackButton(onClick = onNavigateBack) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "KMP A/B Testing Verification",
                        style = IenTheme.typography.title2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        text = "This screen validates the compiled binding classes and exceptions for the ':firebase-abt' KMP wrapper.",
                        style = IenTheme.typography.body1
                    )
                }
            }

            IenButton(
                onClick = {
                    try {
                        logMessage = "Verifying KMP classes...\n"
                        val abtClass = FirebaseABTesting::class
                        val simpleName = abtClass.simpleName ?: "FirebaseABTesting (Native ObjC Class)"
                        logMessage += "FirebaseABTesting KClass resolved successfully!\n"
                        logMessage += "Simple Name: $simpleName\n"
                        logMessage += "Reflect qualifiedName on iOS: ${abtClass.qualifiedName ?: "null (Kotlin/Native Reflection Limitation)"}\n"
                    } catch (e: Exception) {
                        logMessage += "Verification Failed: ${e.message}"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                IenText("Verify SDK Classes")
            }

            IenButton(
                onClick = {
                    try {
                        throw AbtException("Test AbtException message")
                    } catch (e: AbtException) {
                        logMessage = "AbtException Successfully Caught!\nMessage: ${e.message}"
                    } catch (e: Exception) {
                        logMessage = "Caught generic exception: ${e.message}"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                IenText("Trigger and Catch AbtException")
            }

            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "Console Output Logs",
                        style = IenTheme.typography.label1,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        text = logMessage,
                        style = IenTheme.typography.body1,
                        color = IenTheme.colors.textPrimary
                    )
                }
            }
        }
    }
}
