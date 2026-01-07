package com.example.employeeperformancetracker.ui.addemployee

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.R
import com.example.employeeperformancetracker.data.Employee
import com.example.employeeperformancetracker.data.EmployeeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Add Employee", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White.copy(alpha = 0.8f))
                        Text("Create new employee profile", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3949AB))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA)) // Light gray background
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            EmployeeForm(navController)
        }
    }
}

@Composable
fun EmployeeForm(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("EMP007") }
    var department by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var joiningDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image Section
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)) {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)){
                 Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8EAF6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("U", fontSize = 40.sp, color = Color(0xFF3949AB))
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                                .clickable { /* TODO: Handle image picker */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Upload Image", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Upload profile image", color = Color.Gray)
                 }
            }
        }

        // Form Fields
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name *") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = employeeId, onValueChange = { employeeId = it }, label = { Text("Employee ID *") }, modifier = Modifier.fillMaxWidth(), readOnly = true)
                DepartmentDropdown(selected = department, onSelected = { department = it })
                OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Role/Position *") }, placeholder = { Text("e.g., Senior Developer") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = joiningDate,
                    onValueChange = { joiningDate = it },
                    label = { Text("Joining Date *") },
                    placeholder = { Text("dd-mm-yyyy") },
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address *") }, placeholder = { Text("email@company.com") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number *") }, placeholder = { Text("+1 234 567 8900") }, modifier = Modifier.fillMaxWidth())
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Button(
            onClick = {
                val newEmployee = Employee(employeeId, fullName, role, department, 0f, R.drawable.ic_launcher_background)
                EmployeeRepository.addEmployee(newEmployee)
                navController.navigateUp()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Employee", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DepartmentDropdown(selected: String, onSelected: (String) -> Unit) {
    val options = listOf("Engineering", "HR", "Marketing", "Sales")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selected) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = if(selectedText.isEmpty()) "" else selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Department *") },
            placeholder = { Text("Select department") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onSelected(option)
                    selectedText = option
                    expanded = false
                })
            }
        }
    }
}
