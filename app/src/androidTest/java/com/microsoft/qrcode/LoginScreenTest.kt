package com.microsoft.qrcode

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.microsoft.qrcode.ui.theme.QrcodeTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_VerifyContent() {
        composeTestRule.setContent {
            QrcodeTheme {
                LoginScreen(navController = rememberNavController())
            }
        }

        // Verify that the welcome text is displayed
        composeTestRule.onNodeWithText("Welcome back").assertExists()
        
        // Verify that the login button is displayed
        composeTestRule.onNodeWithTag("loginButton").assertExists()
    }

    @Test
    fun loginScreen_EnterCredentials() {
        // We use AppNavigation so that the "dashboard" route exists when we click login
        composeTestRule.setContent {
            QrcodeTheme {
                AppNavigation()
            }
        }

        // Enter email
        composeTestRule.onNodeWithTag("emailInput").performTextInput("test@example.com")
        
        // Enter password
        composeTestRule.onNodeWithTag("passwordInput").performTextInput("password123")
        
        // Click Login
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // Verify that we navigated to the dashboard (Check for a text unique to Dashboard)
        // Note: Assuming DashboardScreen has "Dashboard" text.
        composeTestRule.onNodeWithText("Dashboard").assertExists()
    }
}
