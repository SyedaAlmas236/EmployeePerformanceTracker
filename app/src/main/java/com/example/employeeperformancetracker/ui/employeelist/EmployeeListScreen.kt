package com.example.employeeperformancetracker.ui.employeelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.data.Employee
import com.example.employeeperformancetracker.data.EmployeeRepository
import com.example.employeeperformancetracker.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(navController: NavController, drawerState: DrawerState) {
    val employees by EmployeeRepository.employees.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedDepartment by remember { mutableStateOf("All Departments") }

    val filteredEmployees = employees.filter {
        it.name.contains(searchQuery, ignoreCase = true) &&
                (selectedDepartment == "All Departments" || it.department == selectedDepartment)
    }

    Scaffold(
        topBar = { TopBar(navController) },
        floatingActionButton = { Fab(navController) },
        containerColor = Color(0xFFF2F4F7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(searchQuery) { searchQuery = it }
            FilterBar(filteredEmployees.size, selectedDepartment) { selectedDepartment = it }
            EmployeeList(navController = navController, employees = filteredEmployees)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text(text = "Employees", color = Color.White) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2A3A8D))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search employees...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun FilterBar(employeeCount: Int, selectedDepartment: String, onDepartmentSelected: (String) -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    val departments = listOf("All Departments", "Engineering", "Design", "Management", "Marketing", "Sales", "Analytics")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$employeeCount total", color = Color.Gray, fontSize = 14.sp)
        Box {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { showMenu = true }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = selectedDepartment, fontWeight = FontWeight.SemiBold)
            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                departments.forEach { department ->
                    DropdownMenuItem(text = { Text(department) }, onClick = { onDepartmentSelected(department); showMenu = false })
                }
            }
        }
    }
}

@Composable
private fun EmployeeList(navController: NavController, employees: List<Employee>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(employees) { employee ->
            EmployeeCard(employee = employee, onClick = { navController.navigate("employee_profile/${employee.name}") })
        }
    }
}

@Composable
private fun EmployeeCard(employee: Employee, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = employee.imageRes),
                contentDescription = employee.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = employee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = employee.role, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                DepartmentChip(department = employee.department)
            }
            RatingBadge(rating = employee.rating)
        }
    }
}

@Composable
private fun DepartmentChip(department: String) {
    val color = when (department) {
        "Engineering" -> Color(0xFFE8EAF6)
        "Management" -> Color(0xFFE0F2F1)
        "Design" -> Color(0xFFFFF3E0)
        else -> Color.LightGray
    }
    val textColor = when (department) {
        "Engineering" -> Color(0xFF3949AB)
        "Management" -> Color(0xFF00897B)
        "Design" -> Color(0xFFFB8C00)
        else -> Color.DarkGray
    }

    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = department, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RatingBadge(rating: Float) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = rating.toString(), color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun Fab(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate(Screen.AddEmployee.route) },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Employee", tint = Color.White)
    }
}
