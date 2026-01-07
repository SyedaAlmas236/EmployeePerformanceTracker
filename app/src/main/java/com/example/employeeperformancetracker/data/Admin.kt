package com.example.employeeperformancetracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Admin(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("name") val name: String,
    @SerialName("company_name") val companyName: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)
