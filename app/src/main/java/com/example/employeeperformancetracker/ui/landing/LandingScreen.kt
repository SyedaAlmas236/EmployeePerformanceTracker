package com.example.employeeperformancetracker.ui.landing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.ui.navigation.Screen

@Composable
fun LandingScreen(navController: NavController) {
    val primaryColor = Color(0xFF3949AB)

    Scaffold(
        containerColor = primaryColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Icon(
                imageVector = Icons.Default.Work,
                contentDescription = "App Logo",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Employee Performance Tracker",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Smart Workforce Management App",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))
            LoginOptionCard(
                icon = Icons.Default.AdminPanelSettings,
                title = "Admin Login",
                description = "Access administrative dashboard, manage employees, track performance, and generate reports",
                buttonText = "Continue as Admin",
                buttonColor = primaryColor,
                onClick = { navController.navigate(Screen.Login.route) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            LoginOptionCard(
                icon = Icons.Default.People,
                title = "Employee Login",
                description = "View your tasks, performance ratings, attendance records, and manage your profile",
                buttonText = "Continue as Employee",
                buttonColor = Color(0xFF43A047),
                onClick = { navController.navigate(Screen.EmployeeLogin.route) }
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "© 2025 Performance Tracker. All rights reserved.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun LoginOptionCard(
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String,
    buttonColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF3949AB),
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(buttonText, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}
