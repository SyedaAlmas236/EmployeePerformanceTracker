package com.example.employeeperformancetracker.ui.employee_tasks

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val priority: String,
    val dueDate: String,
    val status: String,
    val hasFiles: Boolean
)