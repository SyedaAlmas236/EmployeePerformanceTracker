package com.example.employeeperformancetracker.data

object TaskRepository {
    fun getTasks(): List<Task> {
        return listOf(
            Task(
                title = "Q4 Marketing Report",
                description = "Compile and analyze the marketing performance data for the fourth quarter.",
                assignedTo = "EMP005",
                priority = "High",
                dueDate = "2024-12-15",
                status = "In Progress"
            ),
            Task(
                title = "Onboard New Developer",
                description = "Complete the full onboarding checklist for the new backend developer, John D.",
                assignedTo = "EMP002",
                priority = "Medium",
                dueDate = "2024-12-10",
                status = "Completed"
            ),
            Task(
                title = "Fix Production Bug #8821",
                description = "Critical bug in the payment processing module is causing checkout failures.",
                assignedTo = "EMP001",
                priority = "High",
                dueDate = "2024-12-05",
                status = "Overdue"
            ),
            Task(
                title = "Design New Dashboard Mockups",
                description = "Create high-fidelity mockups for the v2 analytics dashboard.",
                assignedTo = "EMP003",
                priority = "Medium",
                dueDate = "2024-12-20",
                status = "In Progress"
            )
        )
    }
}
