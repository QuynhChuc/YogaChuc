import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.yogaappadmin.data.YogaClass
import com.example.yogaappadmin.viewmodel.YogaViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClassForm(
    yogaClass: YogaClass?,
    onClose: () -> Unit,
    viewModel: YogaViewModel
) {
    val id by remember { mutableStateOf(yogaClass?.id ?: 0) }
    var dayOfWeek by remember { mutableStateOf(yogaClass?.dayOfWeek ?: "") }
    var time by remember { mutableStateOf(yogaClass?.time ?: "") }
    var teacher by remember { mutableStateOf(yogaClass?.teacher ?: "") }
    var capacity by remember { mutableStateOf(yogaClass?.capacity?.toString() ?: "") }
    var price by remember { mutableStateOf(yogaClass?.price?.toString() ?: "") }
    var duration by remember { mutableStateOf(yogaClass?.duration?.toString() ?: "") }
    var type by remember { mutableStateOf(yogaClass?.type ?: "") }
    var description by remember { mutableStateOf(yogaClass?.description ?: "") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (yogaClass != null) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF993399))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text(text = "Delete Confirmation", color = Color.Red)
                },
                text = {
                    Text("Are you sure to delete this class?", color = Color(0xFF6600CC), fontSize = 18.sp)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            yogaClass?.let { viewModel.deleteYogaClass(it.id) }
                            showDeleteDialog = false  // Đóng hộp thoại
                            onClose()  // Đóng form sau khi xoá
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0564A))
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F5D5D)),
                        onClick = { showDeleteDialog = false }  // Đóng hộp thoại nếu nhấn "No"
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }


        OutlinedTextField(
            value = dayOfWeek,
            onValueChange = { dayOfWeek = it },
            label = { Text("Day of week", color = Color(0xFF6600CC), fontSize = 18.sp, fontWeight = FontWeight.Bold)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            trailingIcon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Time of week",
                    modifier = Modifier.clickable {
                        showDatePickerDialog(context) { selectedDayOfWeek ->
                            dayOfWeek = selectedDayOfWeek
                        }
                    }
                )
            }
        )

        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Time", color = Color(0xFF6600CC), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = teacher,
            onValueChange = { teacher = it },
            label = { Text("Teacher", color = Color(0xFF6600CC), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = capacity,
            onValueChange = { capacity = it },
            label = { Text("Capacity", color = Color(0xFF6600CC), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price", color = Color(0xFF6600CC), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration", color = Color(0xFF6600CC), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Type", color = Color(0xFF6600CC), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (optional)", color = Color(0xFF6600CC), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
        }

        Row {
            Button(
                onClick = {
                    if (dayOfWeek.isEmpty() || time.isEmpty() || teacher.isEmpty() || capacity.isEmpty() || price.isEmpty() || type.isEmpty() || duration.isEmpty()) {
                        errorMessage = "Please fill all required field!"
                    } else {
                        val newClass = YogaClass(
                            id = id,
                            dayOfWeek = dayOfWeek,
                            time = time,
                            teacher = teacher,
                            capacity = capacity.toInt(),
                            duration = duration.toInt(),
                            price = price.toDouble(),
                            type = type,
                            description = description.ifEmpty { null }
                        )

                        if (yogaClass == null) {
                            viewModel.insertYogaClass(newClass)
                        } else {
                            viewModel.updateYogaClass(newClass)
                        }
                        onClose()
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(if (yogaClass == null) "ADD CLASS" else "EDIT CLASS", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { onClose() },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF660099))
            ) {
                Text("CANCEL", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Hàm hiển thị DatePickerDialog và trả về thứ của ngày đã chọn
@RequiresApi(Build.VERSION_CODES.O)
fun showDatePickerDialog(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedLocalDate = LocalDate.of(year, month + 1, dayOfMonth)
            val dayOfWeek =
                selectedLocalDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            onDateSelected(dayOfWeek)  // Trả về thứ của ngày đã chọn
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}
