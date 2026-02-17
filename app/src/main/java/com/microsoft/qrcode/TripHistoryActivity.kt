package com.microsoft.qrcode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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


data class Trip(
    val from: String,
    val to: String,
    val duration: Int,
    val dateTime: String,
    val amount: String,
    val status: TripStatus
)

enum class TripStatus {
    Completed, Adjusted
}

val sampleTrips = listOf(
    Trip("Central Station", "North Park", 22, "Oct 24, 2023 · 08:30 AM", "$2.75", TripStatus.Completed),
    Trip("West End", "Downtown", 45, "Oct 23, 2023 · 05:15 PM", "$3.50", TripStatus.Completed),
    Trip("Airport", "Central Station", 30, "Oct 23, 2023 · 09:10 AM", "$5.00", TripStatus.Completed),
    Trip("University", "Library Plaza", 12, "Oct 22, 2023 · 11:45 AM", "$1.50", TripStatus.Completed),
    Trip("East Harbor", "North Park", 18, "Oct 22, 2023 · 08:15 AM", "$2.25", TripStatus.Completed),
    Trip("Downtown", "West End", 42, "Oct 21, 2023 · 06:40 PM", "$3.50", TripStatus.Adjusted),
    Trip("Central Station", "Airport", 32, "Oct 21, 2023 · 01:20 PM", "$5.00", TripStatus.Completed),
    Trip("North Park", "East Harbor", 15, "Oct 21, 2023 · 09:00 AM", "$2.25", TripStatus.Completed),
    Trip("Library Plaza", "University", 14, "Oct 20, 2023 · 04:30 PM", "$1.50", TripStatus.Completed),
    Trip("Stadium", "Central Station", 10, "Oct 20, 2023 · 07:45 PM", "$1.75", TripStatus.Completed)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripHistoryScreen(
    navController: NavController,
    trips: List<Trip> = sampleTrips
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip History") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { /* calendar */ }) {
                        Icon(Icons.Default.CalendarToday, null)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Text(
                text = "LAST 10 METRO TRIPS",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(trips) { trip ->
                    TripItem(trip)
                }
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Train,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${trip.from} → ${trip.to}",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${trip.duration} mins", fontSize = 12.sp, color = Color.Gray)
                }

                Text(
                    trip.dateTime,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    trip.amount,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                StatusChip(trip.status)
            }
        }
    }
}

@Composable
fun StatusChip(status: TripStatus) {
    val (bg, textColor) = when (status) {
        TripStatus.Completed -> Color(0xFFE6F4EA) to Color(0xFF1B5E20)
        TripStatus.Adjusted -> Color(0xFFFDECEA) to Color(0xFFC62828)
    }

    Box(
        modifier = Modifier
            .padding(top = 6.dp)
            .background(bg, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.name,
            fontSize = 11.sp,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TripHistoryScreenPreview() {
    QrcodeTheme {
        TripHistoryScreen(
            navController = rememberNavController(),
            trips = sampleTrips
        )
    }
}
