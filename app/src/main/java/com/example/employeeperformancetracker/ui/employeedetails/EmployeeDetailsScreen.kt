package com.example.employeeperformancetracker.ui.employeedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.employeeperformancetracker.data.Employee
import com.example.employeeperformancetracker.data.EmployeeRepository
import com.example.employeeperformancetracker.data.Task
import com.example.employeeperformancetracker.data.TaskRepository
import com.example.employeeperformancetracker.ui.common.InfoRow
import com.example.employeeperformancetracker.ui.navigation.Screen
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

private val PrimaryBlue = Color(0xFF3949AB)
private val Green = Color(0xFF43A047)
private val BackgroundGray = Color(0xFFFAFAFA)

data class EmployeeDetails(
    val name: String,
    val position: String,
    val department: String,
    val joiningDate: String,
    val email: String,
    val phone: String,
    val rating: Double,
    val imageId: Int
)

val employeeDetails = EmployeeDetails(
    name = "Sarah Mitchell",
    position = "Senior Developer",
    department = "Engineering",
    joiningDate = "Jan 15, 2022",
    email = "sarah.mitchell@example.com",
    phone = "+1 (123) 456-7890",
    rating = 4.9,
    imageId = R.drawable.ic_launcher_background // Placeholder
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailsScreen(navController: NavController, employeeName: String?) {
    var employee by remember { mutableStateOf<Employee?>(null) }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(employeeName) {
        isLoading = true
        if (EmployeeRepository.getEmployees().isEmpty()) {
            EmployeeRepository.fetchEmployees()
        }
        employee = EmployeeRepository.getEmployeeByName(employeeName)
        tasks = TaskRepository.getTasks().filter { it.assignedTo == employee?.id || it.assignedTo == employee?.userId }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Employee Details") },
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (employee != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileHeaderCard(employee = employee!!)
                DetailsTabs(navController, employee!!, tasks)
            }
        } else {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Employee not found.")
            }
        }
    }
}

@Composable
fun ProfileHeaderCard(employee: Employee) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = employee.profileImageUrl ?: "https://api.dicebear.com/7.x/avataaars/svg?seed=${employee.name}",
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Text(employee.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(employee.position ?: "No Position", fontSize = 16.sp, color = Color.Gray)
            Text(employee.department ?: "No Department", fontSize = 14.sp, color = Color.Gray)
            Text("Joined: ${employee.joiningDate ?: "N/A"}", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            RatingBadge(rating = (employee.rating ?: 0f).toDouble())
        }
    }
}

@Composable
fun RatingBadge(rating: Double) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Green)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = rating.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DetailsTabs(navController: NavController, employee: Employee, tasks: List<Task>) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Profile", "Tasks", "Performance")

    Column {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = PrimaryBlue,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = PrimaryBlue,
                        height = 3.dp
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = FontWeight.Bold) },
                    selectedContentColor = PrimaryBlue,
                    unselectedContentColor = Color.Gray
                )
            }
        }

        when (selectedTabIndex) {
            0 -> ProfileTabContent(employee = employee)
            1 -> TasksTabContent(tasks)
            2 -> PerformanceTabContent(navController)
        }
    }
}

@Composable
fun ProfileTabContent(employee: Employee) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow(icon = Icons.Default.Business, label = "Department", value = employee.department ?: "N/A")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow(icon = Icons.Default.Email, label = "Email", value = employee.email)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow(icon = Icons.Default.Phone, label = "Phone", value = employee.phoneNumber ?: "N/A")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow(icon = Icons.Default.DateRange, label = "Joining Date", value = employee.joiningDate ?: "N/A")
        }
    }
}


@Composable
fun TasksTabContent(tasks: List<Task>) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (tasks.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("No tasks assigned.", color = Color.Gray)
            }
        } else {
            tasks.forEach { task ->
                TaskCard(task)
            }
        }
    }
}

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            task.description?.let { Text(it, fontSize = 14.sp, color = Color.Gray) }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Status: ${task.status}", fontSize = 12.sp, color = PrimaryBlue)
                Text("Priority: ${task.priority}", fontSize = 12.sp, color = Color.Red)
            }
        }
    }
}

@Composable
fun PerformanceTabContent(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { navController.navigate(Screen.PerformanceEvaluation.route) }) {
            Text("Submit New Evaluation")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun EmployeeDetailsScreenPreview() {
    EmployeePerformanceTrackerTheme {
        EmployeeDetailsScreen(rememberNavController(), employeeName = "Sarah Mitchell")
    }
}
