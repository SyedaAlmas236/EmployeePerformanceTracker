package com.example.employeeperformancetracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("id") val id: String? = null,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("priority") val priority: String? = "medium", // 'high', 'medium', 'low'
    @SerialName("status") val status: String? = "pending", // 'pending', 'in_progress', 'completed', 'overdue'
    @SerialName("deadline") val deadline: String? = null,
    @SerialName("assigned_to") val assignedTo: String? = null, // UUID from employees
    @SerialName("created_by") val createdBy: String? = null, // UUID from profiles
    @SerialName("created_at") val createdAt: String? = null
)
