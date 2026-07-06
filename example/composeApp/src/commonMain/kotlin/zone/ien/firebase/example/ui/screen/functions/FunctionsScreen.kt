package zone.ien.firebase.example.ui.screen.functions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.functions.FirebaseFunctions
import zone.ien.firebase.functions.FirebaseFunctionsException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionsScreen(onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val initError = remember {
        if (!FirebaseApp.isInitialized) {
            "Firebase Core must be initialized first. Go to 'Firebase Init' screen."
        } else {
            runCatching { FirebaseFunctions.getInstance() }.exceptionOrNull()?.message
        }
    }

    var functionName by remember { mutableStateOf("helloWorld") }
    var parameterInput by remember { mutableStateOf("{\"name\": \"KMP User\"}") }
    var logText by remember { mutableStateOf("Ready to call Firebase Cloud Functions.") }
    var simulatedErrorCode by remember { mutableStateOf("permission-denied") }

    val errorCodes = remember { listOf(
        "unknown",
        "invalid-argument",
        "deadline-exceeded",
        "not-found",
        "permission-denied",
        "resource-exhausted",
        "failed-precondition",
        "aborted",
        "out_of_range",
        "unimplemented",
        "internal",
        "unavailable",
        "data-loss",
        "unauthenticated",
    ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cloud Functions Demo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },

            )
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
                    Text(
                        text = initError,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Button(onClick = onBack) {
                        Text("Go Back")
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
                        Text(
                            text = "Callable Specification",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                        Text(
                            text = "Cloud Functions triggers a serverless endpoint. Specify a function name and payload to inspect dynamic call routing.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                OutlinedTextField(
                    value = functionName,
                    onValueChange = { functionName = it },
                    label = { Text("Function Name") },
                    placeholder = { Text("e.g. helloWorld") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = parameterInput,
                    onValueChange = { parameterInput = it },
                    label = { Text("Parameters (String/JSON)") },
                    placeholder = { Text("e.g. {\"key\": \"value\"}") },
                    modifier = Modifier.fillMaxWidth()
                )

            // Simulated Error Code Selector Buttons
            Text("Select Error Code to Simulate:", style = MaterialTheme.typography.labelSmall)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                errorCodes.forEach { code ->
                    val isSelected = simulatedErrorCode == code
                    OutlinedButton(
                        onClick = { simulatedErrorCode = code },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                        )
                    ) {
                        Text(code, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            // Normal Call Function button
            Button(
                onClick = {
                    coroutineScope.launch {
                        logText = "Invoking callable function '$functionName'..."
                        try {
                            val functions = FirebaseFunctions.getInstance()
                            val callable = functions.getHttpsCallable(functionName)
                            val result = callable.call(parameterInput)
                            logText = "Invocation Success!\nResult Payload: ${result.data}"
                        } catch (e: FirebaseFunctionsException) {
                            logText = "Callable failed (FirebaseException):\n" +
                                    "Code: ${e.code}\n" +
                                    "Message: ${e.message}\n" +
                                    "Details: ${e.details}"
                        } catch (e: Exception) {
                            logText = "Callable failed (General):\n${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Call Function")
            }

            // Error Simulation Trigger button
            Button(
                onClick = {
                    coroutineScope.launch {
                        logText = "Triggering simulated validation error for '$simulatedErrorCode'..."
                        try {
                            val functions = FirebaseFunctions.getInstance()
                            val callable = functions.getHttpsCallable(functionName)
                            val result = callable.call("{\"trigger_error\": true, \"error_code\": \"$simulatedErrorCode\"}")
                            logText = "Success (Unexpected)? Result: ${result.data}"
                        } catch (e: FirebaseFunctionsException) {
                            logText = "Firebase Exception Caught!\n\n" +
                                    "Mapped Code: ${e.code}\n" +
                                    "Raw Message: ${e.message}\n" +
                                    "Details Payload: ${e.details}"
                        } catch (e: Exception) {
                            logText = "General Exception Caught:\n${e.message}"
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                    Text("Call Function (Trigger Error)")
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
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
}
