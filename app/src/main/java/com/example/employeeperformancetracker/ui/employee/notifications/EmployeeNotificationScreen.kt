package com.example.employeeperformancetracker.ui.employee.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.employeeperformancetracker.data.LeaveRequest
import com.example.employeeperformancetracker.data.SupabaseConfig
import com.example.employeeperformancetracker.data.auth.AuthViewModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeNotificationScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    var notifications by remember { mutableStateOf<List<EmployeeNotification>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    fun fetchNotifications() {
        scope.launch {
            isLoading = true
            try {
                val userId = authViewModel.getCurrentUser()?.id
                if (userId != null) {
                    val leaveRequests = SupabaseConfig.client.from("leave_requests")
                        .select {
                            filter {
                                eq("user_id", userId)
                            }
                            order("created_at", order = Order.DESCENDING)
                        }
                        .decodeList<LeaveRequest>()

                    notifications = leaveRequests.map { leave ->
                        val (message, status) = when (leave.status.lowercase()) {
                            "approved" -> "Your leave request has been approved" to NotificationStatus.APPROVED
                            "rejected" -> "Your leave request has been rejected" to NotificationStatus.REJECTED
                            else -> "Leave request sent, awaiting approval" to NotificationStatus.PENDING
                        }

                        EmployeeNotification(
                            id = leave.id ?: "",
                            title = "Leave Request: ${leave.leave_type}",
                            description = message,
                            timestamp = leave.created_at?.split("T")?.get(0) ?: "Recently",
                            type = NotificationType.LEAVE,
                            status = status
                        )
                    }
                }
            } catch (e: Exception) {
                // Silent error handle as per minimal rules
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchNotifications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Notifications",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF43A047),
                            modifier = Modifier.padding(end = 48.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Gray)
                    }
                },
                actions = {
                    TextButton(onClick = { fetchNotifications() }) {
                        Text("Refresh", color = Color(0xFF3F51B5), fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF43A047))
            }
        } else if (notifications.isEmpty()) {
            EmptyNotifications()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(notifications) { notification ->
                    NotificationItem(notification = notification)
                }
            }
        }
    }
}

@Composable
fun EmptyNotifications() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("No notifications yet", color = Color.Gray, fontSize = 16.sp)
        }
    }
}
