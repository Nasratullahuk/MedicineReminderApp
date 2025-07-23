package e4294395nasratullahuk.medicinereminder


sealed class NavigationScreens(val route: String) {
    object Splash : NavigationScreens("splash_route")
    object Login : NavigationScreens("login_route")
    object Home : NavigationScreens("dashboard")
    object Profile : NavigationScreens("profile_route")
    object DonationsHistory : NavigationScreens("donations_history_route")
    object Campaigns : NavigationScreens("campaigns_route")
}