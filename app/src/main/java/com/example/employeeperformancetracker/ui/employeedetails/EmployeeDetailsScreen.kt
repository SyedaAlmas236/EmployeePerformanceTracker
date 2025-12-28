package com.example.employeeperformancetracker.ui.employeedetails

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
import com.example.employeeperformancetracker.ui.common.InfoRow
import com.example.employeeperformancetracker.ui.navigation.Screen
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme

private val PrimaryBlue = Color(0xFF3949AB)
private val Green = Color(0xFF43A047)
private val BackgroundGray = Color(0xFFFAFAFA)

data class EmployeeDetails(
    val name: String,
    val role: String,
    val department: String,
    val joiningDate: String,
    val email: String,
    val phone: String,
    val rating: Double,
    val imageId: Int
)

val employeeDetails = EmployeeDetails(
    name = "Sarah Mitchell",
    role = "Senior Developer",
    department = "Engineering",
    joiningDate = "Jan 15, 2022",
    email = "sarah.mitchell@example.com",
    phone = "+1 (123) 456-7890",
    rating = 4.9,
    imageId = R.drawable.ic_launcher_background // Placeholder
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailsScreen(navController: NavController, employeeId: Int?) {
    // In a real app, you would use employeeId to fetch details
    val employee = employeeDetails

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Employee Details") },
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
        ) {
            ProfileHeaderCard(employee = employee)
            DetailsTabs(navController, employee)
        }
    }
}

@Composable
fun ProfileHeaderCard(employee: EmployeeDetails) {
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
            Image(
                painter = painterResource(id = employee.imageId),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Text(employee.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(employee.role, fontSize = 16.sp, color = Color.Gray)
            Text(employee.department, fontSize = 14.sp, color = Color.Gray)
            Text("Joined: ${employee.joiningDate}", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            RatingBadge(rating = employee.rating)
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
fun DetailsTabs(navController: NavController, employee: EmployeeDetails) {
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
                    TabRowDefaults.Indicator(
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
            1 -> TasksTabContent() // Placeholder
            2 -> PerformanceTabContent(navController)
        }
    }
}

@Composable
fun ProfileTabContent(employee: EmployeeDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow(icon = Icons.Default.Business, label = "Department", value = employee.department)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow(icon = Icons.Default.Email, label = "Email", value = employee.email)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow(icon = Icons.Default.Phone, label = "Phone", value = employee.phone)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow(icon = Icons.Default.DateRange, label = "Joining Date", value = employee.joiningDate)
        }
    }
}


@Composable
fun TasksTabContent() {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Text("Tasks content goes here")
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
        EmployeeDetailsScreen(rememberNavController(), employeeId = 1)
    }
}
