package com.example.employeeperformancetracker.ui.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeAttendanceScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AttendanceSummaryCard()
            }
            item {
                AttendanceCalendarSection()
            }
            item {
                Button(
                    onClick = { navController.navigate("employee_apply_leave") },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                ) {
                    Text("Apply for Leave", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            item {
                Text("Recent Attendance", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            items(dummyRecentAttendance) { attendance ->
                RecentAttendanceItem(attendance)
            }
        }
    }
}

@Composable
fun AttendanceSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryItem("Present", "22", Color(0xFF43A047))
            SummaryItem("Absent", "02", Color(0xFFD32F2F))
            SummaryItem("Leaves", "01", Color(0xFF1E88E5))
        }
    }
}

@Composable
fun SummaryItem(label: String, count: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(count, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = color)
    }
}

@Composable
fun AttendanceCalendarSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("October 2024", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            SimpleCalendarGrid()
        }
    }
}

@Composable
fun SimpleCalendarGrid() {
    val days = (1..31).toList()
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        val rows = days.chunked(7)
        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { day ->
                    val statusColor = when(day) {
                        4, 15 -> Color(0xFFD32F2F).copy(alpha = 0.1f)
                        10 -> Color(0xFF1E88E5).copy(alpha = 0.1f)
                        in 1..22 -> Color(0xFF43A047).copy(alpha = 0.1f)
                        else -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier.weight(1f).aspectRatio(1f).padding(2.dp).clip(CircleShape).background(statusColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day.toString(), fontSize = 12.sp)
                    }
                }
                if (row.size < 7) {
                    repeat(7 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
fun RecentAttendanceItem(attendance: RecentAttendance) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(attendance.date, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Shift: 09:00 AM - 18:00 PM", color = Color.Gray, fontSize = 12.sp)
            }
            StatusChip(attendance.status)
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val color = when(status) {
        "Present" -> Color(0xFF43A047)
        "Absent" -> Color(0xFFD32F2F)
        else -> Color(0xFF1E88E5)
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(status, color = color, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

data class RecentAttendance(val date: String, val status: String)
val dummyRecentAttendance = listOf(
    RecentAttendance("Oct 22, 2024", "Present"),
    RecentAttendance("Oct 21, 2024", "Present"),
    RecentAttendance("Oct 20, 2024", "Absent"),
    RecentAttendance("Oct 19, 2024", "Present")
)
