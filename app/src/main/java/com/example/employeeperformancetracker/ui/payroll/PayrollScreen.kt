package com.example.employeeperformancetracker.ui.payroll

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

private val PrimaryBlue = Color(0xFF3949AB)
private val GreenColor = Color(0xFF4CAF50)
private val LightGreen = Color(0xFFE8F5E9)
private val BackgroundGray = Color(0xFFFAFAFA)

@Composable
fun PayrollScreen(
    navController: NavController,
    payrollViewModel: PayrollViewModel = viewModel()
) {
    val payrollHistory by payrollViewModel.payrollHistory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        Header(navController)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CurrentSalaryCard(
                salary = payrollViewModel.currentMonthSalary,
                month = payrollViewModel.currentMonth
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Payroll History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            PayrollHistoryList(payrollHistory)
            Spacer(modifier = Modifier.weight(1f))
            DownloadPayslipButton()
        }
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
            .padding(top = 16.dp, bottom = 48.dp, start = 16.dp, end = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Payroll", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("View salary & payments", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun CurrentSalaryCard(salary: String, month: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-32).dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GreenColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Current Month Salary", color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(salary, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = "Month",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(month, color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun PayrollHistoryList(payrollHistory: List<PayrollHistoryItem>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(payrollHistory) { item ->
            PayrollHistoryItem(item)
        }
    }
}

@Composable
fun PayrollHistoryItem(item: PayrollHistoryItem) {
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(LightGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("$", fontSize = 24.sp, color = GreenColor, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.month, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Date",
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(item.date, fontSize = 14.sp, color = Color.Gray)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(item.amount, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                StatusBadge(status = item.status)
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightGreen)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Status",
                tint = GreenColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status,
                color = GreenColor,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun DownloadPayslipButton() {
    Button(
        onClick = { /* TODO: Implement payslip download */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Icon(Icons.Default.CloudDownload, contentDescription = "Download")
        Spacer(Modifier.width(8.dp))
        Text("Download Payslip", fontSize = 16.sp)
    }
}
