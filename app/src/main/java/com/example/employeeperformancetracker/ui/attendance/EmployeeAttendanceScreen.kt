package com.example.employeeperformancetracker.ui.attendance

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.data.LeaveRepository
import com.example.employeeperformancetracker.data.LeaveRequest
import com.example.employeeperformancetracker.data.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Serializable
data class AttendanceRecord(
    val id: String? = null,
    val user_id: String,
    val date: String,
    val status: String,
    val marked_at: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeAttendanceScreen(navController: NavController) {
    var attendanceRecords by remember { mutableStateOf<List<AttendanceRecord>>(emptyList()) }
    var leaveRequests by remember { mutableStateOf<List<LeaveRequest>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    val currentMonthDate = remember { LocalDate.now() }
    val monthName = remember { currentMonthDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)) }

    LaunchedEffect(Unit) {
        try {
            val supabase = SupabaseConfig.client
            val userId = supabase.auth.currentUserOrNull()?.id
            if (userId != null) {
                val startOfMonth = YearMonth.now().atDay(1).toString()
                val endOfMonth = YearMonth.now().atEndOfMonth().toString()

                // Fetch Attendance for current month
                val records = supabase.from("attendance").select {
                    filter {
                        eq("user_id", userId)
                        gte("date", startOfMonth)
                        lte("date", endOfMonth)
                    }
                }.decodeList<AttendanceRecord>()
                attendanceRecords = records

                // Fetch Leaves
                leaveRequests = LeaveRepository.getAllLeaveRequests(supabase).filter { 
                    it.user_id == userId && it.status == "approved"
                }
            }
        } catch (e: Exception) {
            Log.e("EmployeeAttendance", "Error fetching data", e)
        } finally {
            isLoading = false
        }
    }

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
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF43A047))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    val presentCount = attendanceRecords.count { it.status == "present" }
                    val leaveCount = countLeaveDaysInCurrentMonth(leaveRequests)
                    val absentCount = countAbsentDaysUntilNow(attendanceRecords, leaveRequests)

                    AttendanceSummaryCard(
                        presentCount = presentCount.toString().padStart(2, '0'),
                        absentCount = absentCount.toString().padStart(2, '0'),
                        leaveCount = leaveCount.toString().padStart(2, '0')
                    )
                }
                item {
                    AttendanceCalendarSection(monthName, attendanceRecords, leaveRequests)
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
                val recentRecords = attendanceRecords.sortedByDescending { it.date }.take(5)
                items(recentRecords) { attendance ->
                    RecentAttendanceItem(attendance)
                }
            }
        }
    }
}

fun countLeaveDaysInCurrentMonth(leaveRequests: List<LeaveRequest>): Int {
    val today = LocalDate.now()
    val startOfMonth = today.withDayOfMonth(1)
    val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())
    var count = 0
    
    leaveRequests.forEach { leave ->
        val fromDate = LocalDate.parse(leave.from_date)
        val toDate = LocalDate.parse(leave.to_date)
        
        var current = fromDate
        while (!current.isAfter(toDate)) {
            if (!current.isBefore(startOfMonth) && !current.isAfter(endOfMonth)) {
                count++
            }
            current = current.plusDays(1)
        }
    }
    return count
}

fun countAbsentDaysUntilNow(attendance: List<AttendanceRecord>, leaves: List<LeaveRequest>): Int {
    val today = LocalDate.now()
    val startOfMonth = today.withDayOfMonth(1)
    val cutoffTime = LocalTime.of(13, 0)
    var count = 0

    var current = startOfMonth
    while (current.isBefore(today)) {
        val dateStr = current.toString()
        val isPresent = attendance.any { it.date == dateStr && it.status == "present" }
        val isLeave = leaves.any { leave ->
            val from = LocalDate.parse(leave.from_date)
            val to = LocalDate.parse(leave.to_date)
            !current.isBefore(from) && !current.isAfter(to)
        }
        
        if (!isPresent && !isLeave) {
            count++
        }
        current = current.plusDays(1)
    }
    
    // Check for today after 1 PM
    if (LocalTime.now().isAfter(cutoffTime)) {
        val dateStr = today.toString()
        val isPresent = attendance.any { it.date == dateStr && it.status == "present" }
        val isLeave = leaves.any { leave ->
            val from = LocalDate.parse(leave.from_date)
            val to = LocalDate.parse(leave.to_date)
            !today.isBefore(from) && !today.isAfter(to)
        }
        if (!isPresent && !isLeave) {
            count++
        }
    }
    
    return count
}

@Composable
fun AttendanceSummaryCard(presentCount: String, absentCount: String, leaveCount: String) {
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
            SummaryItem("Present", presentCount, Color(0xFF43A047))
            SummaryItem("Absent", absentCount, Color(0xFFD32F2F))
            SummaryItem("Leaves", leaveCount, Color(0xFF1E88E5))
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
fun AttendanceCalendarSection(monthName: String, attendanceRecords: List<AttendanceRecord>, leaveRequests: List<LeaveRequest>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(monthName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            SimpleCalendarGrid(attendanceRecords, leaveRequests)
        }
    }
}

@Composable
fun SimpleCalendarGrid(attendanceRecords: List<AttendanceRecord>, leaveRequests: List<LeaveRequest>) {
    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val days = (1..daysInMonth).toList()
    val today = LocalDate.now()
    val cutoffTime = LocalTime.of(13, 0)

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
                    val currentDayDate = currentMonth.atDay(day)
                    val dateString = currentDayDate.toString()
                    
                    val isPresent = attendanceRecords.any { it.date == dateString && it.status == "present" }
                    val isApprovedLeave = leaveRequests.any { leave ->
                        val from = LocalDate.parse(leave.from_date)
                        val to = LocalDate.parse(leave.to_date)
                        !currentDayDate.isBefore(from) && !currentDayDate.isAfter(to)
                    }
                    
                    val isAbsent = when {
                        isPresent || isApprovedLeave -> false
                        currentDayDate.isBefore(today) -> true
                        currentDayDate == today && LocalTime.now().isAfter(cutoffTime) -> true
                        else -> false
                    }
                    
                    val statusColor = when {
                        isPresent -> Color(0xFF43A047) // Green
                        isApprovedLeave -> Color(0xFF1E88E5) // Blue
                        isAbsent -> Color(0xFFD32F2F) // Red
                        else -> Color.Transparent
                    }
                    
                    val isToday = currentDayDate == today
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(statusColor.copy(alpha = if (statusColor == Color.Transparent) 0f else 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = statusColor,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    day.toString(), 
                                    fontSize = 11.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = if (statusColor == Color.Transparent) Color.Black else Color.White
                                )
                            }
                        }
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
fun RecentAttendanceItem(attendance: AttendanceRecord) {
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
                val formattedDate = try {
                    LocalDate.parse(attendance.date).format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH))
                } catch (e: Exception) {
                    attendance.date
                }
                Text(formattedDate, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Shift: 09:00 AM - 18:00 PM", color = Color.Gray, fontSize = 12.sp)
            }
            StatusChip(attendance.status.replaceFirstChar { it.uppercase() })
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
