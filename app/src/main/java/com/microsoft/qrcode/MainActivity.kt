package com.microsoft.qrcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.microsoft.qrcode.ui.theme.QrcodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QrcodeTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController      = navController,
        startDestination   = "login",
        enterTransition    = { fadeIn(animationSpec     = tween(1500)) },
        exitTransition     = { fadeOut(animationSpec    = tween(1500)) },
        popEnterTransition = { fadeIn(animationSpec     = tween(1500)) },
        popExitTransition  = { fadeOut(animationSpec    = tween(1500)) }
    ) {
        composable("login") { LoginScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
        composable("changepassword") { ChangePasswordScreen(navController) }
        composable("editprofile") { EditProfileScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("triphistory") { TripHistoryScreen(navController) }
        composable("activetickets") { ActiveTicketScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
    }
}
