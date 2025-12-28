
package com.example.employeeperformancetracker.ui.tasklist

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

data class Task(
    val id: UUID = UUID.randomUUID(),
    var taskName: String,
    var assignedTo: String,
    var deadline: String,
    var priority: Priority,
    var status: Status
)

enum class Priority { High, Medium, Low }
enum class Status { Pending, InProgress, Completed }

fun getPriorityColor(priority: Priority): Color {
    return when (priority) {
        Priority.High -> Color(0xFFF44336)
        Priority.Medium -> Color(0xFFFFA726)
        Priority.Low -> Color(0xFF66BB6A)
    }
}

fun getStatusColor(status: Status): Color {
    return when (status) {
        Status.Pending -> Color(0xFFF44336)
        Status.InProgress -> Color(0xFFFFA726)
        Status.Completed -> Color(0xFF66BB6A)
    }
}

fun getStatusIcon(status: Status): androidx.compose.ui.graphics.vector.ImageVector {
    return when (status) {
        Status.Pending -> Icons.Default.Pending
        Status.InProgress -> Icons.Default.Timelapse
        Status.Completed -> Icons.Default.CheckCircle
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, drawerState: DrawerState) {

    val sampleTasks = remember {
        mutableStateListOf(
            Task(taskName = "Implement authentication module", assignedTo = "Sarah Mitchell", deadline = "Nov 18, 2025", priority = Priority.High, status = Status.InProgress),
            Task(taskName = "Project timeline review", assignedTo = "John Anderson", deadline = "Nov 19, 2025", priority = Priority.Medium, status = Status.Pending),
            Task(taskName = "Design system update", assignedTo = "Emily Chen", deadline = "Nov 20, 2025", priority = Priority.Low, status = Status.Completed),
            Task(taskName = "API optimization", assignedTo = "Michael Roberts", deadline = "Nov 21, 2025", priority = Priority.High, status = Status.Pending)
        )
    }

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Tasks", fontWeight = FontWeight.Bold)
                        Text("${sampleTasks.size} total tasks", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3949AB),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Assign Task")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Assign Task", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            sampleTasks.forEach { task ->
                TaskCard(task = task)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        if (showDialog) {
            AssignTaskDialog(
                onDismiss = { showDialog = false },
                onTaskAssign = { newTask ->
                    sampleTasks.add(newTask)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(task.taskName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Chip(label = task.priority.name, color = getPriorityColor(task.priority))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Assigned to: ${task.assignedTo}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Deadline", modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(task.deadline, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
                StatusChip(status = task.status)
            }
        }
    }
}

@Composable
fun Chip(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatusChip(status: Status) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(getStatusColor(status).copy(alpha = 0.1f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = getStatusIcon(status),
            contentDescription = status.name,
            tint = getStatusColor(status),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(status.name, color = getStatusColor(status), fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignTaskDialog(
    onDismiss: () -> Unit,
    onTaskAssign: (Task) -> Unit
) {
    val context = LocalContext.current
    val employees = listOf("Sarah Mitchell", "John Anderson", "Emily Chen", "Michael Roberts")
    val priorities = Priority.values().map { it.name }
    val statuses = Status.values().map { it.name }

    var selectedEmployee by remember { mutableStateOf(employees[0]) }
    var taskDescription by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(priorities[1]) }
    var selectedStatus by remember { mutableStateOf(statuses[0]) }

    var isEmployeeExpanded by remember { mutableStateOf(false) }
    var isPriorityExpanded by remember { mutableStateOf(false) }
    var isStatusExpanded by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            calendar.set(year, month, dayOfMonth)
            deadline = sdf.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Assign New Task", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("Create and assign a task to an employee", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Assign To
                Text("Assign To", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                ExposedDropdownMenuBox(expanded = isEmployeeExpanded, onExpandedChange = { isEmployeeExpanded = it }) {
                    OutlinedTextField(
                        value = selectedEmployee,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isEmployeeExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = isEmployeeExpanded, onDismissRequest = { isEmployeeExpanded = false }) {
                        employees.forEach { employee ->
                            DropdownMenuItem(
                                text = { Text(employee) },
                                onClick = {
                                    selectedEmployee = employee
                                    isEmployeeExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Task Description
                Text("Task Description", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    placeholder = { Text("Describe the task...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Deadline
                Text("Deadline", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = deadline,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("dd-mm-yyyy") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Select Date",
                            modifier = Modifier.clickable { datePickerDialog.show() }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Priority
                Text("Priority", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                ExposedDropdownMenuBox(expanded = isPriorityExpanded, onExpandedChange = { isPriorityExpanded = it }) {
                    OutlinedTextField(
                        value = selectedPriority,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPriorityExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = isPriorityExpanded, onDismissRequest = { isPriorityExpanded = false }) {
                        priorities.forEach { priority ->
                            DropdownMenuItem(
                                text = { Text(priority) },
                                onClick = {
                                    selectedPriority = priority
                                    isPriorityExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Status
                Text("Status", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                ExposedDropdownMenuBox(expanded = isStatusExpanded, onExpandedChange = { isStatusExpanded = it }) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStatusExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = isStatusExpanded, onDismissRequest = { isStatusExpanded = false }) {
                        statuses.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    selectedStatus = status
                                    isStatusExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val newTask = Task(
                            taskName = taskDescription,
                            assignedTo = selectedEmployee,
                            deadline = deadline,
                            priority = Priority.valueOf(selectedPriority),
                            status = Status.valueOf(selectedStatus)
                        )
                        onTaskAssign(newTask)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3949AB))
                ) {
                    Text("Assign Task", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
