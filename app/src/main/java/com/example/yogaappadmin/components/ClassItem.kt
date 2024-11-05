import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yogaappadmin.data.YogaClass

@Composable
fun ClassItem(yogaClass: YogaClass, onClick: (YogaClass) -> Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable { onClick(yogaClass) }
            .fillMaxWidth()
            .border(2.dp, Color(0xFF660099), shape = RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD1BEFC),
        )
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = "Class: ${yogaClass.type}",
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6600CC)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFF003366),
                    fontWeight = FontWeight.Medium,
                    text = "Day of week: ${yogaClass.dayOfWeek}"
                )
                Text(
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFF003366),
                    fontWeight = FontWeight.Medium,
                    text = "Teacher: ${yogaClass.teacher}"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFF003366),
                    fontWeight = FontWeight.Medium,
                    text = "Price: ${yogaClass.price} VND"
                )
                Text(
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFF003366),
                    fontWeight = FontWeight.Medium,
                    text = "Time: ${yogaClass.time}"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFF003366),
                    fontWeight = FontWeight.Medium,
                    text = "Duration: ${yogaClass.duration} minutes"
                )
                Text(
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFF003366),
                    fontWeight = FontWeight.Medium,
                    text = "Capacity: ${yogaClass.capacity} slots"
                )
            }

            Text(
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFF003366),
                fontWeight = FontWeight.Medium,
                text = "Description: ${yogaClass.description}"
            )
        }
    }
}
