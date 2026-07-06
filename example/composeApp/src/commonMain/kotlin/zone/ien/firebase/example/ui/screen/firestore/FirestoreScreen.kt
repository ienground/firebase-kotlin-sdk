package zone.ien.firebase.example.ui.screen.firestore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.example.data.Message
import zone.ien.firebase.firestore.DocumentSnapshot
import zone.ien.firebase.firestore.FirebaseFirestore
import zone.ien.firebase.firestore.Query
import zone.ien.firebase.firestore.QueryDirection
import zone.ien.firebase.firestore.WhereOperator

private const val QueryCollection = "query_samples"

private val queryFields = listOf("category", "score", "age", "tags", "createdAt", "name")
private val sortFields = listOf("score", "age", "createdAt", "name", "category")
private val queryOperators = WhereOperator.entries
private val cursorModes = CursorMode.entries

@Composable
fun FirestoreScreen(onBack: () -> Unit) {
    val firestore = remember {
        if (FirebaseApp.isInitialized) {
            runCatching { FirebaseFirestore.getInstance() }.getOrNull()
        } else {
            null
        }
    }
    val isSupported = firestore != null
    val initError = remember {
        if (!FirebaseApp.isInitialized) {
            "Firebase Core must be initialized first. Open the Firebase Init screen before testing Firestore."
        } else {
            runCatching { FirebaseFirestore.getInstance() }.exceptionOrNull()?.message
        }
    }
    var counter by remember { mutableStateOf(0L) }
    var inputText by remember { mutableStateOf("") }
    var singleReadText by remember { mutableStateOf("Not read yet") }
    var seedStatus by remember { mutableStateOf("Query sample data is prepared automatically when this screen opens.") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(firestore) {
        val db = firestore ?: return@LaunchedEffect
        seedStatus = runCatching {
            seedQueryDocuments(db)
            "Seed complete: prepared ${querySeedDocuments.size} documents in the $QueryCollection collection."
        }.getOrElse {
            "Seed failed: ${it.message}"
        }
    }

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
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "<",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                title = {
                    Text(
                        text = "Firestore Testing",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                PlatformStatusCard(isSupported = isSupported, initError = initError)
            }

            item {
                BasicDocumentSection(
                    isSupported = isSupported,
                    inputText = inputText,
                    singleReadText = singleReadText,
                    onInputChange = { inputText = it },
                    onWrite = {
                        val db = firestore ?: return@BasicDocumentSection
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
                    onRead = {
                        val db = firestore ?: return@BasicDocumentSection
                        scope.launch {
                            try {
                                val doc = db.collection("messages").document("hello").get()
                                singleReadText = if (doc.getExists()) {
                                    doc.get("text") as? String ?: "No text field"
                                } else {
                                    "Document hello was not found"
                                }
                            } catch (e: Exception) {
                                singleReadText = "Read failed: ${e.message}"
                            }
                        }
                    }
                )
            }

            item {
                QueryTestingSection(
                    firestore = firestore,
                    seedStatus = seedStatus,
                    onSeed = {
                        val db = firestore ?: return@QueryTestingSection
                        scope.launch {
                            seedStatus = runCatching {
                                seedQueryDocuments(db)
                                "Seed complete: reset the $QueryCollection collection."
                            }.getOrElse {
                                "Seed failed: ${it.message}"
                            }
                        }
                    }
                )
            }

            item {
                Text(
                    text = "Live Collection Stream",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            items(liveMessages) { msg ->
                MessageCard(msg)
            }
        }
    }
}

@Composable
private fun PlatformStatusCard(
    isSupported: Boolean,
    initError: String?
) {
    val statusColor = if (isSupported) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Platform Status",
                style = MaterialTheme.typography.titleMedium,
                color = statusColor
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Android: Supported - delegates to the official Firebase Android Firestore SDK.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "iOS: Supported - delegates to the SwiftPM FirebaseFirestore Objective-C exposed API.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Query API: where, orderBy, limit, limitToLast, and document cursors call the real SDK.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!isSupported || initError != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = initError ?: "Firestore cannot create an instance on the current target.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun BasicDocumentSection(
    isSupported: Boolean,
    inputText: String,
    singleReadText: String,
    onInputChange: (String) -> Unit,
    onWrite: () -> Unit,
    onRead: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Basic Document Read/Write",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = inputText,
                enabled = isSupported,
                onValueChange = onInputChange,
                label = { Text("Message") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onWrite,
                    enabled = isSupported,
                    modifier = Modifier.weight(1f).height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Write", color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = onRead,
                    enabled = isSupported,
                    modifier = Modifier.weight(1f).height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Read", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Result: $singleReadText",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun QueryTestingSection(
    firestore: FirebaseFirestore?,
    seedStatus: String,
    onSeed: () -> Unit
) {
    var collectionPath by remember { mutableStateOf(QueryCollection) }
    var field by remember { mutableStateOf("category") }
    var selectedOperator by remember { mutableStateOf(WhereOperator.EQUAL) }
    var valueText by remember { mutableStateOf("alpha") }
    var sortField by remember { mutableStateOf("score") }
    var direction by remember { mutableStateOf(QueryDirection.DESCENDING) }
    var limitText by remember { mutableStateOf("5") }
    var useLimitToLast by remember { mutableStateOf(false) }
    var cursorMode by remember { mutableStateOf(CursorMode.NONE) }
    var resultSummary by remember { mutableStateOf("No query has been executed yet.") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var results by remember { mutableStateOf(emptyList<QueryResultRow>()) }
    var lastPageDocuments by remember { mutableStateOf(emptyList<DocumentSnapshot>()) }
    var loadedPages by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val isSupported = firestore != null

    fun runQuery(loadMore: Boolean) {
        val db = firestore ?: return
        scope.launch {
            errorText = null
            runCatching {
                val limit = limitText.toLongOrNull()?.coerceAtLeast(1L) ?: 5L
                val activeCursorMode = if (loadMore) CursorMode.START_AFTER_LAST else cursorMode
                val query = buildQuery(
                    base = db.collection(collectionPath),
                    field = field,
                    operator = selectedOperator,
                    rawValue = valueText,
                    sortField = sortField,
                    direction = direction,
                    limit = limit,
                    useLimitToLast = useLimitToLast,
                    cursorMode = activeCursorMode,
                    cursorDocuments = lastPageDocuments
                )
                val snapshot = query.get()
                val documents = snapshot.getDocuments()
                val rows = documents.map { it.toQueryResultRow() }
                loadedPages = if (loadMore) loadedPages + 1 else 1
                lastPageDocuments = documents
                results = if (loadMore) results + rows else rows
                resultSummary = querySummary(
                    collectionPath = collectionPath,
                    field = field,
                    operator = selectedOperator,
                    rawValue = valueText,
                    sortField = sortField,
                    direction = direction,
                    limit = limit,
                    useLimitToLast = useLimitToLast,
                    count = rows.size,
                    cursorText = cursorSummary(activeCursorMode, lastPageDocuments),
                    loadedPages = loadedPages
                )
            }.onFailure {
                errorText = it.message ?: it.toString()
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Query Testing",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = seedStatus,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onSeed,
                enabled = isSupported,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Reset Seed Data")
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = collectionPath,
                enabled = isSupported,
                onValueChange = { collectionPath = it },
                label = { Text("Collection") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CycleButton(
                    label = "Field",
                    value = field,
                    enabled = isSupported,
                    modifier = Modifier.weight(1f),
                    onClick = { field = queryFields.nextAfter(field) }
                )
                CycleButton(
                    label = "Operator",
                    value = selectedOperator.label(),
                    enabled = isSupported,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedOperator = queryOperators.nextAfter(selectedOperator) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = valueText,
                enabled = isSupported,
                onValueChange = { valueText = it },
                label = { Text("Value") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CycleButton(
                    label = "Sort Field",
                    value = sortField,
                    enabled = isSupported,
                    modifier = Modifier.weight(1f),
                    onClick = { sortField = sortFields.nextAfter(sortField) }
                )
                CycleButton(
                    label = "Direction",
                    value = direction.label(),
                    enabled = isSupported,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        direction = if (direction == QueryDirection.ASCENDING) {
                            QueryDirection.DESCENDING
                        } else {
                            QueryDirection.ASCENDING
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = limitText,
                    enabled = isSupported,
                    onValueChange = { limitText = it.filter(Char::isDigit) },
                    label = { Text("limit") },
                    modifier = Modifier.weight(1f)
                )
                CycleButton(
                    label = "Limit Mode",
                    value = if (useLimitToLast) "limitToLast" else "limit",
                    enabled = isSupported,
                    modifier = Modifier.weight(1f),
                    onClick = { useLimitToLast = !useLimitToLast }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            CycleButton(
                label = "Cursor",
                value = cursorMode.label(),
                enabled = isSupported,
                modifier = Modifier.fillMaxWidth(),
                onClick = { cursorMode = cursorModes.nextAfter(cursorMode) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { runQuery(loadMore = false) },
                    enabled = isSupported,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Run Query")
                }
                Button(
                    onClick = { runQuery(loadMore = true) },
                    enabled = isSupported && lastPageDocuments.isNotEmpty() && !useLimitToLast,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Next Page")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = resultSummary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            errorText?.let {
                Spacer(modifier = Modifier.height(8.dp))
                ErrorBox(it)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Returned documents: ${results.size}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            results.forEach { row ->
                QueryResultCard(row)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun CycleButton(
    label: String,
    value: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(54.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun ErrorBox(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.12f))
            .padding(10.dp)
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = "Missing composite indexes, SDK constraints, and unsupported combinations are shown here without being hidden.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun QueryResultCard(row: QueryResultRow) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "${row.name} / ${row.category}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "id=${row.id}, score=${row.score}, age=${row.age}, createdAt=${row.createdAt}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "tags=${row.tags.joinToString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun MessageCard(msg: Message) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp),
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
                text = "Document=${msg.id}, order=${msg.order}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

private suspend fun seedQueryDocuments(db: FirebaseFirestore) {
    val collection = db.collection(QueryCollection)
    querySeedDocuments.forEach { doc ->
        collection.document(doc.id).set(
            mapOf(
                "name" to doc.name,
                "score" to doc.score,
                "age" to doc.age,
                "category" to doc.category,
                "tags" to doc.tags,
                "createdAt" to doc.createdAt
            )
        )
    }
}

private fun buildQuery(
    base: Query,
    field: String,
    operator: WhereOperator,
    rawValue: String,
    sortField: String,
    direction: QueryDirection,
    limit: Long,
    useLimitToLast: Boolean,
    cursorMode: CursorMode,
    cursorDocuments: List<DocumentSnapshot>
): Query {
    val value = parseQueryValue(field, operator, rawValue)
    var query = base
        .where(field, operator, value)
        .orderBy(sortField, direction)
    query = when (cursorMode) {
        CursorMode.NONE -> query
        CursorMode.START_AT_FIRST -> cursorDocuments.firstOrNull()?.let(query::startAt) ?: query
        CursorMode.START_AFTER_LAST -> cursorDocuments.lastOrNull()?.let(query::startAfter) ?: query
        CursorMode.END_AT_LAST -> cursorDocuments.lastOrNull()?.let(query::endAt) ?: query
        CursorMode.END_BEFORE_FIRST -> cursorDocuments.firstOrNull()?.let(query::endBefore) ?: query
    }
    return if (useLimitToLast) {
        query.limitToLast(limit)
    } else {
        query.limit(limit)
    }
}

private fun parseQueryValue(
    field: String,
    operator: WhereOperator,
    rawValue: String
): Any {
    val values = rawValue.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    return when (operator) {
        WhereOperator.ARRAY_CONTAINS -> parseSingleValue(field, rawValue)
        WhereOperator.ARRAY_CONTAINS_ANY,
        WhereOperator.IN,
        WhereOperator.NOT_IN -> values.map { parseSingleValue(field, it) }
        else -> parseSingleValue(field, rawValue)
    }
}

private fun parseSingleValue(field: String, rawValue: String): Any {
    return when (field) {
        "score", "age", "createdAt" -> rawValue.trim().toLongOrNull() ?: 0L
        else -> rawValue.trim()
    }
}

private fun DocumentSnapshot.toQueryResultRow(): QueryResultRow {
    return QueryResultRow(
        id = getId(),
        name = get("name") as? String ?: "",
        score = (get("score") as? Number)?.toLong() ?: 0L,
        age = (get("age") as? Number)?.toLong() ?: 0L,
        category = get("category") as? String ?: "",
        tags = (get("tags") as? List<*>)?.map { it.toString() } ?: emptyList(),
        createdAt = (get("createdAt") as? Number)?.toLong() ?: 0L
    )
}

private fun querySummary(
    collectionPath: String,
    field: String,
    operator: WhereOperator,
    rawValue: String,
    sortField: String,
    direction: QueryDirection,
    limit: Long,
    useLimitToLast: Boolean,
    count: Int,
    cursorText: String,
    loadedPages: Int
): String {
    val limitName = if (useLimitToLast) "limitToLast" else "limit"
    return "$collectionPath where $field ${operator.label()} $rawValue orderBy $sortField ${direction.label()} $limitName=$limit$cursorText / returned=$count, loadedPages=$loadedPages"
}

private fun cursorSummary(
    cursorMode: CursorMode,
    cursorDocuments: List<DocumentSnapshot>
): String {
    return when (cursorMode) {
        CursorMode.NONE -> ""
        CursorMode.START_AT_FIRST -> cursorDocuments.firstOrNull()?.let { ", startAt=${it.getId()}" } ?: ", startAt=pending"
        CursorMode.START_AFTER_LAST -> cursorDocuments.lastOrNull()?.let { ", startAfter=${it.getId()}" } ?: ", startAfter=pending"
        CursorMode.END_AT_LAST -> cursorDocuments.lastOrNull()?.let { ", endAt=${it.getId()}" } ?: ", endAt=pending"
        CursorMode.END_BEFORE_FIRST -> cursorDocuments.firstOrNull()?.let { ", endBefore=${it.getId()}" } ?: ", endBefore=pending"
    }
}

private fun WhereOperator.label(): String {
    return when (this) {
        WhereOperator.EQUAL -> "=="
        WhereOperator.NOT_EQUAL -> "!="
        WhereOperator.LESS_THAN -> "<"
        WhereOperator.LESS_THAN_OR_EQUAL -> "<="
        WhereOperator.GREATER_THAN -> ">"
        WhereOperator.GREATER_THAN_OR_EQUAL -> ">="
        WhereOperator.ARRAY_CONTAINS -> "array-contains"
        WhereOperator.ARRAY_CONTAINS_ANY -> "array-contains-any"
        WhereOperator.IN -> "in"
        WhereOperator.NOT_IN -> "not-in"
    }
}

private fun QueryDirection.label(): String {
    return when (this) {
        QueryDirection.ASCENDING -> "Ascending"
        QueryDirection.DESCENDING -> "Descending"
    }
}

private fun CursorMode.label(): String {
    return when (this) {
        CursorMode.NONE -> "None"
        CursorMode.START_AT_FIRST -> "startAt first document"
        CursorMode.START_AFTER_LAST -> "startAfter last document"
        CursorMode.END_AT_LAST -> "endAt last document"
        CursorMode.END_BEFORE_FIRST -> "endBefore first document"
    }
}

private fun <T> List<T>.nextAfter(current: T): T {
    val index = indexOf(current)
    return this[(index + 1).mod(size)]
}

private data class QuerySeedDocument(
    val id: String,
    val name: String,
    val score: Long,
    val age: Long,
    val category: String,
    val tags: List<String>,
    val createdAt: Long
)

private data class QueryResultRow(
    val id: String,
    val name: String,
    val score: Long,
    val age: Long,
    val category: String,
    val tags: List<String>,
    val createdAt: Long
)

private enum class CursorMode {
    NONE,
    START_AT_FIRST,
    START_AFTER_LAST,
    END_AT_LAST,
    END_BEFORE_FIRST
}

private val querySeedDocuments = listOf(
    QuerySeedDocument("sample_01", "Ari", 92, 24, "alpha", listOf("kmp", "mobile"), 1_710_000_001),
    QuerySeedDocument("sample_02", "Bora", 61, 31, "beta", listOf("android", "query"), 1_710_000_002),
    QuerySeedDocument("sample_03", "Cyan", 77, 28, "alpha", listOf("ios", "mobile"), 1_710_000_003),
    QuerySeedDocument("sample_04", "Dami", 88, 35, "gamma", listOf("kmp", "query"), 1_710_000_004),
    QuerySeedDocument("sample_05", "Eden", 45, 22, "beta", listOf("android", "low"), 1_710_000_005),
    QuerySeedDocument("sample_06", "Faro", 99, 41, "alpha", listOf("ios", "top"), 1_710_000_006),
    QuerySeedDocument("sample_07", "Gina", 73, 29, "gamma", listOf("kmp", "sort"), 1_710_000_007),
    QuerySeedDocument("sample_08", "Haru", 84, 26, "beta", listOf("mobile", "sort"), 1_710_000_008),
    QuerySeedDocument("sample_09", "Ian", 56, 33, "alpha", listOf("query", "edge"), 1_710_000_009),
    QuerySeedDocument("sample_10", "Juna", 68, 38, "gamma", listOf("ios", "edge"), 1_710_000_010),
    QuerySeedDocument("sample_11", "Kiro", 95, 27, "beta", listOf("android", "top"), 1_710_000_011),
    QuerySeedDocument("sample_12", "Lina", 82, 30, "alpha", listOf("kmp", "query"), 1_710_000_012)
)
