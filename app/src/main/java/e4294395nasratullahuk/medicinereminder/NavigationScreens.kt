package e4294395nasratullahuk.medicinereminder


sealed class NavigationScreens(val route: String) {
    object Splash : NavigationScreens("splash_route")
    object Home : NavigationScreens("dashboard")
}