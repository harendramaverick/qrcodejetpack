package com.microsoft.qrcode

object AuthValidator {
    
    fun isPasswordValid(password: String): Boolean {
        val hasMinLength = password.length >= 8
        val hasNumber = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        return hasMinLength && hasNumber && hasSpecial
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    // Note: Since android.util.Patterns requires Android, 
    // for pure Unit Tests we often use a Regex instead:
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$".toRegex()
        return email.matches(emailRegex)
    }
}
