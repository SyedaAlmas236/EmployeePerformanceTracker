package com.example.employeeperformancetracker.ui.login

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.employeeperformancetracker.ui.navigation.Screen
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val accentColor = Color(0xFF3949AB)

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
                    imageVector = Icons.Default.BusinessCenter,
                    contentDescription = "Admin Login",
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(12.dp),
                    tint = accentColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Welcome Back",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Sign in to continue",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                LoginForm(navController, accentColor)
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
private fun LoginForm(navController: NavController, accentColor: Color) {
    var email by remember { mutableStateOf("admin@company.com") }
    var password by remember { mutableStateOf("password") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
            Text("Admin Login", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Enter your credentials to access the dashboard", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                 trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { 
                     if (email.isNotEmpty() && password.isNotEmpty()) {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Text("Sign In", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Divider(modifier = Modifier.weight(1f))
                Text("or", color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))
                Divider(modifier = Modifier.weight(1f))
            }
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continue as Guest")
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6_pro")
@Composable
fun LoginScreenPreview() {
    EmployeePerformanceTrackerTheme {
        LoginScreen(navController = rememberNavController())
    }
}
