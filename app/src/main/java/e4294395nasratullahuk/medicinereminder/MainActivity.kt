package e4294395nasratullahuk.medicinereminder

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import e4294395nasratullahuk.medicinereminder.database.MedicineViewModel
import e4294395nasratullahuk.medicinereminder.notifications.NotificationScheduler
import e4294395nasratullahuk.medicinereminder.screens.AddMedicineScreen
import e4294395nasratullahuk.medicinereminder.screens.DashboardScreen
import e4294395nasratullahuk.medicinereminder.screens.GlobalMedicineHistoryListScreen
import e4294395nasratullahuk.medicinereminder.screens.MedicineHistoryScreen
import e4294395nasratullahuk.medicinereminder.screens.MedicineListScreen
import e4294395nasratullahuk.medicinereminder.ui.theme.MedicineReminderTheme
import projectapp.medicinereminder.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationScheduler.createNotificationChannels(this)

        setContent {
            MedicineReminderTheme {
                val context = LocalContext.current
                val viewModel: MedicineViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = MedicineViewModel.MedicineViewModelFactory(context.applicationContext as Application)
                )
                MedicineReminderApp(viewModel)
            }
        }
    }
}

@Composable
fun MedicineReminderApp(viewModel: MedicineViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationScreens.Splash.route) {

        composable(NavigationScreens.Splash.route)
        {
            SplashScreen(navController)
        }

        composable(NavigationScreens.Home.route) {
            DashboardScreen(navController = navController)
        }
        composable("add_medicine") {
            AddMedicineScreen(navController = navController, viewModel = viewModel)
        }
        composable("view_medicines") {
            MedicineListScreen(navController = navController, viewModel = viewModel)
        }
        // New route for the global history list screen
        composable("global_medicine_history_list") {
            GlobalMedicineHistoryListScreen(navController = navController, viewModel = viewModel)
        }

        composable("medicine_history/{medicineId}") { backStackEntry ->
            val medicineId = backStackEntry.arguments?.getString("medicineId")?.toIntOrNull()
            if (medicineId != null) {
                MedicineHistoryScreen(navController = navController, viewModel = viewModel, medicineId = medicineId)
            } else {
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    // No need for mutableStateOf 'showSplash' anymore, LaunchedEffect directly navigates
    // No need for LocalContext as Activity for navigation

    val context = LocalContext.current as Activity


    LaunchedEffect(Unit) {
        delay(3000) // Delay for 3 seconds
        // Navigate to the Login screen

//        val UserStatus = UserDetails.getUserLoginStatus(context)
//
//        if (UserStatus) {
            navController.navigate(NavigationScreens.Home.route) {
                // This pops up to the start destination (Splash) and removes it
                popUpTo(NavigationScreens.Splash.route) {
                    inclusive = true
                }
            }
//        } else {
//            navController.navigate(AppDestinations.Login.route) {
//                // This pops up to the start destination (Splash) and removes it
//                popUpTo(AppDestinations.Splash.route) {
//                    inclusive = true
//                }
//            }
//        }


    }

    MedicineReminderSplashScreenDesign() // Your actual splash screen UI
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
            .background(MaterialTheme.colorScheme.primary), // Use your PrimaryDark color for the background
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
                color = MaterialTheme.colorScheme.onPrimary
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
    MedicineReminderSplashScreenDesign(
        appName = "PillPal",
        personName = "By Dr. Reminder"
    )
}