package com.microsoft.qrcode

import org.junit.Test
import org.junit.Assert.*

class AuthValidatorTest {

    @Test
    fun isPasswordValid_ShortPassword_ReturnsFalse() {
        val result = AuthValidator.isPasswordValid("abc1!")
        assertFalse(result)
    }

    @Test
    fun isPasswordValid_NoNumber_ReturnsFalse() {
        val result = AuthValidator.isPasswordValid("abcdefgh!")
        assertFalse(result)
    }

    @Test
    fun isPasswordValid_NoSpecialChar_ReturnsFalse() {
        val result = AuthValidator.isPasswordValid("abcdefgh1")
        assertFalse(result)
    }

    @Test
    fun isPasswordValid_CorrectPassword_ReturnsTrue() {
        val result = AuthValidator.isPasswordValid("StrongP@ss123")
        assertTrue(result)
    }

    @Test
    fun isValidEmail_Empty_ReturnsFalse() {
        assertFalse(AuthValidator.isValidEmail(""))
    }

    @Test
    fun isValidEmail_ValidFormat_ReturnsTrue() {
        assertTrue(AuthValidator.isValidEmail("john.doe@example.com"))
    }

    @Test
    fun isValidEmail_InvalidFormat_ReturnsFalse() {
        assertFalse(AuthValidator.isValidEmail("invalid-email-format"))
    }
}
