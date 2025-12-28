package com.example.employeeperformancetracker.ui.employee_dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDashboardScreen(navController: NavController) {
    val accentColor = Color(0xFF43A047)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hello, Sarah", fontWeight = FontWeight.Bold)
                        Text("EMP001", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("SJ", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        BadgedBox(badge = { Badge() }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFFAFAFA))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WelcomeCard()
            PerformanceRatingCard()
            TasksAssignedCard()
            AttendanceStatusCard()
            DepartmentCard()
            QuickActions(navController)
        }
    }
}

@Composable
fun WelcomeCard() {
    val accentColor = Color(0xFF43A047)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = accentColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Welcome Back!", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
                Text("You have 5 pending tasks today", color = Color.White.copy(alpha = 0.9f))
            }
            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
        }
    }
}

@Composable
fun PerformanceRatingCard() {
    InfoCard(icon = Icons.Default.Star, title = "Performance Rating", primaryText = "4.5/5.0", iconTint = Color(0xFFFBC02D))
}

@Composable
fun TasksAssignedCard() {
    InfoCard(icon = Icons.Default.CheckCircleOutline, title = "Tasks Assigned", primaryText = "12", secondaryText = "5 Pending", iconTint = Color(0xFF3949AB))
}

@Composable
fun AttendanceStatusCard() {
    InfoCard(icon = Icons.Default.DateRange, title = "Attendance Status", primaryText = "Present", isChip = true, iconTint = Color(0xFF43A047))
}

@Composable
fun DepartmentCard() {
    InfoCard(icon = Icons.Default.Apartment, title = "Department", primaryText = "Engineering", isChip = true, iconTint = Color(0xFF1E88E5), isNavigable = true)
}


@Composable
fun QuickActions(navController: NavController) {
    Column {
        Text("Quick Actions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            QuickActionItem(icon = Icons.Default.Checklist, text = "View Tasks", color = Color(0xFFE8EAF6), onClick = { navController.navigate(Screen.EmployeeTasks.route) })
            QuickActionItem(icon = Icons.Default.StarBorder, text = "Performance", color = Color(0xFFE8F5E9), onClick = { navController.navigate(Screen.EmployeePerformance.route) })
            QuickActionItem(icon = Icons.Default.CalendarToday, text = "Attendance", color = Color(0xFFFFF8E1), onClick = { navController.navigate(Screen.Attendance.route) })
            QuickActionItem(icon = Icons.Default.PersonOutline, text = "My Profile", color = Color(0xFFE1F5FE), onClick = { navController.navigate(Screen.EmployeeSelfProfile.route) })
        }
    }
}

@Composable
fun InfoCard(
    icon: ImageVector,
    title: String,
    primaryText: String,
    secondaryText: String? = null,
    isChip: Boolean = false,
    iconTint: Color,
    isNavigable: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = iconTint)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.Gray, fontSize = 14.sp)
                if (isChip) {
                    SuggestionChip(
                        onClick = { }, 
                        label = { Text(primaryText, fontWeight = FontWeight.Bold) },
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = iconTint.copy(alpha = 0.1f), labelColor = iconTint)
                    )
                } else {
                    Text(primaryText, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
                secondaryText?.let { Text(it, color = Color.Gray, fontSize = 14.sp) }
            }
            if(isNavigable) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Navigate", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun QuickActionItem(icon: ImageVector, text: String, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onClick, 
            shape = RoundedCornerShape(16.dp), 
            colors = ButtonDefaults.buttonColors(containerColor = color), 
            contentPadding = PaddingValues(24.dp),
            modifier = Modifier.size(80.dp)
        ) {
            Icon(icon, contentDescription = text, tint = Color.Black)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val accentColor = Color(0xFF43A047)
    NavigationBar(containerColor = Color.White) {
        val items = listOf(
            Screen.EmployeeDashboard to Icons.Default.Home,
            Screen.EmployeeTasks to Icons.Default.Checklist,
            Screen.EmployeePerformance to Icons.Default.BarChart,
            Screen.EmployeeSelfProfile to Icons.Default.Person
        )
        items.forEach { (screen, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = navController.currentDestination?.route == screen.route,
                onClick = { 
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = accentColor, unselectedIconColor = Color.Gray, selectedTextColor = accentColor, unselectedTextColor = Color.Gray)
            )
        }
    }
}