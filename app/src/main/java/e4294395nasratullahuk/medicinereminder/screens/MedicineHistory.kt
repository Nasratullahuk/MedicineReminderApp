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
import e4294395nasratullahuk.medicinereminder.R
import e4294395nasratullahuk.medicinereminder.database.Medicine
import e4294395nasratullahuk.medicinereminder.database.MedicineHistory
import e4294395nasratullahuk.medicinereminder.database.MedicineViewModel
import e4294395nasratullahuk.medicinereminder.notifications.NotificationScheduler
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HistoryMedicineListItem(
    medicine: Medicine,
    onDelete: (Medicine) -> Unit,
    onViewHistory: (Int) -> Unit
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
                // "History" button
                Button(
                    onClick = { onViewHistory(medicine.id) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.iv_history),
                        contentDescription = "View History",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("History")
                }
                Spacer(modifier = Modifier.width(8.dp))
                // "Delete" icon button
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalMedicineHistoryListScreen(navController: NavController, viewModel: MedicineViewModel) {
    val medicines by viewModel.allMedicines.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Medicines History",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface
                        )                     }
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
                    text = "No medicines added yet to view history.",
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
                    HistoryMedicineListItem( // Using the new HistoryMedicineListItem
                        medicine = medicine,
                        onDelete = {
                            viewModel.delete(it)
                            NotificationScheduler.cancelAllNotifications(
                                navController.context,
                                it.id
                            )
                            Toast.makeText(
                                navController.context,
                                "${it.name} deleted.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onViewHistory = { medId ->
                            navController.navigate("medicine_history/$medId")
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineHistoryScreen(navController: NavController, viewModel: MedicineViewModel, medicineId: Int) {
    val medicineHistory by viewModel.getMedicineHistoryForMedicine(medicineId).collectAsState(initial = emptyList())
    val medicine by viewModel.getMedicineById(medicineId).collectAsState(initial = null)
    val context = navController.context

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${medicine?.name ?: "Medicine"} History", style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, // Changed to AutoMirrored
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary // Set icon color
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (medicineHistory.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f), // Take available space
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No history recorded for this medicine yet.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f) // Take available space
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp), // Increased horizontal padding
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp) // More space between history items
                ) {
                    items(medicineHistory) { historyEntry ->
                        MedicineHistoryItem(historyEntry = historyEntry) // Using the dedicated item composable
                    }
                }
            }

            // "Delete All History" Button at the bottom
            if (medicineHistory.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp)) // Space above the button
                Button(
                    onClick = {
                        viewModel.deleteHistoryForMedicine(medicineId)
                        Toast.makeText(context, "History deleted for ${medicine?.name ?: "this medicine"}", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp) // Horizontal padding for the button
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp) // Rounded button
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete All History", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete All History", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(16.dp)) // Space below the button
            }
        }
    }
}

@Composable
fun MedicineHistoryItem(historyEntry: MedicineHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(10.dp) // Slightly more rounded corners
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Taken: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                    Date(historyEntry.takenTimestamp)
                )}",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp), // Larger font for timestamp
                fontWeight = FontWeight.SemiBold, // Semi-bold
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Dosage: ${historyEntry.dosageTaken}",
                style = MaterialTheme.typography.bodyLarge, // Larger body text
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}