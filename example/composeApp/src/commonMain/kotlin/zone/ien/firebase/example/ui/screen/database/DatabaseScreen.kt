package zone.ien.firebase.example.ui.screen.database

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.example.DatabaseTest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseScreen(onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var logs by remember { mutableStateOf("Ready to run Database operations.") }
    var isRunning by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Realtime Database Demo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Perform key write, sub-child auto-key push, remove, and child update operations via common Database API.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
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
                enabled = !isRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isRunning) "Running Operations..." else "Run Database Test")
            }

            Card(
                modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp)
            ) {
                Text(
                    text = logs,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
