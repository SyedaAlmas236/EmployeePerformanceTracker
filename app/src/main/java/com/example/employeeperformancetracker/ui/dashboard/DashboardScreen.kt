package com.example.employeeperformancetracker.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.employeeperformancetracker.R
import com.example.employeeperformancetracker.data.Employee
import com.example.employeeperformancetracker.data.EmployeeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, drawerState: DrawerState) {
    Scaffold(
        topBar = { TopBar() },
        containerColor = Color(0xFFF2F4F7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Header()
            KPISection()
            TopPerformersSection()
        }
    }
}

@Composable
private fun TopBar() {
    // This would typically be a more dynamic TopAppBar
    // For this prototype, it's simplified
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A3A8D))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Dashboard", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Welcome back, Admin", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        }
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // Placeholder
            contentDescription = "Admin Profile",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun KPISection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            KPICard(modifier = Modifier.weight(1f), title = "Total Employees", value = "124", icon = Icons.Default.Groups, iconColor = Color(0xFF3949AB))
            KPICard(modifier = Modifier.weight(1f), title = "Tasks Pending", value = "37", icon = Icons.Default.PendingActions, iconColor = Color(0xFFF57C00))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            KPICard(modifier = Modifier.weight(1f), title = "Average Rating", value = "4.2", icon = Icons.Default.Star, iconColor = Color(0xFF4CAF50))
            KPICard(modifier = Modifier.weight(1f), title = "Top Performer", value = "Sarah M.", icon = Icons.Default.TrendingUp, iconColor = Color(0xFFD32F2F))
        }
    }
}

@Composable
private fun KPICard(modifier: Modifier = Modifier, title: String, value: String, icon: ImageVector, iconColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 14.sp, color = Color.Gray)
                Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TopPerformersSection() {
    val topPerformers = EmployeeRepository.getEmployees().sortedByDescending { it.rating }.take(3)

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Star", tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Top 3 Performers", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            topPerformers.forEachIndexed { index, employee ->
                PerformerCard(employee = employee, rank = index + 1)
            }
        }
    }
}

@Composable
private fun PerformerCard(employee: Employee, rank: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE8EAF6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "#$rank", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painter = painterResource(id = employee.imageRes),
                contentDescription = employee.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = employee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = employee.role, color = Color.Gray, fontSize = 14.sp)
            }
            RatingBadge(rating = employee.rating)
        }
    }
}

@Composable
private fun RatingBadge(rating: Float) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = rating.toString(), color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
