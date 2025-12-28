// FINAL, DEFINITIVE FIX: Adding drawerState to all placeholder screens and correcting all signatures.
package com.example.employeeperformancetracker.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Attendance Screen")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollScreen(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payroll") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Payroll Screen")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReimbursementsScreen(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reimbursements") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Reimbursements Screen")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Settings Screen")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign Up") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Create Account", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                        if (password == confirmPassword) {
                            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forgot Password") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Forgot Password", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        Toast.makeText(context, "Reset link sent", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send Reset Link")
            }
        }
    }
}
