package com.example.employeeperformancetracker.ui.employee_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeLoginScreen(navController: NavController) {
    val accentColor = Color(0xFF43A047)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = accentColor,
        topBar = {
            TopAppBar(
                title = { Text("Back to Landing", color = Color.White, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Landing.route) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Employee Portal",
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(12.dp),
                    tint = accentColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Employee Portal",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Sign in to access your account",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                LoginFormCard(navController, accentColor, email, password, passwordVisible, onEmailChange = {email = it}, onPasswordChange = {password = it}, onPasswordVisibilityChange = {passwordVisible = it})
            }
            Text(
                text = "© 2025 Performance Tracker. All rights reserved.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun LoginFormCard(
    navController: NavController,
    accentColor: Color,
    email: String,
    pass: String,
    passVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text("Employee Login", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Enter your credentials to continue", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Employee ID / Email") },
                placeholder = {Text("EMP001 or employee@company.com")},
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                 trailingIcon = {
                    IconButton(onClick = { onPasswordVisibilityChange(!passVisible) }) {
                        Icon(
                            if (passVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                 TextButton(onClick = { /* TODO */ }) {
                    Text("Forgot Password?", color = accentColor)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Screen.EmployeeDashboard.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Text("Sign In", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}