package zone.ien.firebase.example.ui.screen.database

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.firebase.database.collection.ImmutableSortedMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseCollectionScreen(onBack: () -> Unit) {
    val scrollState = rememberScrollState()

    // Create an empty map sorted alphabetically by String keys
    var sortedMap by remember {
        mutableStateOf(ImmutableSortedMap.emptyMap<String, String>(compareBy { it }))
    }

    var keyInput by remember { mutableStateOf("") }
    var valueInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Database Collection Demo") },
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
                text = "Demonstrate real-time ImmutableSortedMap storage updates. Keys are alphabetically sorted automatically on insertion.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = keyInput,
                onValueChange = { keyInput = it },
                label = { Text("Key (e.g. apple, banana)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = valueInput,
                onValueChange = { valueInput = it },
                label = { Text("Value") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (keyInput.isNotBlank()) {
                            sortedMap = sortedMap.insert(keyInput, valueInput)
                            keyInput = ""
                            valueInput = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Insert Key")
                }

                Button(
                    onClick = {
                        if (keyInput.isNotBlank()) {
                            sortedMap = sortedMap.remove(keyInput)
                            keyInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Remove Key")
                }
            }

            Text("Sorted Collection Entries:", style = MaterialTheme.typography.titleMedium)

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (sortedMap.isEmpty()) {
                        Text("No entries in sorted map.", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        sortedMap.forEach { entry ->
                            Text(
                                text = "🔑 ${entry.key} ➔ 📄 ${entry.value}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
