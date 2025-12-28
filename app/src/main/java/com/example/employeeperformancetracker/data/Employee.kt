package com.example.employeeperformancetracker.data

import androidx.annotation.DrawableRes

data class Employee(
    val id: String,
    val name: String,
    val role: String,
    val department: String,
    val rating: Float,
    @DrawableRes val imageRes: Int
)