package com.microsoft.qrcode

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.microsoft.qrcode.ui.theme.QrcodeTheme

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        // App Icon
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = Color(0xFF1E6CF1),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_train),
                contentDescription = "App Icon",
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome back",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ready for your next journey?",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email / Mobile
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email / Mobile Number") },
            placeholder = { Text("Enter your email or phone") },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = "Forgot Password?",
                color = Color(0xFF1E6CF1),
                modifier = Modifier.clickable(onClick = {navController.navigate("changepassword")})
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = {
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E6CF1)
            )
        ) {
            Text(
                text = "Login",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Login"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // New Commuter
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "  New Commuter?  ",
                color = Color.Gray
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create Account
        OutlinedButton(
            onClick = { navController.navigate("registration") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Text(
                text = "Create an Account",
                fontSize = 16.sp,
                color = Color(0xFF1E6CF1)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    QrcodeTheme {
        LoginScreen(navController = rememberNavController())
    }
}
