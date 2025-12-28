package com.example.employeeperformancetracker.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.employeeperformancetracker.ui.ForgotPasswordScreen
import com.example.employeeperformancetracker.ui.SignupScreen
import com.example.employeeperformancetracker.ui.addemployee.AddEmployeeScreen
import com.example.employeeperformancetracker.ui.analytics.AnalyticsScreen
import com.example.employeeperformancetracker.ui.assigntask.AssignTaskScreen
import com.example.employeeperformancetracker.ui.attendance.AttendanceScreen
import com.example.employeeperformancetracker.ui.dashboard.DashboardScreen
import com.example.employeeperformancetracker.ui.employee_dashboard.EmployeeDashboardScreen
import com.example.employeeperformancetracker.ui.employee_login.EmployeeLoginScreen
import com.example.employeeperformancetracker.ui.employee_performance.EmployeePerformanceScreen
import com.example.employeeperformancetracker.ui.employee_profile.EmployeeProfileScreen
import com.example.employeeperformancetracker.ui.employee_self_profile.EmployeeSelfProfileScreen
import com.example.employeeperformancetracker.ui.employee_tasks.MyTasksScreen
import com.example.employeeperformancetracker.ui.employee_tasks.TaskDetailsScreen
import com.example.employeeperformancetracker.ui.employeelist.EmployeeListScreen
import com.example.employeeperformancetracker.ui.landing.LandingScreen
import com.example.employeeperformancetracker.ui.login.LoginScreen
import com.example.employeeperformancetracker.ui.meetingbooking.MeetingBookingScreen
import com.example.employeeperformancetracker.ui.payroll.PayrollScreen
import com.example.employeeperformancetracker.ui.performanceevaluation.PerformanceEvaluationScreen
import com.example.employeeperformancetracker.ui.quicklinks.QuickLinksScreen
import com.example.employeeperformancetracker.ui.reimbursements.ReimbursementsScreen
import com.example.employeeperformancetracker.ui.reportsandsettings.ReportsAndSettingsScreen
import com.example.employeeperformancetracker.ui.splash.SplashScreen
import com.example.employeeperformancetracker.ui.tasklist.TaskListScreen

@Composable
fun NavigationGraph(navController: NavHostController, drawerState: DrawerState) {
    NavHost(navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Landing.route) {
            LandingScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController, drawerState = drawerState)
        }
        composable(Screen.EmployeeList.route) {
            EmployeeListScreen(navController = navController, drawerState = drawerState)
        }
        composable(
            route = "employee_profile/{employeeName}",
            arguments = listOf(navArgument("employeeName") { type = NavType.StringType })
        ) { backStackEntry ->
            val employeeName = backStackEntry.arguments?.getString("employeeName")
            EmployeeProfileScreen(navController = navController, employeeName = employeeName)
        }
        composable(Screen.AddEmployee.route) {
            AddEmployeeScreen(navController = navController)
        }
        composable(Screen.TaskList.route) {
            TaskListScreen(navController = navController, drawerState = drawerState)
        }
        composable(Screen.AssignTask.route) {
            AssignTaskScreen(navController = navController)
        }
        composable(Screen.PerformanceEvaluation.route) {
            PerformanceEvaluationScreen(navController = navController)
        }
        composable(Screen.Analytics.route) {
            AnalyticsScreen(navController = navController, drawerState = drawerState)
        }
        composable(Screen.MeetingBooking.route) {
            MeetingBookingScreen(navController = navController, drawerState = drawerState)
        }
        composable(Screen.QuickLinks.route) {
            QuickLinksScreen(navController = navController)
        }
        composable(Screen.Attendance.route) {
            AttendanceScreen(navController = navController)
        }
        composable(Screen.Payroll.route) {
            PayrollScreen(navController = navController)
        }
        composable(Screen.Reimbursements.route) {
            ReimbursementsScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            ReportsAndSettingsScreen(navController = navController)
        }
        composable(Screen.Signup.route) {
            SignupScreen(navController = navController)
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(Screen.EmployeeLogin.route) {
            EmployeeLoginScreen(navController = navController)
        }
        composable(Screen.EmployeeDashboard.route) {
            EmployeeDashboardScreen(navController = navController)
        }
        composable(Screen.EmployeeTasks.route) {
            MyTasksScreen(navController = navController)
        }
        composable(
            route = "task_details/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            TaskDetailsScreen(navController = navController, taskId = taskId)
        }
        composable(Screen.EmployeePerformance.route) {
            EmployeePerformanceScreen(navController = navController)
        }
        composable(Screen.EmployeeSelfProfile.route) {
            EmployeeSelfProfileScreen(navController = navController)
        }
        composable(Screen.EmployeeSettings.route) {
            // Placeholder for Employee Settings
        }
    }
}
