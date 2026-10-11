package zone.ien.firebase.example.ui.screen.perf

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import zone.ien.firebase.perf.FirebasePerformance
import zone.ien.firebase.perf.HttpMetric
import zone.ien.firebase.perf.Trace
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenSwitch
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.interactive.IenTextFieldState
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.IenBackButton
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenTopAppBar
import com.kyant.capsule.ContinuousRoundedRectangle

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

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Performance Monitoring", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IenBackButton(onClick = onNavigateBack)
                }
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
                            IenTheme.colors.surface,
                            IenTheme.colors.surfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                )
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Config Info Card
            IenSurface(
                color = IenTheme.colors.surfaceVariant,
                shape = ContinuousRoundedRectangle(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "Config & Status",
                        style = IenTheme.typography.title3,
                        fontWeight = FontWeight.Bold,
                        color = IenTheme.colors.brand
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IenText(
                            text = "Performance Collection Enabled:",
                            style = IenTheme.typography.body2,
                            color = IenTheme.colors.textPrimary
                        )
                        IenSwitch(
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
            IenSurface(
                color = IenTheme.colors.surfaceVariant,
                shape = ContinuousRoundedRectangle(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "Custom Trace Control",
                        style = IenTheme.typography.title2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    IenTextField(
                        value = traceName,
                        onValueChange = { traceName = it },
                        label = "Trace Name",
                        modifier = Modifier.fillMaxWidth(),
                        state = IenTextFieldState(enabled = activeTrace == null)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IenButton(
                            onClick = {
                                val trace = FirebasePerformance.instance.newTrace(traceName)
                                trace.start()
                                activeTrace = trace
                                logMessage = "Started Trace: '$traceName'"
                            },
                            modifier = Modifier.weight(1f),
                            state = IenButtonState(enabled = activeTrace == null)
                        ) {
                            IenText("Start Trace")
                        }

                        IenButton(
                            onClick = {
                                activeTrace?.stop()
                                activeTrace = null
                                logMessage = "Stopped active trace."
                            },
                            modifier = Modifier.weight(1f),
                            state = IenButtonState(enabled = activeTrace != null)
                        ) {
                            IenText("Stop Trace")
                        }
                    }

                    if (activeTrace != null) {
                        IenDivider(modifier = Modifier.padding(vertical = 12.dp))
                        IenText(
                            text = "Active Trace Mutations",
                            style = IenTheme.typography.body2,
                            fontWeight = FontWeight.SemiBold,
                            color = IenTheme.colors.brand
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Metric Mutation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IenTextField(
                                value = metricName,
                                onValueChange = { metricName = it },
                                label = "Metric Name",
                                modifier = Modifier.weight(1.5f)
                            )
                            IenTextField(
                                value = metricIncrement,
                                onValueChange = { metricIncrement = it },
                                label = "Inc By",
                                modifier = Modifier.weight(1f)
                            )
                            IenButton(
                                onClick = {
                                    val inc = metricIncrement.toLongOrNull() ?: 1L
                                    activeTrace?.incrementMetric(metricName, inc)
                                    val currentVal = activeTrace?.getLongMetric(metricName) ?: 0L
                                    logMessage = "Incremented metric '$metricName' by $inc. Current total: $currentVal"
                                },
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                IenText("Add")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Attribute Mutation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IenTextField(
                                value = attributeKey,
                                onValueChange = { attributeKey = it },
                                label = "Attr Key",
                                modifier = Modifier.weight(1.2f)
                            )
                            IenTextField(
                                value = attributeValue,
                                onValueChange = { attributeValue = it },
                                label = "Attr Value",
                                modifier = Modifier.weight(1.2f)
                            )
                            IenButton(
                                onClick = {
                                    activeTrace?.putAttribute(attributeKey, attributeValue)
                                    val currentVal = activeTrace?.getAttribute(attributeKey) ?: "null"
                                    logMessage = "Added attribute '$attributeKey' = '$currentVal'"
                                },
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                IenText("Put")
                            }
                        }
                    }
                }
            }

            // Section 3: Manual HTTP Metric Control Card
            IenSurface(
                color = IenTheme.colors.surfaceVariant,
                shape = ContinuousRoundedRectangle(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "HTTP Request Metric Control",
                        style = IenTheme.typography.title2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    IenTextField(
                        value = httpUrl,
                        onValueChange = { httpUrl = it },
                        label = "Request URL",
                        modifier = Modifier.fillMaxWidth(),
                        state = IenTextFieldState(enabled = activeHttpMetric == null)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IenTextField(
                            value = httpMethod,
                            onValueChange = { httpMethod = it },
                            label = "Method (GET/POST)",
                            modifier = Modifier.weight(1f),
                            state = IenTextFieldState(enabled = activeHttpMetric == null)
                        )
                        IenTextField(
                            value = httpResponseCode,
                            onValueChange = { httpResponseCode = it },
                            label = "Response Code",
                            modifier = Modifier.weight(1f),
                            state = IenTextFieldState(enabled = activeHttpMetric == null)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IenButton(
                            onClick = {
                                val metric = HttpMetric(httpUrl, httpMethod)
                                metric.start()
                                activeHttpMetric = metric
                                logMessage = "Started HTTP Metric for $httpMethod to $httpUrl"
                            },
                            modifier = Modifier.weight(1f),
                            state = IenButtonState(enabled = activeHttpMetric == null)
                        ) {
                            IenText("Start HTTP Req")
                        }

                        IenButton(
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
                            state = IenButtonState(enabled = activeHttpMetric != null)
                        ) {
                            IenText("Stop & Log")
                        }
                    }
                }
            }

            // Log Console Card
            IenSurface(
                color = IenTheme.colors.surfaceVariant,
                shape = ContinuousRoundedRectangle(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "Console Output",
                        style = IenTheme.typography.title3,
                        fontWeight = FontWeight.Bold,
                        color = IenTheme.colors.brand
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        text = logMessage,
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.textSecondary
                    )
                }
            }
        }
    }
}
