package com.example.employeeperformancetracker.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.employeeperformancetracker.ui.navigation.Screen
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1200)
    )
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000L)
        navController.navigate(Screen.Landing.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3949AB), Color(0xFF5C6BC0))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale).alpha(alpha)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "App Logo",
                    tint = Color(0xFF3949AB),
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Employee Performance Tracker",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Smart Workforce Management App",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    EmployeePerformanceTrackerTheme {
        SplashScreen(navController = rememberNavController())
    }
}
