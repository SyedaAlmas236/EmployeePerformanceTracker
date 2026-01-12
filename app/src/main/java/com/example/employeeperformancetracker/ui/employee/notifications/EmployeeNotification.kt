package com.example.employeeperformancetracker.ui.employee.notifications

data class EmployeeNotification(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val type: NotificationType,
    val status: NotificationStatus? = null,
    val meetingLink: String? = null
)

enum class NotificationType {
    LEAVE, MEETING, TASK, INFO
}

enum class NotificationStatus {
    PENDING, APPROVED, REJECTED
}
