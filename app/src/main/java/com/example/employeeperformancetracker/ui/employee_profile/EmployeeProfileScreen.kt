package com.example.employeeperformancetracker.ui.employee_profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.employeeperformancetracker.R
import com.example.employeeperformancetracker.data.Employee
import com.example.employeeperformancetracker.data.EmployeeRepository
import com.example.employeeperformancetracker.data.Task
import com.example.employeeperformancetracker.data.TaskRepository
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

private val PrimaryBlue = Color(0xFF3949AB)
private val BackgroundGray = Color(0xFFF8F9FA)
private val GoldColor = Color(0xFFFFC107)
private val GreenColor = Color(0xFF4CAF50)
private val OrangeColor = Color(0xFFF57C00)
private val RedColor = Color(0xFFD32F2F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeProfileScreen(navController: NavController, employeeName: String?) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var employee by remember { mutableStateOf(EmployeeRepository.getEmployeeByName(employeeName)) }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showEditSheet by remember { mutableStateOf(false) }

    LaunchedEffect(employeeName) {
        isLoading = true
        // Ensure employees are fetched
        if (EmployeeRepository.getEmployees().isEmpty()) {
            EmployeeRepository.fetchEmployees()
        }
        employee = EmployeeRepository.getEmployeeByName(employeeName)
        
        // Fetch tasks
        tasks = TaskRepository.getTasks().filter { it.assignedTo == employee?.id || it.assignedTo == employee?.userId }
        isLoading = false
    }

    Scaffold(
        topBar = { TopAppBar(navController, onEditClick = { showEditSheet = true }) },
        containerColor = BackgroundGray
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (employee != null) {
            val currentEmployee = employee!!
            var employeeImageUri by remember { mutableStateOf<Uri?>(null) }

            if (showEditSheet) {
                EditProfileSheet(
                    employee = currentEmployee,
                    imageUri = employeeImageUri,
                    onDismiss = { showEditSheet = false },
                    onUpdate = { updatedEmployee, newImageUri ->
                        scope.launch {
                            val result = EmployeeRepository.updateEmployee(updatedEmployee)
                            if (result.isSuccess) {
                                employee = updatedEmployee
                                employeeImageUri = newImageUri
                                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                            }
                            showEditSheet = false
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileHeader(currentEmployee, employeeImageUri)
                ProfileTabs(currentEmployee, tasks)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Employee not found.")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(navController: NavController, onEditClick: () -> Unit) {
    TopAppBar(
        title = { Text("Employee Profile", color = Color.White) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileSheet(
    employee: Employee,
    imageUri: Uri?,
    onDismiss: () -> Unit,
    onUpdate: (Employee, Uri?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var currentImageUri by remember { mutableStateOf(imageUri) }
    var fullName by rememberSaveable { mutableStateOf(employee.name) }
    var email by rememberSaveable { mutableStateOf(employee.email) }
    var phone by rememberSaveable { mutableStateOf(employee.phoneNumber ?: "") }
    var department by rememberSaveable { mutableStateOf(employee.department ?: "") }
    var position by rememberSaveable { mutableStateOf(employee.position ?: "") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> currentImageUri = uri }
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BackgroundGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Employee", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    val painter = if (currentImageUri != null) {
                        rememberAsyncImagePainter(currentImageUri)
                    } else {
                        rememberAsyncImagePainter(employee.profileImageUrl ?: "https://api.dicebear.com/7.x/avataaars/svg?seed=${employee.name}")
                    }
                    Image(
                        painter = painter,
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Upload profile image", fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name *") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = employee.employeeId ?: "",
                    onValueChange = {},
                    label = { Text("Employee ID *") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Gray, unfocusedTextColor = Color.Gray)
                )

                var departmentExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = departmentExpanded, onExpandedChange = { departmentExpanded = !departmentExpanded }) {
                    OutlinedTextField(
                        value = department,
                        onValueChange = {},
                        label = { Text("Department *") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = departmentExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    )
                    ExposedDropdownMenu(expanded = departmentExpanded, onDismissRequest = { departmentExpanded = false }) {
                        listOf("Engineering", "Management", "Design", "Marketing", "Analytics").forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = {
                                department = it
                                departmentExpanded = false
                            })
                        }
                    }
                }
                
                var positionExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = positionExpanded, onExpandedChange = { positionExpanded = !positionExpanded }) {
                    OutlinedTextField(
                        value = position,
                        onValueChange = {},
                        label = { Text("Role/Position *") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = positionExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    )
                    ExposedDropdownMenu(expanded = positionExpanded, onDismissRequest = { positionExpanded = false }) {
                         listOf("Senior Developer", "Project Manager", "UX Designer", "Backend Developer", "Marketing Manager", "Data Analyst").forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = {
                                position = it
                                positionExpanded = false
                            })
                        }
                    }
                }

                OutlinedTextField(
                    value = employee.joiningDate ?: "",
                    onValueChange = {}, 
                    label = { Text("Joining Date *") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true, 
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Gray, unfocusedTextColor = Color.Gray)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address *") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val updatedEmployee = employee.copy(name = fullName, position = position, department = department, email = email, phoneNumber = phone)
                        onUpdate(updatedEmployee, currentImageUri)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenColor)
                ) {
                    Text("Update Employee")
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(employee: Employee, imageUri: Uri?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBlue, shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter = if (imageUri != null) {
            rememberAsyncImagePainter(imageUri)
        } else {
            rememberAsyncImagePainter(employee.profileImageUrl ?: "https://api.dicebear.com/7.x/avataaars/svg?seed=${employee.name}")
        }
        Image(
            painter = painter,
            contentDescription = employee.name,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(employee.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(employee.position ?: "No Position", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = GoldColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${employee.rating ?: 0f} Rating", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProfileTabs(employee: Employee, tasks: List<Task>) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Profile", "Tasks", "Performance")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = PrimaryBlue,
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 3.dp,
                        color = PrimaryBlue
                    )
                }
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = FontWeight.Bold) }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        when (selectedTabIndex) {
            0 -> ProfileInfoSection(employee)
            1 -> TasksSection(tasks)
            2 -> PerformanceSection()
        }
    }
}

@Composable
private fun ProfileInfoSection(employee: Employee) {
    var email by rememberSaveable(employee.name) { mutableStateOf("${employee.name.replace(" ", ".").lowercase()}@company.com") }
    var phone by rememberSaveable { mutableStateOf("+1 234 567 8900") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            InfoRow(icon = Icons.Default.Badge, label = "Employee ID", value = employee.employeeId ?: "")
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            InfoRow(icon = Icons.Default.Email, label = "Email", value = employee.email)
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            InfoRow(icon = Icons.Default.Phone, label = "Phone", value = employee.phoneNumber ?: "")
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            InfoRow(icon = Icons.Default.DateRange, label = "Joining Date", value = employee.joiningDate ?: "")
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            DepartmentInfoRow(department = employee.department ?: "")
        }
    }
}

@Composable
private fun TasksSection(tasks: List<Task>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (tasks.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("No tasks assigned.", color = Color.Gray)
            }
        } else {
            tasks.forEach { task ->
                TaskCard(
                    title = task.title,
                    date = task.deadline ?: "No deadline",
                    priority = task.priority ?: "Low",
                    status = task.status ?: "Pending"
                )
            }
        }
    }
}

