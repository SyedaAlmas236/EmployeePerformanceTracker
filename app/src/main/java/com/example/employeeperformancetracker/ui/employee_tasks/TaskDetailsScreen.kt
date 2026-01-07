package com.example.employeeperformancetracker.ui.employee_tasks

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.data.Task
import com.example.employeeperformancetracker.data.TaskRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(navController: NavController, taskId: String?) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var task by remember { mutableStateOf<Task?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var updatedStatus by remember { mutableStateOf("") }

    LaunchedEffect(taskId) {
        isLoading = true
        task = TaskRepository.getTaskById(taskId)
        updatedStatus = task?.status?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "Pending"
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Task Details", fontWeight = FontWeight.Bold)
                        Text("Update your task progress", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3949AB))
            }
        } else if (task != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFFFAFAFA))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TaskInformationCard(task!!)
                UpdateTaskStatusCard(updatedStatus) { updatedStatus = it }
                WorkUpdateRemarksCard()
                AttachmentsCard()
                UpdateAndCompleteButtons(
                    onUpdateClick = {
                        scope.launch {
                            val result = TaskRepository.updateTaskStatus(task!!.id!!, updatedStatus)
                            if (result.isSuccess) {
                                Toast.makeText(context, "Task updated!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                val errorMsg = result.exceptionOrNull()?.message ?: "Unknown Error"
                                println("UI Error Log: Task Update Failed: $errorMsg")
                                Toast.makeText(context, "Update Failed: $errorMsg. Check RLS Policies.", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    onCompleteClick = {
                        scope.launch {
                            val result = TaskRepository.updateTaskStatus(task!!.id!!, "completed")
                            if (result.isSuccess) {
                                Toast.makeText(context, "Task completed!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                val errorMsg = result.exceptionOrNull()?.message ?: "Unknown Error"
                                println("UI Error Log: Task Completion Failed: $errorMsg")
                                Toast.makeText(context, "Completion Failed: $errorMsg", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Task not found.")
            }
        }
    }
}

@Composable
fun TaskInformationCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Task Information", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(label = "Task Title", value = task.title)
            InfoRow(label = "Deadline", value = task.deadline ?: "No deadline set")
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                     InfoRow(label = "Priority", value = "", content = { PriorityBadge(task.priority ?: "Medium") })
                }
                Column(modifier = Modifier.weight(1f)) {
                     InfoRow(label = "Current Status", value = "", content = { StatusChip(task.status ?: "Pending") })
                }
            }
             InfoRow(label = "Description", value = task.description ?: "No description provided")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTaskStatusCard(currentStatus: String, onStatusSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("Pending", "In Progress", "Completed")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Update Task Status", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = currentStatus,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                     shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    statuses.forEach { status ->
                        DropdownMenuItem(text = { Text(status) }, onClick = {
                            onStatusSelected(status)
                            expanded = false
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun WorkUpdateRemarksCard() {
    var remarks by remember { mutableStateOf("") }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Work Update / Remarks", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                placeholder = { Text("Describe your work progress or issues...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Provide detailed updates about your progress, challenges, or any blockers", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

        }
    }
}

@Composable
fun AttachmentsCard() {
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            selectedFileUri = uri
        }
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Attachments", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { 
                    launcher.launch(arrayOf(
                        "application/pdf",
                        "image/*",
                        "application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    ))
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Default.Attachment, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload Attachment")
            }
            Spacer(modifier = Modifier.height(4.dp))
             Text("Supported: Images, PDFs, Documents", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            selectedFileUri?.let { uri ->
                val fileName = try {
                    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (nameIndex != -1) {
                            cursor.moveToFirst()
                            cursor.getString(nameIndex)
                        } else null
                    }
                } catch (e: Exception) { null }
                
                if (fileName != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Attachment, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = fileName, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateAndCompleteButtons(onUpdateClick: () -> Unit, onCompleteClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = onUpdateClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3949AB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Update Task", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        OutlinedButton(
            onClick = onCompleteClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            border = BorderStroke(1.dp, Color(0xFF43A047)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF43A047))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Mark as Completed", color = Color(0xFF43A047), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, content: @Composable (() -> Unit)? = null) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        if (content != null) {
            content()
        } else {
            Text(value, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}