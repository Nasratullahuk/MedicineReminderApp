package e4294395nasratullahuk.medicinereminder

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Toast
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
import e4294395nasratullahuk.medicinereminder.database.MedicineViewModel
import e4294395nasratullahuk.medicinereminder.notifications.NotificationScheduler
import e4294395nasratullahuk.medicinereminder.screens.AboutUsScreen
import e4294395nasratullahuk.medicinereminder.screens.AddMedicineScreen
import e4294395nasratullahuk.medicinereminder.screens.DashboardScreen
import e4294395nasratullahuk.medicinereminder.screens.GlobalMedicineHistoryListScreen
import e4294395nasratullahuk.medicinereminder.screens.LoginScreen
import e4294395nasratullahuk.medicinereminder.screens.MedicineHistoryScreen
import e4294395nasratullahuk.medicinereminder.screens.MedicineListScreen
import e4294395nasratullahuk.medicinereminder.screens.ProfileScreen
import e4294395nasratullahuk.medicinereminder.screens.RegistrationScreen
import e4294395nasratullahuk.medicinereminder.ui.theme.MedicineReminderTheme
import kotlinx.coroutines.delay

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

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegistrationScreen(navController = navController)
        }
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }
        composable("add_medicine") {
            AddMedicineScreen(navController = navController, viewModel = viewModel)
        }
        composable("view_medicines") {
            MedicineListScreen(navController = navController, viewModel = viewModel)
        }
        composable("global_medicine_history_list") {
            GlobalMedicineHistoryListScreen(navController = navController, viewModel = viewModel)
        }
        composable("medicine_history/{medicineId}") { backStackEntry ->
            val medicineId = backStackEntry.arguments?.getString("medicineId")?.toIntOrNull()
            if (medicineId != null) {
                MedicineHistoryScreen(navController = navController, viewModel = viewModel, medicineId = medicineId)
            } else {
                Toast.makeText(LocalContext.current, "Medicine ID not found for history.", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }

        composable("profile_screen") {
            ProfileScreen(navController = navController)
        }
        composable("about_us_screen") {
            AboutUsScreen(navController = navController)
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current as Activity


    LaunchedEffect(Unit) {
        delay(3000)

        val patientStatus = PatientData.getLoginStatus(context)
        if (patientStatus) {
            navController.navigate(NavigationScreens.Home.route) {
                popUpTo(NavigationScreens.Splash.route) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate("login") {
                popUpTo(NavigationScreens.Splash.route) {
                    inclusive = true
                }
            }
        }


    }

    MedicineReminderSplashScreenDesign()
}


@Composable
fun MedicineReminderSplashScreenDesign(
    appName: String = "MedReminder",
    personName: String = "By Nasratullahuk"
) {
    val primaryDark = colorResource(id = R.color.PrimaryDark)
    val textOnPrimaryDark = colorResource(id = R.color.text_on_primary_dark)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Medicine Reminder App",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = appName,
                fontSize = 48.sp, // Large font size for app name
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = personName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = textOnPrimaryDark.copy(alpha = 0.7f)
            )
        }
    }
}