@Composable
private fun PerformanceSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                PerformanceMetric(label = "Quality", rating = 5)
                PerformanceMetric(label = "Timeliness", rating = 5)
                PerformanceMetric(label = "Attendance", rating = 4)
                PerformanceMetric(label = "Communication", rating = 4)
                PerformanceMetric(label = "Innovation", rating = 5)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Icon(Icons.Default.WorkspacePremium, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Evaluate Performance", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = label, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(value, fontWeight = FontWeight.Normal, fontSize = 16.sp)
        }
    }
}

@Composable
private fun DepartmentInfoRow(department: String) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Business, contentDescription = "Department", tint = PrimaryBlue, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("Department", color = Color.Gray, fontSize = 12.sp)
            SuggestionChip(onClick = {}, label = { Text(department) })
        }
    }
}

@Composable
private fun TaskCard(title: String, date: String, priority: String, status: String) {
    val statusColor = when(status) {
        "In Progress" -> OrangeColor
        "Completed" -> GreenColor
        "Pending" -> RedColor
        else -> Color.Gray
    }
     val priorityColor = when(priority) {
        "High" -> RedColor
        "Medium" -> OrangeColor
        else -> Color.Gray
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                StatusChip(text = priority, color = priorityColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                 Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Date", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(date, color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                StatusChip(text = status, color = statusColor)
            }
        }
    }
}

@Composable
fun StatusChip(text: String, color: Color) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun PerformanceMetric(label: String, rating: Int) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { rating / 5f },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = GreenColor,
            trackColor = GreenColor.copy(alpha = 0.2f)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(5) { index ->
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < rating) GoldColor else Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
