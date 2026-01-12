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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.employeeperformancetracker.data.Employee
import com.example.employeeperformancetracker.data.EmployeeRepository
import com.example.employeeperformancetracker.data.Task
import com.example.employeeperformancetracker.data.TaskRepository
import com.example.employeeperformancetracker.data.auth.AuthViewModel
import com.example.employeeperformancetracker.ui.navigation.Screen
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val accentColor = Color(0xFF43A047)
    var employee by remember { mutableStateOf<Employee?>(null) }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var attendanceMarked by rememberSaveable { mutableStateOf(false) }
    var markedTime by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null) {
            employee = EmployeeRepository.getEmployeeByAuthId(currentUser.id)
            tasks = TaskRepository.getTasks().filter { it.assignedTo == employee?.id || it.assignedTo == employee?.userId }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hello, ${employee?.name ?: "Loading..."}", fontWeight = FontWeight.Bold)
                        Text(employee?.employeeId ?: "----", fontSize = 12.sp, color = Color.Gray)
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
                        Text(
                            text = (employee?.name?.take(2)?.uppercase() ?: "??"),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("employee_notifications") }) {
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
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accentColor)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFFFAFAFA))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Attendance Marking Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = accentColor)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Today's Attendance",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        if (!attendanceMarked) {
                            Text(
                                text = "Mark your attendance for today",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        attendanceMarked = true
                                        markedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = accentColor
                                    ),
                                    contentPadding = PaddingValues(vertical = 12.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Mark Present", fontWeight = FontWeight.SemiBold)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                                Text("Present", color = Color.White, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Marked at $markedTime",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                WelcomeCard(tasks)
                PerformanceRatingCard(employee)
                TasksAssignedCard(tasks)
                AttendanceStatusCard()
                DepartmentCard(employee)
                QuickActions(navController)
            }
        }
    }
}

@Composable
fun WelcomeCard(tasks: List<Task>) {
    val accentColor = Color(0xFF43A047)
    val pendingCount = tasks.count { 
        val s = it.status?.lowercase()?.replace("_", " ")
        s == "pending" || s == "not started" || s == "in progress"
    }
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
                Text("You have $pendingCount pending tasks today", color = Color.White.copy(alpha = 0.9f))
            }
            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
        }
    }
}

@Composable
fun PerformanceRatingCard(employee: Employee?) {
    val rating = employee?.rating ?: 0f
    InfoCard(icon = Icons.Default.Star, title = "Performance Rating", primaryText = "$rating/5.0", iconTint = Color(0xFFFBC02D))
}

@Composable
fun TasksAssignedCard(tasks: List<Task>) {
    val pendingCount = tasks.count { 
        val s = it.status?.lowercase()?.replace("_", " ")
        s == "pending" || s == "not started" || s == "in progress"
    }
    val completedCount = tasks.count { 
        val s = it.status?.lowercase()?.replace("_", " ")
        s == "completed" 
    }
    
    InfoCard(
        icon = Icons.Default.CheckCircleOutline, 
        title = "Tasks Status", 
        primaryText = "${tasks.size} Total", 
        secondaryText = "$pendingCount Pending, $completedCount Completed", 
        iconTint = Color(0xFF3949AB)
    )
}

@Composable
fun AttendanceStatusCard() {
    InfoCard(icon = Icons.Default.DateRange, title = "Attendance Status", primaryText = "Present", isChip = true, iconTint = Color(0xFF43A047))
}

@Composable
fun AttendanceStatusCard(navController: NavController) {
    InfoCard(icon = Icons.Default.DateRange, title = "Attendance Status", primaryText = "Present", isChip = true, iconTint = Color(0xFF43A047))
}

@Composable
fun DepartmentCard(employee: Employee?) {
    InfoCard(icon = Icons.Default.Apartment, title = "Department", primaryText = employee?.department ?: "N/A", isChip = true, iconTint = Color(0xFF1E88E5))
}


@Composable
fun QuickActions(navController: NavController) {
    Column {
        Text("Quick Actions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            QuickActionItem(icon = Icons.Default.Checklist, text = "View Tasks", color = Color(0xFFE8EAF6), onClick = { navController.navigate(Screen.EmployeeTasks.route) })
            QuickActionItem(icon = Icons.Default.StarBorder, text = "Performance", color = Color(0xFFE8F5E9), onClick = { navController.navigate(Screen.EmployeePerformance.route) })
            QuickActionItem(icon = Icons.Default.CalendarToday, text = "Attendance", color = Color(0xFFFFF8E1), onClick = { navController.navigate("employee_attendance") })
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