package com.example.employeeperformancetracker.ui.employee_tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.employeeperformancetracker.ui.employee_dashboard.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTasksScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var employee by remember { mutableStateOf<Employee?>(null) }
    var allTasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedFilter by remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null) {
            employee = EmployeeRepository.getEmployeeByAuthId(currentUser.id)
            allTasks = TaskRepository.getTasks().filter { 
                it.assignedTo == employee?.id || it.assignedTo == employee?.userId 
            }
        }
        isLoading = false
    }

    val filteredTasks = remember(selectedFilter, allTasks) {
        when (selectedFilter) {
            "Pending" -> allTasks.filter { it.status == "In Progress" || it.status == "Not Started" }
            "Completed" -> allTasks.filter { it.status == "Completed" }
            else -> allTasks
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("My Tasks", fontWeight = FontWeight.Bold)
                        Text("Manage your assigned tasks", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF43A047))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFFAFAFA))
            ) {
                SearchAndFilter(
                    tasks = allTasks,
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )
                if (filteredTasks.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tasks found", color = Color.Gray)
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(filteredTasks) { task ->
                            TaskCard(task = task, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchAndFilter(tasks: List<Task>, selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("All", "Pending", "Completed")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            val count = when (filter) {
                "All" -> tasks.size
                "Pending" -> tasks.count { it.status == "Not Started" || it.status == "In Progress" }
                "Completed" -> tasks.count { it.status == "Completed" }
                else -> 0
            }
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text("$filter ($count)") },
                shape = RoundedCornerShape(50)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(task: Task, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { navController.navigate("task_details/${task.id}") }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(task.title, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                StatusChip(task.status ?: "Pending")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(task.description ?: "No description", color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    PriorityBadge(task.priority ?: "Medium")
                    InfoChip(icon = Icons.Default.CalendarToday, text = "Due: ${task.deadline ?: "N/A"}")
                }
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "View Task", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (color, icon) = when (status) {
        "In Progress" -> Color(0xFF3949AB) to null
        "Completed" -> Color(0xFF43A047) to Icons.Default.CheckCircle
        "Not Started", "Pending" -> Color.Gray to null
        else -> Color.Gray to null
    }
    val chipColors = SuggestionChipDefaults.suggestionChipColors(
        containerColor = color.copy(alpha = 0.1f),
        labelColor = color
    )

    SuggestionChip(
        onClick = {},
        label = { Text(status, fontWeight = FontWeight.Medium) },
        icon = { icon?.let { Icon(it, contentDescription = null, modifier = Modifier.size(18.dp)) } },
        colors = chipColors,
        border = null
    )
}

@Composable
fun PriorityBadge(priority: String) {
    val color = when (priority) {
        "High" -> Color(0xFFF44336)
        "Medium" -> Color(0xFFFF9800)
        else -> Color(0xFF4CAF50)
    }
    Card(
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Text(
            text = priority,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun InfoChip(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        Text(text, color = Color.Gray, fontSize = 12.sp)
    }
}