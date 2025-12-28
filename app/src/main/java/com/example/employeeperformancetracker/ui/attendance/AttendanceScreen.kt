package com.example.employeeperformancetracker.ui.attendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

private val PrimaryBlue = Color(0xFF3949AB)
private val BackgroundGray = Color(0xFFFAFAFA)
private val GreenColor = Color(0xFF4CAF50)
private val OrangeColor = Color(0xFFF57C00)
private val BlueColor = Color(0xFF3949AB)

@Composable
fun AttendanceScreen(
    navController: NavController,
    attendanceViewModel: AttendanceViewModel = viewModel()
) {
    val attendanceList by attendanceViewModel.attendanceList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        Header(navController)
        SummaryCards(attendanceViewModel.presentCount, attendanceViewModel.onLeaveCount)
        Spacer(modifier = Modifier.height(16.dp))
        TodaysAttendance(attendanceList)
    }
}

@Composable
fun Header(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = PrimaryBlue,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(top = 16.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Attendance", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Tuesday, December 2, 2025", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SummaryCards(presentCount: String, onLeaveCount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SummaryCard(icon = Icons.Default.Person, label = "Present", count = presentCount, color = GreenColor)
        SummaryCard(icon = Icons.Default.PersonOff, label = "On Leave", count = onLeaveCount, color = BlueColor)
    }
}

@Composable
fun SummaryCard(icon: ImageVector, label: String, count: String, color: Color) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .width(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = color)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(label, fontSize = 14.sp, color = Color.Gray)
                Text(count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            }
        }
    }
}

@Composable
fun TodaysAttendance(attendanceList: List<AttendanceData>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Today's Attendance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(attendanceList) { attendance ->
                AttendanceCard(attendance = attendance)
            }
        }
    }
}

@Composable
fun AttendanceCard(attendance: AttendanceData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = attendance.image),
                contentDescription = attendance.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(attendance.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = "Time", modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(attendance.time, fontSize = 14.sp, color = Color.Gray)
                }
            }
            StatusBadge(status = attendance.status)
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Present" -> GreenColor to Color.White
        "On Leave" -> BlueColor to Color.White
        "Late" -> OrangeColor to Color.White
        else -> Color.Gray to Color.White
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Text(
            text = status,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
