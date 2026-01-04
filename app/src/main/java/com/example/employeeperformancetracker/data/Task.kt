package com.example.employeeperformancetracker.data

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val assignedTo: String, // Employee ID
    val priority: String,
    val dueDate: String,
    val status: String
)
