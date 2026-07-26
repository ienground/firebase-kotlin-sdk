package zone.ien.firebase.example.ui.screen.database
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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.example.DatabaseTest

@Composable
fun DatabaseScreen(onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var logs by remember { mutableStateOf("Ready to run Database operations.") }
    var isRunning by remember { mutableStateOf(false) }

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Realtime Database Demo") },
                navigationIcon = { IenBackButton(onClick = onBack) }
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
            IenText(
                text = "Perform key write, sub-child auto-key push, remove, and child update operations via common Database API.",
                style = IenTheme.typography.body1,
                color = IenTheme.colors.textSecondary
            )

            IenButton(
                onClick = {
                    coroutineScope.launch {
                        isRunning = true
                        logs = "Running Database operations..."
                        try {
                            DatabaseTest.runTest()
                            logs = "Database operations executed successfully!\n\n1. Initialized FirebaseDatabase\n2. Set string value to 'test_path'\n3. Auto-pushed child element\n4. Deleted pushed child element\n5. Updated multiple children paths."
                        } catch (e: Exception) {
                            logs = "Operation failed:\n${e.message}"
                        } finally {
                            isRunning = false
                        }
                    }
                },
                state = IenButtonState(enabled = !isRunning),
                modifier = Modifier.fillMaxWidth()
            ) {
                IenText(if (isRunning) "Running Operations..." else "Run Database Test")
            }

            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp)
            ) {
                IenText(
                    text = logs,
                    modifier = Modifier.padding(16.dp),
                    style = IenTheme.typography.body2
                )
            }
        }
    }
}
