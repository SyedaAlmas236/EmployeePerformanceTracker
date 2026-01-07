package com.example.employeeperformancetracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("employee_id") val employeeId: String? = null,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("department") val department: String? = null,
    @SerialName("position") val position: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    @SerialName("joining_date") val joiningDate: String? = null,
    @SerialName("rating") val rating: Float? = 0f,
    @SerialName("profile_image_url") val profileImageUrl: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)