package com.example.employeeperformancetracker.ui.performanceevaluation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.employeeperformancetracker.data.Employee
import com.example.employeeperformancetracker.data.EmployeeRepository
import kotlinx.coroutines.launch

private val PrimaryBlue = Color(0xFF3949AB)
private val BackgroundGray = Color(0xFFF8F9FA)
private val StarYellow = Color(0xFFFFC107)
private val GreenColor = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceEvaluationScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val employeeName = navController.previousBackStackEntry?.savedStateHandle?.get<String>("employeeName")
    val employees by EmployeeRepository.employees.collectAsState()
    val employee = remember(employees, employeeName) { employees.find { it.name == employeeName } }

    var qualityRating by remember { mutableIntStateOf(0) }
    var timelinessRating by remember { mutableIntStateOf(0) }
    var attendanceRating by remember { mutableIntStateOf(0) }
    var communicationRating by remember { mutableIntStateOf(0) }
    var innovationRating by remember { mutableIntStateOf(0) }
    var comments by remember { mutableStateOf("") }

    val ratings = listOf(qualityRating, timelinessRating, attendanceRating, communicationRating, innovationRating)
    val averageRating = if (ratings.any { it > 0 }) {
        ratings.filter { it > 0 }.average().toFloat()
    } else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Performance Evaluation", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Rate employee performance", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        if (employee == null) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EmployeeInfoCard(employee)

                OverallRatingCard(averageRating)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Rating Categories", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))

                        RatingCategoryItem(
                            icon = Icons.Default.Diamond,
                            label = "Quality of Work",
                            rating = qualityRating,
                            onRatingChange = { qualityRating = it }
                        )
                        Divider(Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        RatingCategoryItem(
                            icon = Icons.Default.Timer,
                            label = "Timeliness",
                            rating = timelinessRating,
                            onRatingChange = { timelinessRating = it }
                        )
                        Divider(Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        RatingCategoryItem(
                            icon = Icons.Default.CalendarMonth,
                            label = "Attendance",
                            rating = attendanceRating,
                            onRatingChange = { attendanceRating = it }
                        )
                        Divider(Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        RatingCategoryItem(
                            icon = Icons.Default.ChatBubble,
                            label = "Communication",
                            rating = communicationRating,
                            onRatingChange = { communicationRating = it }
                        )
                        Divider(Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        RatingCategoryItem(
                            icon = Icons.Default.Lightbulb,
                            label = "Innovation",
                            rating = innovationRating,
                            onRatingChange = { innovationRating = it }
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Additional Comments", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = comments,
                            onValueChange = { comments = it },
                            placeholder = { Text("Provide detailed feedback...", color = Color.Gray, fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF2F4F7),
                                unfocusedContainerColor = Color(0xFFF2F4F7),
                                focusedBorderColor = PrimaryBlue.copy(alpha = 0.5f),
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent
                            )
                        )
                    }
                }

                Button(
                    onClick = {
                        scope.launch {
                            val updatedEmployee = employee.copy(rating = averageRating)
                            EmployeeRepository.updateEmployee(updatedEmployee)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenColor),
                    enabled = ratings.any { it > 0 }
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Submit Evaluation", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun EmployeeInfoCard(employee: Employee) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(employee.profileImageUrl ?: "https://api.dicebear.com/7.x/avataaars/svg?seed=${employee.name}"),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(employee.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Text(employee.position ?: "No Position", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun OverallRatingCard(rating: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Overall Rating", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(String.format("%.1f", rating), color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
            }
            Text("out of 5.0", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
        }
    }
}

@Composable
private fun RatingCategoryItem(
    icon: ImageVector,
    label: String,
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, fontWeight = FontWeight.Medium, fontSize = 15.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..5).forEach { index ->
                val isSelected = index <= rating
                val scale by animateFloatAsState(targetValue = if (isSelected) 1.1f else 1f, label = "scale")
                val tint by animateColorAsState(targetValue = if (isSelected) StarYellow else Color.LightGray.copy(alpha = 0.5f), label = "color")

                Icon(
                    imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .size(40.dp)
                        .scale(scale)
                        .clickable { onRatingChange(index) }
                )
            }
        }
        if (rating > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { rating / 5f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = GreenColor,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
        }
    }
}
