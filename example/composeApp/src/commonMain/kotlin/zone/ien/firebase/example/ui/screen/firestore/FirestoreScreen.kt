package zone.ien.firebase.example.ui.screen.firestore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.example.data.Message
import zone.ien.firebase.firestore.FirebaseFirestore

@Composable
fun FirestoreScreen(onBack: () -> Unit) {
    var firestore: FirebaseFirestore? by remember { mutableStateOf(null) }
    var initError by remember { mutableStateOf<String?>(null) }
    var counter by remember { mutableStateOf(0L) }
    var inputText by remember { mutableStateOf("") }
    var singleReadText by remember { mutableStateOf("Not read yet") }

    remember {
        try {
            if (FirebaseApp.isInitialized) {
                firestore = FirebaseFirestore.getInstance()
            } else {
                initError = "Firebase Core must be initialized first. Go to 'Firebase Init' screen."
            }
        } catch (e: Exception) {
            initError = e.message ?: "Firebase not configured on local platform yet."
        }
    }

    val scope = rememberCoroutineScope()

    val messagesFlow = remember(firestore) {
        firestore?.collection("messages")?.snapshots()?.map { querySnapshot ->
            querySnapshot.getDocuments().mapNotNull { doc ->
                val text = doc.get("text") as? String ?: return@mapNotNull null
                val order = (doc.get("order") as? Number)?.toLong() ?: 0L
                Message(doc.getId(), text, order)
            }.sortedByDescending { it.order }
        } ?: flowOf(emptyList())
    }
    val liveMessages by messagesFlow.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                title = {
                    Text(
                        text = "Firestore Live Demo",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            if (initError != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Notice: $initError",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Message Input", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        val db = firestore ?: return@Button
                        scope.launch {
                            try {
                                counter++
                                val data = mapOf(
                                    "text" to inputText,
                                    "order" to counter
                                )
                                db.collection("messages").document("hello").set(data)
                                db.collection("messages").document().set(data)
                                inputText = ""
                            } catch (e: Exception) {
                                singleReadText = "Write failed: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).height(46.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Write Hello", color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        val db = firestore ?: return@Button
                        scope.launch {
                            try {
                                val doc = db.collection("messages").document("hello").get()
                                if (doc.getExists()) {
                                    singleReadText = doc.get("text") as? String ?: "No 'text' field"
                                } else {
                                    singleReadText = "Document 'hello' not found"
                                }
                            } catch (e: Exception) {
                                singleReadText = "Read failed: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).height(46.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Read Hello", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Single Read Result:",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Text(
                text = singleReadText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Live Collection Stream (Flow):",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(liveMessages) { msg ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "DocID: ${msg.id} | Order: ${msg.order}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}
