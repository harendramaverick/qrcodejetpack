package com.microsoft.qrcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.microsoft.qrcode.ui.theme.QrcodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QrcodeTheme {
                val authViewModel: AuthViewModel = viewModel()
                AppNavigation(authViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController      = navController,
        startDestination   = "login",
        enterTransition    = { fadeIn(animationSpec = tween(300)) },
        exitTransition     = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition  = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("registration") { RegistrationScreen(navController, authViewModel) }
        composable("changepassword") { ChangePasswordScreen(navController) }
        composable("editprofile") { EditProfileScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("triphistory") { TripHistoryScreen(navController) }
        composable("activetickets") { ActiveTicketScreen(navController) }
        composable("profile") { ProfileScreen(navController, authViewModel) }
    }
}
