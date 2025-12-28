package com.example.employeeperformancetracker.ui.reimbursements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

private val PrimaryBlue = Color(0xFF3949AB)
private val BackgroundGray = Color(0xFFFAFAFA)
private val GreenColor = Color(0xFF4CAF50)
private val OrangeColor = Color(0xFFF57C00)
private val RedColor = Color(0xFFD32F2F)

@Composable
fun ReimbursementsScreen(
    navController: NavController,
    reimbursementsViewModel: ReimbursementsViewModel = viewModel()
) {
    val reimbursements by reimbursementsViewModel.reimbursements.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
        ) {
            Header(navController)
            SummaryCards(
                approvedCount = reimbursementsViewModel.approvedCount,
                pendingCount = reimbursementsViewModel.pendingCount,
                rejectedCount = reimbursementsViewModel.rejectedCount
            )
            Spacer(modifier = Modifier.height(24.dp))
            MyReimbursements(reimbursements, modifier = Modifier.weight(1f))
        }

        FloatingActionButton(
            onClick = { /* TODO: Handle add reimbursement */ },
            containerColor = GreenColor,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.9f)
                .height(56.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = "Submit New Reimbursement")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Submit New Reimbursement", fontWeight = FontWeight.Bold)
            }
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
                Text("Reimbursements", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Submit & track expenses", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SummaryCards(approvedCount: String, pendingCount: String, rejectedCount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-32).dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        SummaryCard(label = "Approved", count = approvedCount, GreenColor)
        SummaryCard(label = "Pending", count = pendingCount, OrangeColor)
        SummaryCard(label = "Rejected", count = rejectedCount, RedColor)
    }
}

@Composable
fun SummaryCard(label: String, count: String, color: Color) {
    Card(
        modifier = Modifier
            .height(80.dp)
            .width(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(label, fontSize = 14.sp, color = Color.Gray)
            Text(count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun MyReimbursements(reimbursements: List<ReimbursementItem>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text("My Reimbursements", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 80.dp)) {
            items(reimbursements) { item ->
                ReimbursementItem(item)
            }
        }
    }
}

@Composable
fun ReimbursementItem(item: ReimbursementItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                StatusBadge(status = item.status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.type, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(item.amount, color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(item.date, color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (backgroundColor, textColor, icon) = when (status) {
        "Approved" -> Triple(GreenColor.copy(alpha = 0.1f), GreenColor, Icons.Default.CheckCircle)
        "Pending" -> Triple(OrangeColor.copy(alpha = 0.1f), OrangeColor, Icons.Default.HourglassEmpty)
        "Rejected" -> Triple(RedColor.copy(alpha = 0.1f), RedColor, Icons.Default.ThumbDown)
        else -> Triple(Color.Gray.copy(alpha = 0.1f), Color.Gray, Icons.Default.CheckCircle) // Should not happen
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = "Status",
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}
