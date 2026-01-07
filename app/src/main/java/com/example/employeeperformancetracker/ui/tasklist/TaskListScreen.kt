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
import com.example.employeeperformancetracker.ui.navigation.Screen
import kotlinx.coroutines.launch
import java.util.*

// Local data class to ensure screen is self-contained and compilable
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val assignedTo: String,
    val createdBy: String,
    val deadline: String,
    val priority: String,
    val status: String
)

// Local repository to ensure screen is self-contained and compilable
object TaskRepository {
    private var tasks = listOf(
        Task(
            title = "Implement user authentication",
            description = "Add JWT-based authentication for all API endpoints to secure the application.",
            assignedTo = "Sarah Johnson",
            createdBy = "Admin",
            deadline = "2024-12-20",
            priority = "High",
            status = "In Progress"
        ),
        Task(
            title = "Fix responsive layout issues",
            description = "The main dashboard is not rendering correctly on smaller mobile devices.",
            assignedTo = "Emily Davis",
            createdBy = "Admin",
            deadline = "2024-12-18",
            priority = "Medium",
            status = "Completed"
        ),
        Task(
            title = "Write API documentation",
            description = "Create comprehensive documentation for all public REST API endpoints.",
            assignedTo = "Michael Chen",
            createdBy = "Admin",
            deadline = "2024-12-25",
            priority = "Low",
            status = "Pending"
        ),
        Task(
            title = "Database optimization",
            description = "Review and optimize slow-performing database queries.",
            assignedTo = "James Wilson",
            createdBy = "Admin",
            deadline = "2024-12-22",
            priority = "High",
            status = "In Progress"
        )
    )

    fun getTasks(): List<Task> = tasks

    fun deleteTask(taskId: String) {
        tasks = tasks.filterNot { it.id == taskId }
    }

    fun completeTask(taskId: String) {
        tasks = tasks.map {
            if (it.id == taskId) it.copy(status = "Completed") else it
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, drawerState: DrawerState) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var tasks by remember { mutableStateOf(TaskRepository.getTasks()) }
    val scope = rememberCoroutineScope()

    var showAssignTaskSheet by remember { mutableStateOf(false) }
    val assignTaskSheetState = rememberModalBottomSheetState()

    var selectedTask by remember { mutableStateOf<Task?>(null) }
    val taskDetailSheetState = rememberModalBottomSheetState()

    Scaffold(
        containerColor = Color(0xFFF2F4F7),
    ) { paddingValues ->
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
                selectedTabIndex = selectedTabIndex,
                onTaskClick = { task -> selectedTask = task })
        }

        if (showAssignTaskSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAssignTaskSheet = false },
                sheetState = assignTaskSheetState
            ) {
                AssignTaskContent(
                    onCancel = { scope.launch { assignTaskSheetState.hide() }.invokeOnCompletion { if (!assignTaskSheetState.isVisible) showAssignTaskSheet = false } },
                    onAssign = { scope.launch { assignTaskSheetState.hide() }.invokeOnCompletion { if (!assignTaskSheetState.isVisible) showAssignTaskSheet = false } }
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
                    onClose = { selectedTask = null },
                    onMarkAsCompleted = {
                        TaskRepository.completeTask(task.id)
                        tasks = TaskRepository.getTasks()
                        scope.launch { taskDetailSheetState.hide() }.invokeOnCompletion { selectedTask = null }
                    },
                    onDelete = {
                        TaskRepository.deleteTask(task.id)
                        tasks = TaskRepository.getTasks()
                        scope.launch { taskDetailSheetState.hide() }.invokeOnCompletion { selectedTask = null }
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
private fun TaskList(tasks: List<Task>, selectedTabIndex: Int, onTaskClick: (Task) -> Unit) {
    val filteredTasks = when (selectedTabIndex) {
        1 -> tasks.filter { it.status == "Pending" }
        2 -> tasks.filter { it.status == "In Progress" }
        3 -> tasks.filter { it.status == "Completed" }
        else -> tasks
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(filteredTasks) { task ->
            TaskListItem(task = task, onTaskClick = onTaskClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListItem(task: Task, onTaskClick: (Task) -> Unit) {
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
                StatusBadge(status = task.status)
            }
            Text(text = task.description, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(icon = Icons.Default.Person, text = "Assigned to: ${task.assignedTo}")
            InfoRow(icon = Icons.Default.CalendarToday, text = "Deadline: ${task.deadline}")
            Spacer(modifier = Modifier.height(16.dp))
            PriorityBadge(priority = task.priority)
        }
    }
}

@Composable
private fun TaskDetailsSheet(task: Task, onClose: () -> Unit, onMarkAsCompleted: () -> Unit, onDelete: () -> Unit) {
    var workUpdate by remember { mutableStateOf("") }

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
                    StatusBadge(status = task.status)
                }
                Spacer(modifier = Modifier.height(16.dp))
                PriorityBadge(priority = task.priority)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.Person, text = task.assignedTo)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.CalendarToday, text = task.deadline)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Task Description
        Text("Task Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(task.description, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        // Attachments
        Text("Attachments", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO: Open file picker */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray), shape = RoundedCornerShape(8.dp)) {
            Icon(Icons.Default.Upload, contentDescription = "Upload Attachment", tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text("No attachments uploaded", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Work Update
        Text("Work Update", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = workUpdate,
            onValueChange = { workUpdate = it },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            placeholder = { Text("Add a note...") }
        )

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


@Composable
private fun DetailItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = label, tint = Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, color = Color.Gray, fontSize = 12.sp)
                Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssignTaskContent(onCancel: () -> Unit, onAssign: () -> Unit) {
    val employees = listOf("Sarah Johnson", "Emily Davis", "Michael Chen", "James Wilson")
    val priorities = listOf("Low", "Medium", "High")
    val statuses = listOf("Pending", "In Progress", "Completed")

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var assignedTo by remember { mutableStateOf(employees[0]) }
    var priority by remember { mutableStateOf(priorities[1]) }
    var deadline by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(statuses[0]) }

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
        DropdownField(label = "Assign To *", options = employees, selected = assignedTo, onSelected = { assignedTo = it })
        Spacer(modifier = Modifier.height(16.dp))
        DropdownField(label = "Priority *", options = priorities, selected = priority, onSelected = { priority = it })
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = deadline,
            onValueChange = { deadline = it },
            label = { Text("Deadline *") },
            placeholder = { Text("dd-mm-yyyy") },
            trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Select Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        DropdownField(label = "Status *", options = statuses, selected = status, onSelected = { status = it })
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
                onClick = onAssign,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Assign Task")
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
            modifier = Modifier.fillMaxWidth().menuAnchor()
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
    val color = when (status) {
        "Completed" -> Color(0xFF4CAF50)
        "In Progress" -> Color(0xFFF57C00)
        "Pending" -> Color.Gray
        else -> Color.Gray
    }
    Text(
        text = status,
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
    val priorityText = when (priority) {
        "High" -> "High Priority"
        "Medium" -> "Medium Priority"
        "Low" -> "Low Priority"
        else -> priority
    }
    val color = when (priority) {
        "High" -> Color(0xFFD32F2F)
        "Medium" -> Color(0xFFF57C00)
        "Low" -> Color.Gray
        else -> Color.Gray
    }
    Text(
        text = priorityText,
        color = color,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        fontSize = 12.sp
    )
}
