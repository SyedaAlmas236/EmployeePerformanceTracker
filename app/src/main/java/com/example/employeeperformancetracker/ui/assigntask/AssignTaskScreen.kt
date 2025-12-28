package com.example.employeeperformancetracker.ui.assigntask

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme

private val PrimaryBlue = Color(0xFF3949AB)
private val BackgroundGray = Color(0xFFFAFAFA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignTaskScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assign Task", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TaskForm()
            Spacer(modifier = Modifier.weight(1f))
            SaveButton { navController.navigateUp() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskForm() {
    var priority by remember { mutableStateOf("") }
    val priorityOptions = listOf("High", "Medium", "Low")
    var priorityExpanded by remember { mutableStateOf(false) }

    var status by remember { mutableStateOf("") }
    val statusOptions = listOf("Pending", "Completed", "Overdue")
    var statusExpanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Task Description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Deadline") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
        )

        // Priority Dropdown
        ExposedDropdownMenuBox(expanded = priorityExpanded, onExpandedChange = { priorityExpanded = !priorityExpanded }) {
            OutlinedTextField(
                value = priority,
                onValueChange = {},
                readOnly = true,
                label = { Text("Priority") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = priorityExpanded, onDismissRequest = { priorityExpanded = false }) {
                priorityOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            priority = selectionOption
                            priorityExpanded = false
                        }
                    )
                }
            }
        }

        // Status Dropdown
        ExposedDropdownMenuBox(expanded = statusExpanded, onExpandedChange = { statusExpanded = !statusExpanded }) {
            OutlinedTextField(
                value = status,
                onValueChange = {},
                readOnly = true,
                label = { Text("Status") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                statusOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            status = selectionOption
                            statusExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SaveButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("Save Task", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun AssignTaskScreenPreview() {
    EmployeePerformanceTrackerTheme {
        AssignTaskScreen(rememberNavController())
    }
}
