package com.example.employeeperformancetracker.ui.performanceevaluation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme

private val PrimaryBlue = Color(0xFF3949AB)
private val BackgroundGray = Color(0xFFFAFAFA)
private val StarYellow = Color(0xFFFFC107)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceEvaluationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance Evaluation", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val categories = listOf("Quality", "Timeliness", "Attendance", "Communication", "Innovation")
            categories.forEach { category ->
                RatingCategoryCard(category = category)
            }
            CommentBox()
            SubmitEvaluationButton { navController.navigateUp() }
        }
    }
}

@Composable
fun RatingCategoryCard(category: String) {
    var rating by remember { mutableStateOf(0) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(category, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            StarRatingSelector(rating = rating, onRatingChange = { rating = it })
        }
    }
}

@Composable
fun StarRatingSelector(rating: Int, onRatingChange: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.Center) {
        (1..5).forEach { index ->
            IconButton(onClick = { onRatingChange(index) }) {
                Icon(
                    imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Star $index",
                    tint = if (index <= rating) StarYellow else Color.Gray,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun CommentBox() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text("Add comments...") },
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun SubmitEvaluationButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("Submit Evaluation", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun PerformanceEvaluationScreenPreview() {
    EmployeePerformanceTrackerTheme {
        PerformanceEvaluationScreen(rememberNavController())
    }
}
