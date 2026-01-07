package com.example.employeeperformancetracker.ui.assigntask

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.data.Employee
import com.example.employeeperformancetracker.data.EmployeeRepository
import com.example.employeeperformancetracker.data.Task
import com.example.employeeperformancetracker.data.TaskRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignTaskScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var employees by remember { mutableStateOf<List<Employee>>(emptyList()) }
    var selectedEmployee by remember { mutableStateOf<Employee?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }
    var deadline by remember { mutableStateOf("") }
    
    val priorities = listOf("High", "Medium", "Low")
    var selectedPriority by remember { mutableStateOf(priorities[1]) }
    
    val scope = rememberCoroutineScope()
    var isSubmitting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        employees = EmployeeRepository.getEmployees()
        if (employees.isEmpty()) {
            EmployeeRepository.fetchEmployees()
            employees = EmployeeRepository.getEmployees()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assign New Task", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedEmployee?.name ?: "Select Employee",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Assign To") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    employees.forEach { employee ->
                        DropdownMenuItem(
                            text = { Text(employee.name) },
                            onClick = {
                                selectedEmployee = employee
                                expanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = { priorityExpanded = !priorityExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPriority,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Priority") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    priorities.forEach { priority ->
                        DropdownMenuItem(
                            text = { Text(priority) },
                            onClick = {
                                selectedPriority = priority
                                priorityExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Deadline (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        isSubmitting = true
                        val task = Task(
                            title = title,
                            description = description,
                            assignedTo = selectedEmployee?.id,
                            priority = selectedPriority.lowercase(),
                            deadline = deadline,
                            status = "pending" // Correct default status
                        )
                        val result = TaskRepository.addTask(task)
                        if (result.isSuccess) {
                            navController.popBackStack()
                        }
                        isSubmitting = false
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank() && selectedEmployee != null && !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Assign Task", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
