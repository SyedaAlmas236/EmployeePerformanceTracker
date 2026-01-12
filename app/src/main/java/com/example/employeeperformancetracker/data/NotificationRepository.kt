package com.example.employeeperformancetracker.data

import androidx.compose.runtime.mutableStateListOf
import com.example.employeeperformancetracker.ui.employee.notifications.NotificationStatus
import com.example.employeeperformancetracker.ui.employee.notifications.NotificationType
import java.util.*

object NotificationRepository {
    data class NotificationData(
        val id: String,
        val title: String,
        val description: String,
        val timestamp: String,
        val type: NotificationType,
        val status: NotificationStatus? = null,
        val meetingLink: String? = null,
        val targetUserId: String, // "admin" or employee userId
        val leaveRequestId: String? = null
    )

    private val _notifications = mutableStateListOf<NotificationData>()
    val notifications: List<NotificationData> get() = _notifications

    fun sendNotification(notification: NotificationData) {
        _notifications.add(0, notification) // Add to top
    }

    fun updateNotificationStatusByLeaveId(leaveRequestId: String, status: NotificationStatus) {
        val index = _notifications.indexOfFirst { it.leaveRequestId == leaveRequestId }
        if (index != -1) {
            _notifications[index] = _notifications[index].copy(status = status)
        }
    }

    fun getNotificationsForUser(userId: String): List<NotificationData> {
        return _notifications.filter { it.targetUserId == userId }
    }

    fun clearAll(userId: String) {
        _notifications.removeAll { it.targetUserId == userId }
    }
}
