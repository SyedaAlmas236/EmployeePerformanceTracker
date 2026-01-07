package com.example.employeeperformancetracker.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.employeeperformancetracker.R
import com.example.employeeperformancetracker.data.Employee
import com.example.employeeperformancetracker.data.EmployeeRepository
import com.example.employeeperformancetracker.data.Task
import com.example.employeeperformancetracker.data.TaskRepository
import com.example.employeeperformancetracker.ui.navigation.Screen
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    var employees by remember { mutableStateOf<List<Employee>>(emptyList()) }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        employees = EmployeeRepository.getEmployees()
        if (employees.isEmpty()) {
            EmployeeRepository.fetchEmployees()
            employees = EmployeeRepository.getEmployees()
        }
        tasks = TaskRepository.getTasks()
        isLoading = false
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        containerColor = Color(0xFFF2F4F7)
    ) { paddingValues ->
        if (isLoading) {
             androidx.compose.material3.LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(paddingValues))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Header(scope, drawerState)
            KPISection(employees, tasks)
            TopPerformersSection(navController, employees)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Screen.Analytics.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3A8D))
            ) {
                Icon(Icons.Default.Analytics, contentDescription = "Analytics Icon", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "View Analytics", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Screen.RegisterEmployeeCredentials.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Register Icon", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Register Employee", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Header(scope: kotlinx.coroutines.CoroutineScope, drawerState: DrawerState) {
    Column(
        modifier = Modifier
            .background(
                Color(0xFF2A3A8D), 
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Placeholder
                contentDescription = "Admin Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Dashboard", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(text = "Welcome back, Admin", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
    }
}

@Composable
private fun KPISection(employees: List<Employee>, tasks: List<Task>) {
    val totalEmployees = employees.size
    val pendingTasks = tasks.count { it.status?.lowercase() == "pending" }
    
    // Calculate average rating safely
    val avgRating = if (employees.isNotEmpty()) {
        val ratings = employees.mapNotNull { it.rating }
        if (ratings.isNotEmpty()) "%.1f".format(ratings.average()) else "0.0"
    } else "0.0"

    // Find top performer safely
    val topPerformer = employees.maxByOrNull { it.rating ?: 0f }?.name ?: "N/A"

    Column(modifier = Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            KPICard(modifier = Modifier.weight(1f), title = "Total Employees", value = totalEmployees.toString(), icon = Icons.Default.Groups, iconColor = Color(0xFF3949AB))
            KPICard(modifier = Modifier.weight(1f), title = "Tasks Pending", value = pendingTasks.toString(), icon = Icons.Default.PendingActions, iconColor = Color(0xFFF57C00))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            KPICard(modifier = Modifier.weight(1f), title = "Average Rating", value = avgRating, icon = Icons.Default.Star, iconColor = Color(0xFF4CAF50))
            KPICard(modifier = Modifier.weight(1f), title = "Top Performer", value = topPerformer, icon = Icons.Default.TrendingUp, iconColor = Color(0xFFD32F2F))
        }
    }
}

@Composable
private fun KPICard(modifier: Modifier = Modifier, title: String, value: String, icon: ImageVector, iconColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 14.sp, color = Color.Gray)
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = if (title == "Top Performer") Color(0xFFD32F2F) else Color.Black)
        }
    }
}

@Composable
private fun TopPerformersSection(navController: NavController, employees: List<Employee>) {
    val topPerformers = employees
        .sortedByDescending { it.rating ?: 0f }
        .take(3)

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Star", tint = Color(0xFFF57C00))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Top 3 Performers", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            topPerformers.forEachIndexed { index, employee ->
                PerformerCard(employee = employee, rank = index + 1, navController = navController)
            }
        }
    }
}

@Composable
private fun PerformerCard(employee: Employee, rank: Int, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { navController.navigate("employee_profile/${employee.name}") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFFE8EAF6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "#$rank", fontWeight = FontWeight.Bold, color = Color(0xFF3949AB))
            }
            Spacer(modifier = Modifier.width(12.dp))
            AsyncImage(
                model = employee.profileImageUrl ?: "https://api.dicebear.com/7.x/avataaars/svg?seed=${employee.name}",
                contentDescription = employee.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = employee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = employee.position ?: "No position", color = Color.Gray, fontSize = 14.sp)
            }
            RatingBadge(rating = employee.rating ?: 0f)
        }
    }
}

@Composable
private fun RatingBadge(rating: Float) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF43A047))
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = rating.toString(), color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Dashboard, 
        Screen.EmployeeList, 
        Screen.TaskList, 
        Screen.Analytics, 
        Screen.Settings
    )
    NavigationBar {
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            screen.icon?.let {
                NavigationBarItem(
                    icon = { Icon(imageVector = it, contentDescription = screen.route) },
                    label = { Text(screen.route) },
                    selected = currentRoute == screen.route,
                    onClick = { navController.navigate(screen.route) }
                )
            }
        }
    }
}

@Composable
private fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
