import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yogaappadmin.R
import com.example.yogaappadmin.components.SyncConfirmationDialog
import com.example.yogaappadmin.components.isInternetAvailable
import com.example.yogaappadmin.data.YogaClass
import com.example.yogaappadmin.viewmodel.YogaViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaClassListScreen(viewModel: YogaViewModel, modifier: Modifier = Modifier) {
    val yogaClasses by viewModel.allYogaClasses.collectAsState(initial = emptyList())
    var showForm by remember { mutableStateOf(false) }
    var selectedYogaClass by remember { mutableStateOf<YogaClass?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }
    var showSyncDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val filterOptions = listOf("Time", "Day of Week", "Teacher")

    Scaffold(
        floatingActionButton = {
            if (!showForm) {
                FloatingActionButton(
                    onClick = {
                        showForm = true
                        selectedYogaClass = null
                    },
                    containerColor = Color(0xFF993399)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add class", tint = Color.White, modifier = Modifier.size(36.dp))
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Tạo khoảng trống giữa tiêu đề và icon
        ) {
            Text(
                text = "QuynhChuc Yoga",
                modifier = Modifier.padding(start = 16.dp),
                style = TextStyle(
                    color = Color(0xFF660066),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            // Biểu tượng search ở bên phải
            IconButton(onClick = { showSyncDialog = true }) {
                Icon(painter = painterResource(id = R.drawable.baseline_sync_24), contentDescription = "Sync", tint = Color(0xFF660099), modifier = Modifier.size(36.dp))
            }
        }
        Column(modifier = Modifier.padding(innerPadding)) {
            if (!showForm) {
                // Chỉ hiển thị thanh tìm kiếm và bộ lọc khi không hiển thị form
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.weight(1f).border(3.dp, Color(0xFF660099)),
                        placeholder = { Text("Search...", color = Color(0xFF660099), fontWeight = FontWeight.SemiBold) },
                        singleLine = true,

                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box {
                        Button(onClick = { expanded = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF993399))) {
                            Text(selectedFilter)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color(0xFF993399))
                        ) {
                            filterOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = Color.White) },
                                    onClick = {
                                        selectedFilter = option
                                        expanded = false
                                    },
                                    modifier = Modifier.background(Color(0xFF993399))
                                )
                            }
                        }
                    }
                }
            }

            if (showForm) {
                ClassForm(
                    yogaClass = selectedYogaClass,
                    onClose = {
                        showForm = false
                        selectedYogaClass = null
                    },
                    viewModel = viewModel,
                )
            } else {
                YogaClassList(
                    classes = yogaClasses.filter {
                        it.matchesSearchQuery(searchQuery, selectedFilter)
                    },
                    onItemClick = { yogaClass ->
                        selectedYogaClass = yogaClass
                        showForm = true
                    }
                )
            }

            if (showSyncDialog) {
                SyncConfirmationDialog(
                    onConfirm = {
                        if (isInternetAvailable(context)) {
                            viewModel.syncWithFirestore()
                        } else {
                            // Show no internet message
                        }
                        showSyncDialog = false
                    },
                    onDismiss = { showSyncDialog = false },
                    hasInternet = isInternetAvailable(context)
                )
            }
        }
    }
}


@Composable
fun YogaClassList(classes: List<YogaClass>, onItemClick: (YogaClass) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(classes) { yogaClass ->
            ClassItem(yogaClass = yogaClass, onClick = onItemClick)
        }
    }
}

// Extension function to filter YogaClass based on search query and selected filter
fun YogaClass.matchesSearchQuery(query: String, filter: String): Boolean {
    return when (filter) {
        "Time" -> this.time.contains(query, ignoreCase = true)
        "Day of Week" -> this.dayOfWeek.contains(query, ignoreCase = true)
        "Teacher" -> this.teacher.contains(query, ignoreCase = true)
        else -> this.time.contains(query, ignoreCase = true) ||
                this.dayOfWeek.contains(query, ignoreCase = true) ||
                this.teacher.contains(query, ignoreCase = true)
    }
}