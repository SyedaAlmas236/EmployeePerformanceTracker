package com.example.employeeperformancetracker.ui.tasklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.data.Task
import com.example.employeeperformancetracker.data.TaskRepository
import com.example.employeeperformancetracker.data.EmployeeRepository
import com.example.employeeperformancetracker.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, drawerState: DrawerState) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var employees by remember { mutableStateOf<List<com.example.employeeperformancetracker.data.Employee>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    var showAssignTaskSheet by remember { mutableStateOf(false) }
    val assignTaskSheetState = rememberModalBottomSheetState()

    var selectedTask by remember { mutableStateOf<Task?>(null) }
    val taskDetailSheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        isLoading = true
        tasks = TaskRepository.getTasks()
        employees = EmployeeRepository.getEmployees()
        if (employees.isEmpty()) {
            EmployeeRepository.fetchEmployees()
            employees = EmployeeRepository.getEmployees()
        }
        isLoading = false
    }

    Scaffold(
        containerColor = Color(0xFFF2F4F7),
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Header(onAssignClick = { showAssignTaskSheet = true })
                Spacer(modifier = Modifier.height(16.dp))
                TaskTabs(selectedTabIndex = selectedTabIndex, onTabSelected = { selectedTabIndex = it })
                Spacer(modifier = Modifier.height(16.dp))
                TaskList(
                    tasks = tasks,
                    employees = employees,
                    selectedTabIndex = selectedTabIndex,
                    onTaskClick = { task -> selectedTask = task })
            }
        }

        if (showAssignTaskSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAssignTaskSheet = false },
                sheetState = assignTaskSheetState
            ) {
                AssignTaskContent(
                    onCancel = { scope.launch { assignTaskSheetState.hide() }.invokeOnCompletion { if (!assignTaskSheetState.isVisible) showAssignTaskSheet = false } },
                    onAssign = { 
                        scope.launch { 
                            tasks = TaskRepository.getTasks()
                            assignTaskSheetState.hide() 
                        }.invokeOnCompletion { if (!assignTaskSheetState.isVisible) showAssignTaskSheet = false } 
                    }
                )
            }
        }

        selectedTask?.let { task ->
            ModalBottomSheet(
                onDismissRequest = { selectedTask = null },
                sheetState = taskDetailSheetState
            ) {
                TaskDetailsSheet(
                    task = task,
                    employees = employees,
                    onClose = { selectedTask = null },
                    onMarkAsCompleted = {
                        scope.launch {
                            task.id?.let { id ->
                                TaskRepository.updateTaskStatus(id, "completed")
                                tasks = TaskRepository.getTasks()
                            }
                            taskDetailSheetState.hide()
                        }.invokeOnCompletion { selectedTask = null }
                    },
                    onDelete = {
                        scope.launch {
                            task.id?.let { id ->
                                TaskRepository.deleteTask(id)
                                tasks = TaskRepository.getTasks()
                            }
                            taskDetailSheetState.hide()
                        }.invokeOnCompletion { selectedTask = null }
                    }
                )
            }
        }
    }
}

@Composable
private fun Header(onAssignClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Task Management", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text("Assign and track employee tasks", color = Color.Gray, fontSize = 14.sp)
        }
        Button(
            onClick = onAssignClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Assign Task")
            Spacer(modifier = Modifier.width(4.dp))
            Text("Assign Task")
        }
    }
}

@Composable
private fun TaskTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("All", "Pending", "In Progress", "Completed")
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
        edgePadding = 0.dp,
        indicator = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = Color.Gray
            )
        }
    }
}

