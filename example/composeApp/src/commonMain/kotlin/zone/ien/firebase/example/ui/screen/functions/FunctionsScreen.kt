package zone.ien.firebase.example.ui.screen.functions

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.functions.FirebaseFunctions
import zone.ien.firebase.functions.FirebaseFunctionsException
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.IenBackButton
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenTopAppBar
import com.kyant.capsule.ContinuousRoundedRectangle

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

    val errorCodes = remember {
        listOf(
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
            "unauthenticated")
    }

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Cloud Functions Demo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IenBackButton(onClick = onBack)
                }
            )
        }
    ) { innerPadding ->
        if (initError != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IenText(
                        text = initError,
                        style = IenTheme.typography.body1,
                        color = IenTheme.colors.danger,
                        textAlign = TextAlign.Center
                    )
                    IenButton(onClick = onBack) {
                        IenText("Go Back")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                IenSurface(
                    shape = ContinuousRoundedRectangle(16.dp),
                    color = IenTheme.colors.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IenText(
                            text = "Callable Specification",
                            style = IenTheme.typography.title2,
                            color = IenTheme.colors.brand
                        )
                        IenDivider(color = IenTheme.colors.border)
                        IenText(
                            text = "Cloud Functions triggers a serverless endpoint. Specify a function name and payload to inspect dynamic call routing.",
                            style = IenTheme.typography.body1,
                            color = IenTheme.colors.textSecondary
                        )
                    }
                }

                IenTextField(
                    value = functionName,
                    onValueChange = { functionName = it },
                    label = "Function Name",
                    placeholder = "e.g. helloWorld",
                    modifier = Modifier.fillMaxWidth()
                )

                IenTextField(
                    value = parameterInput,
                    onValueChange = { parameterInput = it },
                    label = "Parameters (String/JSON)",
                    placeholder = "e.g. {\"key\": \"value\"}",
                    modifier = Modifier.fillMaxWidth()
                )

                IenText("Select Error Code to Simulate:", style = IenTheme.typography.label2)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    errorCodes.chunked(2).forEach { rowCodes ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowCodes.forEach { code ->
                                val isSelected = simulatedErrorCode == code
                                IenButton(
                                    onClick = { simulatedErrorCode = code },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    IenText(
                                        text = if (isSelected) "✓ $code" else code,
                                        style = IenTheme.typography.label2
                                    )
                                }
                            }
                        }
                    }
                }

                // Normal Call Function button
                IenButton(
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
                    IenText("Call Function")
                }

                // Error Simulation Trigger button
                IenButton(
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IenText("Call Function (Trigger Error)")
                }

                IenSurface(
                    shape = ContinuousRoundedRectangle(12.dp),
                    color = Color(0xFF1E1E1E),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
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
