package com.example.employeeperformancetracker.ui.tasklist

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.ui.navigation.Screen
import kotlinx.coroutines.launch
import java.util.UUID

// Local data class to ensure screen is self-contained and compilable
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val assignedTo: String, // Employee ID
    val priority: String,
    val dueDate: String,
    val status: String
)

// Local repository to ensure screen is self-contained and compilable
object TaskRepository {
    fun getTasks(): List<Task> {
        return listOf(
            Task(
                title = "Q4 Marketing Report",
                description = "Compile and analyze the marketing performance data for the fourth quarter.",
                assignedTo = "EMP005",
                priority = "High",
                dueDate = "2024-12-15",
                status = "In Progress"
            ),
            Task(
                title = "Onboard New Developer",
                description = "Complete the full onboarding checklist for the new backend developer, John D.",
                assignedTo = "EMP002",
                priority = "Medium",
                dueDate = "2024-12-10",
                status = "Completed"
            ),
            Task(
                title = "Fix Production Bug #8821",
                description = "Critical bug in the payment processing module is causing checkout failures.",
                assignedTo = "EMP001",
                priority = "High",
                dueDate = "2024-12-05",
                status = "Overdue"
            ),
            Task(
                title = "Design New Dashboard Mockups",
                description = "Create high-fidelity mockups for the v2 analytics dashboard.",
                assignedTo = "EMP003",
                priority = "Medium",
                dueDate = "2024-12-20",
                status = "In Progress"
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { TopBar(navController, drawerState, scope) },
        floatingActionButton = { Fab(navController) },
        containerColor = Color(0xFFF2F4F7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TaskSummary()
            TaskList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController, drawerState: DrawerState, scope: kotlinx.coroutines.CoroutineScope) {
    TopAppBar(
        title = { Text(text = "Tasks", color = Color.White) },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2A3A8D))
    )
}

@Composable
private fun TaskSummary() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SummaryCard(modifier = Modifier.weight(1f), title = "Completed", count = "25", icon = Icons.Default.CheckCircle, iconColor = Color(0xFF4CAF50))
        SummaryCard(modifier = Modifier.weight(1f), title = "Pending", count = "12", icon = Icons.Default.Pending, iconColor = Color(0xFFF57C00))
        SummaryCard(modifier = Modifier.weight(1f), title = "Overdue", count = "3", icon = Icons.Default.Warning, iconColor = Color(0xFFD32F2F))
    }
}

@Composable
private fun SummaryCard(modifier: Modifier = Modifier, title: String, count: String, icon: ImageVector, iconColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = count, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = title, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun TaskList() {
    val tasks = TaskRepository.getTasks()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tasks) { task ->
            TaskListItem(task = task)
        }
    }
}

@Composable
private fun TaskListItem(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Due: ${task.dueDate}", color = Color.Gray, fontSize = 14.sp)
            }
            StatusBadge(status = task.status)
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val color = when (status) {
        "Completed" -> Color(0xFF4CAF50)
        "In Progress" -> Color(0xFFF57C00)
        "Overdue" -> Color(0xFFD32F2F)
        else -> Color.Gray
    }
    Text(
        text = status,
        color = color,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
private fun Fab(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate(Screen.AssignTask.route) },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
    }
}
