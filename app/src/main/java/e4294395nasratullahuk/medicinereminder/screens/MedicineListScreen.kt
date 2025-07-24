package e4294395nasratullahuk.medicinereminder.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import e4294395nasratullahuk.medicinereminder.database.Medicine
import e4294395nasratullahuk.medicinereminder.database.MedicineViewModel
import e4294395nasratullahuk.medicinereminder.notifications.NotificationScheduler
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(navController: NavController, viewModel: MedicineViewModel) {
    val medicines by viewModel.allMedicines.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Text(
                        "Scheduled Medicines",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface
                        )                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (medicines.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No medicines scheduled yet.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("add_medicine") }) {
                    Text("Add Your First Medicine")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(medicines) { medicine ->
                    ScheduleMedicineListItem( // Using the new ScheduleMedicineListItem
                        medicine = medicine,
                        onDelete = {
                            viewModel.delete(it)
                            NotificationScheduler.cancelAllNotifications(
                                navController.context,
                                it.id
                            )
                        },
                        onMarkTaken = { med ->
                            viewModel.markMedicineTaken(med.id, med.dosage)
                            Toast.makeText(
                                navController.context,
                                "${med.name} marked as taken!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ScheduleMedicineListItem(
    medicine: Medicine,
    onDelete: (Medicine) -> Unit,
    onMarkTaken: (Medicine) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val photoPainter = if (medicine.photoUri != null) {
                    rememberAsyncImagePainter(Uri.parse(medicine.photoUri))
                } else {
                    painterResource(id = android.R.drawable.ic_menu_gallery)
                }

                Image(
                    painter = photoPainter,
                    contentDescription = "Medicine Photo",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (medicine.photoUri == null) androidx.compose.ui.graphics.ColorFilter.tint(
                        MaterialTheme.colorScheme.primary
                    ) else null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medicine.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Dosage: ${medicine.dosage}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Times: ${medicine.times.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Frequency: ${medicine.frequencyType}" +
                                if (medicine.frequencyType == "Specific Days" && medicine.specificDays != null) {
                                    val dayNames = medicine.specificDays.map { dayInt ->
                                        when (dayInt) {
                                            Calendar.SUNDAY -> "Sun"
                                            Calendar.MONDAY -> "Mon"
                                            Calendar.TUESDAY -> "Tue"
                                            Calendar.WEDNESDAY -> "Wed"
                                            Calendar.THURSDAY -> "Thu"
                                            Calendar.FRIDAY -> "Fri"
                                            Calendar.SATURDAY -> "Sat"
                                            else -> ""
                                        }
                                    }
                                    " (${dayNames.joinToString(", ")})"
                                } else "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    medicine.currentQuantity?.let {
                        Text(
                            text = "Quantity: $it" + (medicine.totalQuantity?.let { total -> "/$total" }
                                ?: ""),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (it <= (medicine.refillThreshold
                                    ?: 0)
                            ) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (it <= (medicine.refillThreshold
                                    ?: 0)
                            ) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Only "Mark Taken" button
                Button(
                    onClick = { onMarkTaken(medicine) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Mark Taken",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Taken")
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Only "Delete" icon button
                IconButton(onClick = { onDelete(medicine) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Medicine",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}