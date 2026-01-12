package com.example.employeeperformancetracker.ui.attendance

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.employeeperformancetracker.data.LeaveRepository
import com.example.employeeperformancetracker.data.SupabaseConfig
import com.example.employeeperformancetracker.data.auth.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeApplyLeaveScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    var leaveType by remember { mutableStateOf("Sick Leave") }
    var expanded by remember { mutableStateOf(false) }
    val leaveTypes = listOf("Sick Leave", "Casual Leave", "Paid Leave", "Unpaid Leave", "Maternity / Paternity Leave")

    var fromDate by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }
    var fromDateFormatted by remember { mutableStateOf("") }
    var toDateFormatted by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var attachmentName by remember { mutableStateOf("") }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        attachmentName = uri?.lastPathSegment ?: "Document Attached"
    }

    val fromDatePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fromDate = String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, month + 1, year)
            fromDateFormatted = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    val toDatePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            toDate = String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, month + 1, year)
            toDateFormatted = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Apply Leave", fontWeight = FontWeight.Bold, color = Color(0xFF43A047)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    
                    Text("Leave Type", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = leaveType,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            leaveTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        leaveType = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Text("From Date", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    OutlinedTextField(
                        value = fromDate,
                        onValueChange = {},
                        placeholder = { Text("dd-mm-yyyy") },
                        modifier = Modifier.fillMaxWidth().clickable { fromDatePicker.show() },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray,
                            disabledTrailingIconColor = Color.Gray
                        ),
                        trailingIcon = { Icon(Icons.Default.CalendarToday, null) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text("To Date", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    OutlinedTextField(
                        value = toDate,
                        onValueChange = {},
                        placeholder = { Text("dd-mm-yyyy") },
                        modifier = Modifier.fillMaxWidth().clickable { toDatePicker.show() },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray,
                            disabledTrailingIconColor = Color.Gray
                        ),
                        trailingIcon = { Icon(Icons.Default.CalendarToday, null) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text("Reason", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        placeholder = { Text("Enter reason for leave...") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text("Attachment (Optional)", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                            .clickable { filePickerLauncher.launch("*/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.UploadFile, null, tint = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(attachmentName.ifEmpty { "Upload Document" }, color = Color.Gray)
                        }
                    }
                    Text("Attach medical certificate or supporting documents", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Cancel") }

                Button(
                    onClick = {
                        if (reason.isBlank() || fromDate.isEmpty() || toDate.isEmpty()) {
                            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        } else {
                            val start = sdf.parse(fromDate)
                            val end = sdf.parse(toDate)
                            if (start != null && end != null && end.before(start)) {
                                Toast.makeText(context, "To Date cannot be before From Date", Toast.LENGTH_SHORT).show()
                            } else {
                                val currentUser = authViewModel.getCurrentUser()
                                val userId = currentUser?.id ?: return@Button
                                
                                scope.launch {
                                    try {
                                        LeaveRepository.applyLeave(
                                            supabase = SupabaseConfig.client,
                                            userId = userId,
                                            leaveType = leaveType,
                                            fromDateFormatted = fromDateFormatted,
                                            toDateFormatted = toDateFormatted,
                                            reason = reason
                                        )
                                        Toast.makeText(context, "Leave applied successfully", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                ) { Text("Apply Leave") }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Text(
                    "Note: Your leave request will be sent to the admin for approval. You will receive a notification once it's processed.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 13.sp,
                    color = Color(0xFF1565C0)
                )
            }
        }
    }
}
