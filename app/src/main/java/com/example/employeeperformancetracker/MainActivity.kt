package com.example.employeeperformancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CoPresent
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.employeeperformancetracker.ui.navigation.NavigationGraph
import com.example.employeeperformancetracker.ui.navigation.Screen
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmployeePerformanceTrackerTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Smart Workforce Management", fontSize = 14.sp, color = Color.Gray)
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = currentRoute == Screen.Dashboard.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Dashboard.route)
                    },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") }
                )
                 NavigationDrawerItem(
                    label = { Text("Employees") },
                    selected = currentRoute == Screen.EmployeeList.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.EmployeeList.route)
                    },
                    icon = { Icon(Icons.Default.Groups, contentDescription = "Employees") }
                )
                 NavigationDrawerItem(
                    label = { Text("Tasks") },
                    selected = currentRoute == Screen.TaskList.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.TaskList.route)
                     },
                    icon = { Icon(Icons.Default.Task, contentDescription = "Tasks") }
                )
                 NavigationDrawerItem(
                    label = { Text("Analytics") },
                    selected = currentRoute == Screen.Analytics.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Analytics.route)
                     },
                    icon = { Icon(Icons.Default.Analytics, contentDescription = "Analytics") }
                )
                NavigationDrawerItem(
                    label = { Text("Attendance") },
                    selected = currentRoute == Screen.Attendance.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Attendance.route)
                     },
                    icon = { Icon(Icons.Default.CoPresent, contentDescription = "Attendance") }
                )
                NavigationDrawerItem(
                    label = { Text("Payroll") },
                    selected = currentRoute == Screen.Payroll.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Payroll.route)
                     },
                    icon = { Icon(Icons.Default.Paid, contentDescription = "Payroll") }
                )
                NavigationDrawerItem(
                    label = { Text("Reimbursements") },
                    selected = currentRoute == Screen.Reimbursements.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Reimbursements.route)
                     },
                    icon = { Icon(Icons.Default.Paid, contentDescription = "Reimbursements") }
                )
                NavigationDrawerItem(
                    label = { Text("Meetings") },
                    selected = currentRoute == Screen.MeetingBooking.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MeetingBooking.route)
                     },
                    icon = { Icon(Icons.Default.People, contentDescription = "Meetings") }
                )
                NavigationDrawerItem(
                    label = { Text("Quick Links") },
                    selected = currentRoute == Screen.QuickLinks.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.QuickLinks.route)
                     },
                    icon = { Icon(Icons.Default.Task, contentDescription = "Quick Links") }
                )
                 NavigationDrawerItem(
                    label = { Text("Recruitment") },
                    selected = currentRoute == "",
                    onClick = { 
                        scope.launch { drawerState.close() }
                        // navController.navigate(Screen.Recruitment.route)
                     },
                    icon = { Icon(Icons.Default.Work, contentDescription = "Recruitment") }
                )
                 NavigationDrawerItem(
                    label = { Text("Self Services") },
                    selected = currentRoute == "",
                    onClick = { 
                        scope.launch { drawerState.close() }
                        // navController.navigate(Screen.SelfServices.route)
                     },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Self Services") }
                )
                NavigationDrawerItem(
                    label = { Text("Corporate Services") },
                    selected = currentRoute == "",
                    onClick = { 
                        scope.launch { drawerState.close() }
                        // navController.navigate(Screen.CorporateServices.route)
                     },
                    icon = { Icon(Icons.Default.Apartment, contentDescription = "Corporate Services") }
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                 NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = currentRoute == Screen.Settings.route,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Settings.route)
                     },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") }
                )
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Landing.route) { popUpTo(0) }
                     },
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = Color.Red) }
                )
            }
        }
    ) {
        NavigationGraph(navController = navController, drawerState = drawerState)
    }
}