@Composable
private fun TaskList(tasks: List<Task>, employees: List<com.example.employeeperformancetracker.data.Employee>, selectedTabIndex: Int, onTaskClick: (Task) -> Unit) {
    val filteredTasks = when (selectedTabIndex) {
        1 -> tasks.filter { 
            val s = it.status?.lowercase()?.replace("_", " ")
            s == "pending" || s == "not started"
        }
        2 -> tasks.filter { it.status?.lowercase()?.replace("_", " ") == "in progress" }
        3 -> tasks.filter { it.status?.lowercase()?.replace("_", " ") == "completed" }
        else -> tasks
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(filteredTasks) { task ->
            val assignedName = employees.find { it.id == task.assignedTo || it.userId == task.assignedTo }?.name ?: "Unassigned"
            TaskListItem(task = task, assignedName = assignedName, onTaskClick = onTaskClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListItem(task: Task, assignedName: String, onTaskClick: (Task) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onTaskClick(task) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                StatusBadge(status = task.status ?: "pending")
            }
            task.description?.let { Text(text = it, color = Color.Gray, fontSize = 14.sp) }
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(icon = Icons.Default.Person, text = "Assigned to: $assignedName")
            InfoRow(icon = Icons.Default.CalendarToday, text = "Deadline: ${task.deadline ?: "No deadline"}")
            Spacer(modifier = Modifier.height(16.dp))
            PriorityBadge(priority = task.priority ?: "medium")
        }
    }
}

@Composable
private fun TaskDetailsSheet(task: Task, employees: List<com.example.employeeperformancetracker.data.Employee>, onClose: () -> Unit, onMarkAsCompleted: () -> Unit, onDelete: () -> Unit) {
    val assignedName = employees.find { it.id == task.assignedTo || it.userId == task.assignedTo }?.name ?: "Unassigned"

    Column(modifier = Modifier.padding(16.dp).background(Color.White)) {
        // Header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Task Details", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Task Header Card
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    StatusBadge(status = task.status ?: "pending")
                }
                Spacer(modifier = Modifier.height(16.dp))
                PriorityBadge(priority = task.priority ?: "medium")
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.Person, text = assignedName)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.CalendarToday, text = task.deadline ?: "No deadline")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Task Description
        Text("Task Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(task.description ?: "No description provided", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onMarkAsCompleted,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Mark as Completed")
            }
            Button(
                onClick = onDelete,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.weight(1f)
            ) {
                Text("Delete Task")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssignTaskContent(onCancel: () -> Unit, onAssign: () -> Unit) {
    var employees by remember { mutableStateOf<List<com.example.employeeperformancetracker.data.Employee>>(emptyList()) }
    val priorities = listOf("low", "medium", "high")
    val statuses = listOf("pending", "completed", "overdue")

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedEmployee by remember { mutableStateOf<com.example.employeeperformancetracker.data.Employee?>(null) }
    var priority by remember { mutableStateOf(priorities[1]) }
    var deadline by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(statuses[0]) }
    
    val scope = rememberCoroutineScope()
    var isSubmitting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        employees = EmployeeRepository.getEmployees()
        if (employees.isEmpty()) {
            EmployeeRepository.fetchEmployees()
            employees = EmployeeRepository.getEmployees()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Assign New Task", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task Title *") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description *") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Spacer(modifier = Modifier.height(16.dp))
        DropdownField(
            label = "Assign To *", 
            options = employees.map { it.name }, 
            selected = selectedEmployee?.name ?: "Select", 
            onSelected = { name -> selectedEmployee = employees.find { it.name == name } }
        )
        Spacer(modifier = Modifier.height(16.dp))
        DropdownField(label = "Priority *", options = priorities, selected = priority, onSelected = { priority = it })
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = deadline,
            onValueChange = { deadline = it },
            label = { Text("Deadline *") },
            placeholder = { Text("yyyy-mm-dd") },
            trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Select Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        isSubmitting = true
                        val newTask = Task(
                            title = title,
                            description = description,
                            assignedTo = selectedEmployee?.id,
                            priority = priority,
                            status = status,
                            deadline = deadline
                        )
                        TaskRepository.addTask(newTask)
                        isSubmitting = false
                        onAssign()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp),
                enabled = !isSubmitting && title.isNotBlank()
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Assign Task")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(label: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected,
            onValueChange = { },
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
private fun StatusBadge(status: String) {
    val s = status.lowercase().replace("_", " ")
    val color = when (s) {
        "completed" -> Color(0xFF4CAF50)
        "in progress" -> Color(0xFFF57C00)
        "pending", "not started" -> Color.Gray
        else -> Color.Gray
    }
    Text(
        text = s.replaceFirstChar { it.uppercase() },
        color = Color.White,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(color, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        fontSize = 12.sp
    )
}

@Composable
private fun PriorityBadge(priority: String) {
    val color = when (priority.lowercase()) {
        "high" -> Color(0xFFD32F2F)
        "medium" -> Color(0xFFF57C00)
        "low" -> Color.Gray
        else -> Color.Gray
    }
    Text(
        text = "${priority.replaceFirstChar { it.uppercase() }} Priority",
        color = color,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        fontSize = 12.sp
    )
}
