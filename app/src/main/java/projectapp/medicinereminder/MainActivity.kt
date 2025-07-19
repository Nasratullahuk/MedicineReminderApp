package projectapp.medicinereminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import projectapp.medicinereminder.ui.theme.MedicineReminderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicineReminderTheme {
                MedicineReminderSplashScreenDesign()
            }
        }
    }
}

@Composable
fun MedicineReminderSplashScreenDesign(
    appName: String = "MedReminder", // Default app name
    personName: String = "By Nasrat" // Default developer name
) {
    val primaryDark = colorResource(id = R.color.PrimaryDark)
    val textOnPrimaryDark = colorResource(id = R.color.text_on_primary_dark)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryDark), // Use your PrimaryDark color for the background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo (using a Material Icon as a placeholder)

            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Medicine Reminder App",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp)) // Space between logo and app name

            // App Name
            Text(
                text = appName,
                fontSize = 48.sp, // Large font size for app name
                fontWeight = FontWeight.ExtraBold,
                color = textOnPrimaryDark
            )
            Spacer(modifier = Modifier.height(16.dp)) // Space between app name and person name

            // By Person Name
            Text(
                text = personName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = textOnPrimaryDark.copy(alpha = 0.7f) // Slightly faded for secondary text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMedicineReminderSplashScreen() {
    // You can customize the names for the preview
    MedicineReminderSplashScreenDesign(
        appName = "PillPal",
        personName = "By Dr. Reminder"
    )
}