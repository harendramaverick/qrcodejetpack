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

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val phoneNo: String,
    val password: String
)

@Serializable
data class RegisterResponse(
    val status: String? = null,
    val message: String? = null
)

@Serializable
data class UserDetails(
    val userName: String? = null,
    val email: String? = null,
    val phoneNo: String? = null
)

// --- UI State ---

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val data: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val message: String) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

sealed class UserDetailsUiState {
    object Idle : UserDetailsUiState()
    object Loading : UserDetailsUiState()
    data class Success(val data: UserDetails) : UserDetailsUiState()
    data class Error(val message: String) : UserDetailsUiState()
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

    var registerUiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    var userDetailsUiState by mutableStateOf<UserDetailsUiState>(UserDetailsUiState.Idle)
        private set

    var loggedInUsername by mutableStateOf<String?>(null)
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
                            loggedInUsername = username
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

    /**
     * Executes the Registration API
     */
    fun register(username: String, email: String, password: String, phone: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank() || phone.isBlank()) {
            registerUiState = RegisterUiState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            registerUiState = RegisterUiState.Loading
            try {
                val response = client.post("http://10.0.2.2:4200/api/Authenticate/register") {
                    contentType(ContentType.Application.Json)
                    setBody(RegisterRequest(username, email, phone, password))
                }

                when (response.status) {
                    HttpStatusCode.OK, HttpStatusCode.Created -> {
                        registerUiState = RegisterUiState.Success("Registration successful!")
                    }
                    HttpStatusCode.Conflict -> {
                        registerUiState = RegisterUiState.Error("User already exists")
                    }
                    else -> {
                        val errorBody = response.body<RegisterResponse>()
                        registerUiState = RegisterUiState.Error(errorBody.message ?: "Registration failed")
                    }
                }
            } catch (e: Exception) {
                registerUiState = RegisterUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    /**
     * Fetches User Details
     */
    fun fetchUserDetails(username: String) {
        viewModelScope.launch {
            userDetailsUiState = UserDetailsUiState.Loading
            try {
                val response = client.post("http://10.0.2.2:4200/api/Authenticate/userdetails") {
                    parameter("Username", username)
                    contentType(ContentType.Application.Json)
                    setBody("") // Empty body as per curl -d ''
                }

                if (response.status == HttpStatusCode.OK) {
                    val details: UserDetails = response.body()
                    userDetailsUiState = UserDetailsUiState.Success(details)
                } else {
                    userDetailsUiState = UserDetailsUiState.Error("Failed to fetch details: ${response.status}")
                }
            } catch (e: Exception) {
                userDetailsUiState = UserDetailsUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close() // Close the client when ViewModel is destroyed
    }

    fun logout() {
        loggedInUsername = null
        loginUiState = LoginUiState.Idle
        userDetailsUiState = UserDetailsUiState.Idle
    }
}
