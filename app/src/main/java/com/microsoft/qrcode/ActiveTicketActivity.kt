package com.microsoft.qrcode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.microsoft.qrcode.compose.BottomNavigationBar
import com.microsoft.qrcode.ui.theme.QrcodeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveTicketScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tickets") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                SectionTitle("CURRENT JOURNEY")
                ActiveTicketCard()
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { navController.navigate("trip_history") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Full History")
                }
            }
        }
    }
}
@Composable
fun ActiveTicketCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFF2ECC71), CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "ACTIVE",
                    color = Color(0xFF2ECC71),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Central Station to Airport T3",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Today, 10:45 AM · Zone 1-3",
                        color = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            Color(0xFFFFE0CC),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.QrCode,
                        contentDescription = null,
                        tint = Color(0xFFFB8C00),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Button(
                onClick = { /* view ticket */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.QrCode, null)
                Spacer(Modifier.width(8.dp))
                Text("View Ticket", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Gray
    )
}

@Preview(showBackground = true)
@Composable
fun ActiveTicketScreenPreview() {
    QrcodeTheme {
        ActiveTicketScreen(navController = rememberNavController())
    }
}
