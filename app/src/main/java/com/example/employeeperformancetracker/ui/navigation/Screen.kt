package com.example.employeeperformancetracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String = "", val icon: ImageVector? = null) {
    object Splash : Screen("splash")
    object Login : Screen("login", "Login")
    object Signup : Screen("signup", "Sign Up")
    object ForgotPassword : Screen("forgot_password", "Forgot Password")
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object EmployeeList : Screen("employee_list", "Employees", Icons.Default.Groups)
    object AddEmployee : Screen("add_employee", "Add Employee")
    object RegisterEmployeeCredentials : Screen("register_employee_credentials", "Register Employee Credentials")
    object EmployeeDetails : Screen("employee_details/{employeeId}", "Employee Details") {
        fun createRoute(employeeId: Int) = "employee_details/$employeeId"
    }
    object TaskList : Screen("task_list", "Tasks", Icons.Default.Checklist)
    object AssignTask : Screen("assign_task", "Assign Task")
    object PerformanceEvaluation : Screen("performance_evaluation", "Performance Evaluation")
    object Analytics : Screen("analytics", "Analytics", Icons.Default.BarChart)
    object MeetingBooking : Screen("meeting_booking", "Meetings", Icons.Default.MeetingRoom)
    object QuickLinks : Screen("quick_links", "Quick Links", Icons.Default.Link)

    object AdminProfile : Screen("adminProfile", "Admin Profile")
    // Added missing screens for the drawer
    object Attendance : Screen("attendance", "Attendance", Icons.Default.CoPresent)
    object Payroll : Screen("payroll", "Payroll", Icons.Default.Payment)
    object Reimbursements : Screen("reimbursements", "Reimbursements", Icons.Default.ReceiptLong)

    object ReportsAndSettings : Screen("reports_and_settings", "Reports & Settings", Icons.Default.Settings)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    // Employee-facing screens
    object Landing : Screen("landing", "Landing")
    object EmployeeLogin : Screen("employee_login", "Employee Login")
    object EmployeeDashboard : Screen("employee_dashboard", "Dashboard", Icons.Default.Home)
    object EmployeeTasks : Screen("employee_tasks", "My Tasks", Icons.Default.Checklist)
    object TaskDetail : Screen("task_details/{taskId}", "Task Detail") {
        fun createRoute(taskId: String) = "task_details/$taskId"
    }
    object EmployeePerformance : Screen("employee_performance", "Performance", Icons.Default.BarChart)
    object EmployeeSelfProfile : Screen("employee_self_profile", "Profile", Icons.Default.Person)
    object EmployeeSettings : Screen("employee_settings", "Settings")
}