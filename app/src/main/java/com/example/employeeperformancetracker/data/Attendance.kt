package com.example.employeeperformancetracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attendance(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("date") val date: String, // yyyy-MM-dd
    @SerialName("status") val status: String, // Present, Absent, etc.
    @SerialName("marked_at") val markedAt: String? = null, // HH:mm:ss
    @SerialName("created_at") val createdAt: String? = null
)
