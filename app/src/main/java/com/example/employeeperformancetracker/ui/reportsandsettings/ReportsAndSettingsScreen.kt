package com.example.employeeperformancetracker.ui.reportsandsettings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

// --- Colors ---
private val PrimaryBlue = Color(0xFF3949AB)
private val BackgroundGray = Color(0xFFFAFAFA)
private val GreenColor = Color(0xFF4CAF50)
private val RedColor = Color(0xFFD32F2F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsAndSettingsScreen(navController: NavController, reportsViewModel: ReportsViewModel = viewModel()) {
    val department by reportsViewModel.department.collectAsState()
    val dateRange by reportsViewModel.dateRange.collectAsState()
    val ratingRange by reportsViewModel.ratingRange.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        Header(navController)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExportReportsCard(
                department = department,
                onDepartmentChange = { reportsViewModel.setDepartment(it) },
                dateRange = dateRange,
                onDateRangeChange = { reportsViewModel.setDateRange(it) },
                ratingRange = ratingRange,
                onRatingRangeChange = { reportsViewModel.setRatingRange(it) },
                onExportClick = { reportsViewModel.exportToCSV(context) }
            )
            SettingsCard(navController)
            SystemInfoCard()
            LogoutButton(navController)
        }
    }
}

@Composable
fun Header(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = PrimaryBlue,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(top = 16.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Reports & Settings", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Export data & configure app", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun ExportReportsCard(
    department: String,
    onDepartmentChange: (String) -> Unit,
    dateRange: String,
    onDateRangeChange: (String) -> Unit,
    ratingRange: String,
    onRatingRangeChange: (String) -> Unit,
    onExportClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = "Export Reports", tint = PrimaryBlue)
                Spacer(Modifier.width(12.dp))
                Text("Export Reports", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))

            FilterDropdown(
                label = "Department Filter",
                selectedValue = department,
                options = listOf("All Departments", "Engineering", "Design", "Management", "Marketing", "Analysis"),
                onValueChange = onDepartmentChange
            )
            Spacer(Modifier.height(8.dp))
            FilterDropdown(
                label = "Date Range",
                selectedValue = dateRange,
                options = listOf("Last Week", "Last Month", "Last Quarter", "Last Year", "All Time"),
                onValueChange = onDateRangeChange
            )
            Spacer(Modifier.height(8.dp))
            FilterDropdown(
                label = "Rating Range",
                selectedValue = ratingRange,
                options = listOf("All Ratings", "5 Stars", "4–5 Stars", "3–4 Stars", "Below 3 Stars"),
                onValueChange = onRatingRangeChange
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onExportClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenColor)
            ) {
                Icon(Icons.Default.CloudDownload, contentDescription = "Export")
                Spacer(Modifier.width(8.dp))
                Text("Export to CSV", fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onValueChange(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsCard(navController: NavController) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var autoBackupEnabled by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = PrimaryBlue)
                Spacer(Modifier.width(12.dp))
                Text("Settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            SettingItem(title = "Notifications", subtitle = "Receive push notifications", icon = Icons.Default.Notifications, trailingContent = {
                Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
            })
            Divider(Modifier.padding(vertical = 8.dp))
            SettingItem(title = "Auto Backup", subtitle = "Automatic data backup", icon = Icons.Default.Shield, trailingContent = {
                Switch(checked = autoBackupEnabled, onCheckedChange = { autoBackupEnabled = it })
            })
            Divider(Modifier.padding(vertical = 8.dp))
            SettingItem(title = "Account Settings", subtitle = "Manage your account", icon = Icons.Default.Person, modifier = Modifier.clickable { /* navController.navigate("account_settings") */ }) // TODO: Add navigation
        }
    }
}

@Composable
fun SettingItem(title: String, subtitle: String, icon: ImageVector, modifier: Modifier = Modifier, trailingContent: @Composable (() -> Unit)? = null) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = Color.Gray)
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        if (trailingContent != null) {
            trailingContent()
        }
    }
}


@Composable
fun SystemInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("System Information", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            SystemInfoItem(label = "Total Employees", value = "124")
            Divider(Modifier.padding(vertical = 8.dp))
            SystemInfoItem(label = "Total Tasks", value = "342")
            Divider(Modifier.padding(vertical = 8.dp))
            SystemInfoItem(label = "Evaluations", value = "89")
            Divider(Modifier.padding(vertical = 8.dp))
            SystemInfoItem(label = "App Version", value = "1.0.0")
        }
    }
}

@Composable
fun SystemInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.Gray)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun LogoutButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("login") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = RedColor)
    ) {
        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
        Spacer(Modifier.width(8.dp))
        Text("Logout", fontSize = 16.sp)
    }
}
