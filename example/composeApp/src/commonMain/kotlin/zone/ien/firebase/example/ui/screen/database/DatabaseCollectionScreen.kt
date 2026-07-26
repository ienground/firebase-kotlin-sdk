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
import zone.ien.firebase.database.collection.ImmutableSortedMap

@Composable
fun DatabaseCollectionScreen(onBack: () -> Unit) {
    val scrollState = rememberScrollState()

    // Create an empty map sorted alphabetically by String keys
    var sortedMap by remember {
        mutableStateOf(ImmutableSortedMap.emptyMap<String, String>(compareBy { it }))
    }

    var keyInput by remember { mutableStateOf("") }
    var valueInput by remember { mutableStateOf("") }

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Database Collection Demo") },
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
                text = "Demonstrate real-time ImmutableSortedMap storage updates. Keys are alphabetically sorted automatically on insertion.",
                style = IenTheme.typography.body1,
                color = IenTheme.colors.textSecondary
            )

            IenTextField(
                value = keyInput,
                onValueChange = { keyInput = it },
                label = "Key (e.g. apple, banana)",
                modifier = Modifier.fillMaxWidth()
            )

            IenTextField(
                value = valueInput,
                onValueChange = { valueInput = it },
                label = "Value",
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IenButton(
                    onClick = {
                        if (keyInput.isNotBlank()) {
                            sortedMap = sortedMap.insert(keyInput, valueInput)
                            keyInput = ""
                            valueInput = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    IenText("Insert Key")
                }

                IenButton(
                    onClick = {
                        if (keyInput.isNotBlank()) {
                            sortedMap = sortedMap.remove(keyInput)
                            keyInput = ""
                        }
                    },
                    
                    modifier = Modifier.weight(1f)
                ) {
                    IenText("Remove Key")
                }
            }

            IenText("Sorted Collection Entries:", style = IenTheme.typography.title2)

            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (sortedMap.isEmpty()) {
                        IenText("No entries in sorted map.", style = IenTheme.typography.body1)
                    } else {
                        sortedMap.forEach { entry ->
                            IenText(
                                text = "🔑 ${entry.key} ➔ 📄 ${entry.value}",
                                style = IenTheme.typography.body1
                            )
                        }
                    }
                }
            }
        }
    }
}
