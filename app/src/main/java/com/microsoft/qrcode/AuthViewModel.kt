package com.microsoft.qrcode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// --- Data Classes (Models) ---

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String? = null,
    val expiration: String? = null
)

// --- UI State ---

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val data: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

// --- ViewModel ---

class AuthViewModel : ViewModel() {

    // Holding the Ktor Client
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    var loginUiState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    /**
     * Executes the Login API
     */
    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            loginUiState = LoginUiState.Error("Username and password cannot be empty")
            return
        }

        viewModelScope.launch {
            loginUiState = LoginUiState.Loading
            try {
                // 10.0.2.2 is the special alias to your host loopback interface (i.e., 127.0.0.1 on your development machine)
                val response = client.post("http://10.0.2.2:4200/api/Authenticate/login") {
                    contentType(ContentType.Application.Json)
                    setBody(LoginRequest(username, password))
                }

                when (response.status) {
                    HttpStatusCode.OK -> {
                        val loginData: LoginResponse = response.body()
                        if (loginData.token != null) {
                            loginUiState = LoginUiState.Success(loginData)
                        } else {
                            loginUiState = LoginUiState.Error("Login failed: No token received")
                        }
                    }
                    HttpStatusCode.Unauthorized -> {
                        loginUiState = LoginUiState.Error("Invalid username or password")
                    }
                    else -> {
                        loginUiState = LoginUiState.Error("Server error: ${response.status.description}")
                    }
                }
            } catch (e: Exception) {
                loginUiState = LoginUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close() // Close the client when ViewModel is destroyed
    }
}
