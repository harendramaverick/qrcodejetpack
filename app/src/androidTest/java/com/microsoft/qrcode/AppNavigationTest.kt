package com.microsoft.qrcode

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.microsoft.qrcode.ui.theme.QrcodeTheme
import org.junit.Rule
import org.junit.Test

class AppNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // --- Authentication & Account Flow ---

    @Test
    fun testLogin_SuccessNavigation() {
        composeTestRule.setContent {
            QrcodeTheme { AppNavigation() }
        }
        // Click Login using tag for better reliability
        composeTestRule.onNodeWithTag("loginButton").performClick()
        // Verify Dashboard
        composeTestRule.onNodeWithText("Dashboard").assertExists()
    }

    @Test
    fun testNavigation_ToRegistrationAndBack() {
        composeTestRule.setContent {
            QrcodeTheme { AppNavigation() }
        }
        // To Registration
        composeTestRule.onNodeWithText("Create an Account").performClick()
        composeTestRule.onNodeWithText("Join the Metro network").assertExists()
        
        // Back to Login via "Already have an account?"
        composeTestRule.onNodeWithText("Already have an account? Login").performClick()
        composeTestRule.onNodeWithText("Welcome back").assertExists()
    }

    @Test
    fun testNavigation_ForgotPassword() {
        composeTestRule.setContent {
            QrcodeTheme { AppNavigation() }
        }
        composeTestRule.onNodeWithText("Forgot Password?").performClick()
        composeTestRule.onNodeWithText("Secure your account").assertExists()
    }

    // --- Dashboard & Bottom Navigation Flow ---

    @Test
    fun testBottomNavigation_AllTabs() {
        composeTestRule.setContent {
            QrcodeTheme { AppNavigation() }
        }
        // Login to reach Dashboard
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // Home (Default)
        composeTestRule.onNodeWithText("Dashboard").assertExists()

        // History (In Bottom Bar label is "History", Screen header is "Trip History")
        composeTestRule.onNodeWithText("History").performClick()
        composeTestRule.onNodeWithText("Trip History").assertExists()

        // Tickets (In Bottom Bar label is "Tickets", Screen header in ActiveTicketScreen is "Tickets")
        composeTestRule.onNodeWithText("Tickets").performClick()
        // Note: ActiveTicketScreen TopAppBar title is "Tickets", but it also has "CURRENT JOURNEY" text
        composeTestRule.onNodeWithText("CURRENT JOURNEY").assertExists()

        // Profile
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("Personal Information").assertExists()
    }

    // --- Profile & Settings Flow ---

    @Test
    fun testProfile_Logout() {
        composeTestRule.setContent {
            QrcodeTheme { AppNavigation() }
        }
        // Navigate to Profile
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.onNodeWithText("Profile").performClick()

        // Click Logout
        composeTestRule.onNodeWithText("Logout").performClick()
        
        // Verify back at Login
        composeTestRule.onNodeWithText("Welcome back").assertExists()
    }

    @Test
    fun testProfile_SettingsOptionsExist() {
        composeTestRule.setContent {
            QrcodeTheme { AppNavigation() }
        }
        // Navigate to Profile
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.onNodeWithText("Profile").performClick()

        // Verify options
        composeTestRule.onNodeWithText("Personal Information").assertExists()
        composeTestRule.onNodeWithText("Payment Methods").assertExists()
        composeTestRule.onNodeWithText("Trip Statistics").assertExists()
        composeTestRule.onNodeWithText("Notification Settings").assertExists()
        composeTestRule.onNodeWithText("Security & Password").assertExists()
        composeTestRule.onNodeWithText("Support").assertExists()
    }

    // --- Content Interaction Tests ---

    @Test
    fun testLogin_InputFields() {
        composeTestRule.setContent {
            QrcodeTheme { AppNavigation() }
        }
        
        // Find using tags for the input fields as labels might be unmerged or ambiguous
        composeTestRule.onNodeWithTag("emailInput").performTextInput("user@metro.com")
        composeTestRule.onNodeWithTag("passwordInput").performTextInput("password123")

        // Verify that the text was actually entered
        composeTestRule.onNodeWithText("user@metro.com").assertExists()
        
        // Click Login
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // Verify Dashboard
        composeTestRule.onNodeWithText("Dashboard").assertExists()
    }

    @Test
    fun testDashboard_BalanceCardExists() {
        composeTestRule.setContent {
            QrcodeTheme { AppNavigation() }
        }
        composeTestRule.onNodeWithTag("loginButton").performClick()
        
        composeTestRule.onNodeWithText("Available Balance").assertExists()
        composeTestRule.onNodeWithText("$25.50").assertExists()
    }

    @Test
    fun testDashboard_BookingSection() {
        composeTestRule.setContent {
            QrcodeTheme { AppNavigation() }
        }
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // In DashboardScreen, "Book QR Ticket" appears twice: once as a header and once on the button.
        // We use onAllNodesWithText and check for at least one to avoid AmbiguousSearchException.
        composeTestRule.onAllNodesWithText("Book QR Ticket").onFirst().assertExists()
        
        // Verify station text
        composeTestRule.onNodeWithText("Central Station").assertExists()
    }
}
