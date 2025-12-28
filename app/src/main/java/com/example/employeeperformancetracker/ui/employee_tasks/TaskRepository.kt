package com.example.employeeperformancetracker.ui.employee_tasks

import androidx.compose.runtime.mutableStateListOf
import java.util.UUID

object TaskRepository {
    private val tasks = mutableStateListOf(
        Task(UUID.randomUUID().toString(), "Complete Q4 Performance Review", "Submit self-assessment for Q4 performance review", "High", "Dec 20, 2025", "In Progress", true),
        Task(UUID.randomUUID().toString(), "Update Project Documentation", "Update technical documentation for the new feature", "Medium", "Dec 18, 2025", "In Progress", false),
        Task(UUID.randomUUID().toString(), "Team Meeting Preparation", "Prepare presentation for weekly team meeting", "Medium", "Dec 16, 2025", "Not Started", false),
        Task(UUID.randomUUID().toString(), "Bug Fix - Dashboard Loading", "Fix slow loading issue on admin dashboard", "Low", "Dec 22, 2025", "Not Started", true),
        Task(UUID.randomUUID().toString(), "Onboarding New Team Member", "Complete onboarding process for new developer", "Medium", "Dec 10, 2025", "Completed", true),
        Task(UUID.randomUUID().toString(), "Security Audit Review", "Review and implement security audit recommendations", "High", "Dec 8, 2025", "Completed", false)
    )

    fun getTasks() = tasks

    fun getTaskById(id: String?) = tasks.find { it.id == id }

    fun updateTask(updatedTask: Task) {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            tasks[index] = updatedTask
        }
    }
}