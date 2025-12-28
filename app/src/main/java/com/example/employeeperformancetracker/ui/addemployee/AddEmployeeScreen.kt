package com.example.employeeperformancetracker.ui.addemployee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.employeeperformancetracker.R
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme

private val PrimaryBlue = Color(0xFF3949AB)
private val BackgroundGray = Color(0xFFFAFAFA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Employee", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileUploadSection()
            Spacer(modifier = Modifier.height(24.dp))
            EmployeeForm()
            Spacer(modifier = Modifier.height(24.dp))
            SaveButton { navController.navigateUp() }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileUploadSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // Placeholder
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Handle upload */ }) {
            Text("Upload Photo")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeForm() {
    var department by remember { mutableStateOf("") }
    val departmentOptions = listOf("Engineering", "HR", "Marketing", "Sales")
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Employee ID") }, modifier = Modifier.fillMaxWidth())

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = department,
                onValueChange = {},
                readOnly = true,
                label = { Text("Department") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                departmentOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            department = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Role") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Joining Date") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
        )
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun SaveButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("Save Employee", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun AddEmployeeScreenPreview() {
    EmployeePerformanceTrackerTheme {
        AddEmployeeScreen(rememberNavController())
    }
}
