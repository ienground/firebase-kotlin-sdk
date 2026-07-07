package zone.ien.firebase.example.ui.screen.perf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zone.ien.firebase.perf.FirebasePerformance
import zone.ien.firebase.perf.Trace
import zone.ien.firebase.perf.HttpMetric

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceScreen(
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    var isCollectionEnabled by remember { mutableStateOf(FirebasePerformance.instance.isPerformanceCollectionEnabled) }
    var activeTrace by remember { mutableStateOf<Trace?>(null) }
    var traceName by remember { mutableStateOf("sample_custom_trace") }
    var metricName by remember { mutableStateOf("sample_metric") }
    var metricIncrement by remember { mutableStateOf("1") }
    var attributeKey by remember { mutableStateOf("sample_key") }
    var attributeValue by remember { mutableStateOf("sample_value") }

    // HTTP Metric states
    var httpUrl by remember { mutableStateOf("https://example.com/api/data") }
    var httpMethod by remember { mutableStateOf("GET") }
    var httpResponseCode by remember { mutableStateOf("200") }
    var activeHttpMetric by remember { mutableStateOf<HttpMetric?>(null) }

    var logMessage by remember { mutableStateOf("Ready to test manual performance monitoring API.") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance Monitoring", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                },

            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                )
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Config Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Config & Status",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Performance Collection Enabled:",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Switch(
                            checked = isCollectionEnabled,
                            onCheckedChange = { checked ->
                                FirebasePerformance.instance.isPerformanceCollectionEnabled = checked
                                isCollectionEnabled = checked
                                logMessage = "Performance collection enabled changed to: $checked"
                            }
                        )
                    }
                }
            }

            // Section 2: Custom Trace Manual Control Card
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Custom Trace Control",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = traceName,
                        onValueChange = { traceName = it },
                        label = { Text("Trace Name") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = activeTrace == null
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val trace = FirebasePerformance.instance.newTrace(traceName)
                                trace.start()
                                activeTrace = trace
                                logMessage = "Started Trace: '$traceName'"
                            },
                            modifier = Modifier.weight(1f),
                            enabled = activeTrace == null
                        ) {
                            Text("Start Trace")
                        }

                        Button(
                            onClick = {
                                activeTrace?.stop()
                                activeTrace = null
                                logMessage = "Stopped active trace."
                            },
                            modifier = Modifier.weight(1f),
                            enabled = activeTrace != null,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Stop Trace")
                        }
                    }

                    if (activeTrace != null) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        Text(
                            text = "Active Trace Mutations",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Metric Mutation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = metricName,
                                onValueChange = { metricName = it },
                                label = { Text("Metric Name") },
                                modifier = Modifier.weight(1.5f)
                            )
                            OutlinedTextField(
                                value = metricIncrement,
                                onValueChange = { metricIncrement = it },
                                label = { Text("Inc By") },
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    val inc = metricIncrement.toLongOrNull() ?: 1L
                                    activeTrace?.incrementMetric(metricName, inc)
                                    val currentVal = activeTrace?.getLongMetric(metricName) ?: 0L
                                    logMessage = "Incremented metric '$metricName' by $inc. Current total: $currentVal"
                                },
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text("Add")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Attribute Mutation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = attributeKey,
                                onValueChange = { attributeKey = it },
                                label = { Text("Attr Key") },
                                modifier = Modifier.weight(1.2f)
                            )
                            OutlinedTextField(
                                value = attributeValue,
                                onValueChange = { attributeValue = it },
                                label = { Text("Attr Value") },
                                modifier = Modifier.weight(1.2f)
                            )
                            Button(
                                onClick = {
                                    activeTrace?.putAttribute(attributeKey, attributeValue)
                                    val currentVal = activeTrace?.getAttribute(attributeKey) ?: "null"
                                    logMessage = "Added attribute '$attributeKey' = '$currentVal'"
                                },
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text("Put")
                            }
                        }
                    }
                }
            }

            // Section 3: Manual HTTP Metric Control Card
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "HTTP Request Metric Control",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = httpUrl,
                        onValueChange = { httpUrl = it },
                        label = { Text("Request URL") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = activeHttpMetric == null
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = httpMethod,
                            onValueChange = { httpMethod = it },
                            label = { Text("Method (GET/POST)") },
                            modifier = Modifier.weight(1f),
                            enabled = activeHttpMetric == null
                        )
                        OutlinedTextField(
                            value = httpResponseCode,
                            onValueChange = { httpResponseCode = it },
                            label = { Text("Response Code") },
                            modifier = Modifier.weight(1f),
                            enabled = activeHttpMetric == null
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val metric = HttpMetric(httpUrl, httpMethod)
                                metric.start()
                                activeHttpMetric = metric
                                logMessage = "Started HTTP Metric for $httpMethod to $httpUrl"
                            },
                            modifier = Modifier.weight(1f),
                            enabled = activeHttpMetric == null
                        ) {
                            Text("Start HTTP Req")
                        }

                        Button(
                            onClick = {
                                activeHttpMetric?.let { metric ->
                                    val code = httpResponseCode.toIntOrNull() ?: 200
                                    metric.setHttpResponseCode(code)
                                    metric.setRequestPayloadBytes(1024L)
                                    metric.setResponsePayloadBytes(2048L)
                                    metric.setResponseContentType("application/json")
                                    metric.stop()
                                }
                                activeHttpMetric = null
                                logMessage = "HTTP Request logged successfully! Code: $httpResponseCode, Req: 1KB, Resp: 2KB."
                            },
                            modifier = Modifier.weight(1f),
                            enabled = activeHttpMetric != null,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Stop & Log")
                        }
                    }
                }
            }

            // Log Console Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Console Output",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = logMessage,
                        fontSize = 13.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
