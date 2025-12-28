package com.example.employeeperformancetracker.ui.employee_performance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.employeeperformancetracker.ui.employee_dashboard.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeePerformanceScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("My Performance", fontWeight = FontWeight.Bold)
                        Text("View your performance ratings", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFFAFAFA))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OverallPerformanceRatingCard()
            PerformanceCategories()
            MonthlyPerformanceTrend()
            NotesCard()
        }
    }
}

@Composable
fun OverallPerformanceRatingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800).copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Overall Performance Rating", color = Color.White, fontWeight = FontWeight.Bold)
            Text("4.5", color = Color.White, fontSize = 60.sp, fontWeight = FontWeight.Bold)
            Row {
                repeat(5) { index ->
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < 4) Color.White else Color.White.copy(alpha = 0.5f)
                    )
                }
            }
            Text("Out of 5.0", color = Color.White.copy(alpha = 0.8f))
        }
    }
}

@Composable
fun PerformanceCategories() {
    Column {
        Text("Performance Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        PerformanceCategoryRow("Quality", 4.8f, Color(0xFF3949AB))
        PerformanceCategoryRow("Timeliness", 4.5f, Color(0xFF43A047))
        PerformanceCategoryRow("Attendance", 4.7f, Color(0xFFFF9800))
        PerformanceCategoryRow("Communication", 4.3f, Color(0xFF1E88E5))
        PerformanceCategoryRow("Innovation", 4.4f, Color(0xFF9C27B0))
    }
}

@Composable
fun PerformanceCategoryRow(title: String, rating: Float, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            LinearProgressIndicator(
                progress = { rating / 5f },
                modifier = Modifier
                    .weight(2f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = color,
                trackColor = color.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(rating.toString(), color = color, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MonthlyPerformanceTrend() {
    val points = listOf(
        Point(0f, 4.1f),
        Point(1f, 4.2f),
        Point(2f, 4.3f),
        Point(3f, 4.4f),
        Point(4f, 4.5f),
        Point(5f, 4.5f)
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(40.dp)
        .steps(points.size - 1)
        .labelData { i -> listOf("Jul", "Aug", "Sep", "Oct", "Nov", "Dec")[i] }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i -> String.format("%.1f", 3.5f + i * 0.4) }
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = points,
                    lineStyle = LineStyle(color = Color(0xFF43A047)),
                    intersectionPoint = IntersectionPoint(color = Color(0xFF43A047)),
                    selectionHighlightPoint = SelectionHighlightPoint(color = Color(0xFF43A047)),
                    shadowUnderLine = ShadowUnderLine(alpha = 0.2f, brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color(0xFF43A047), Color.Transparent))),
                    selectionHighlightPopUp = SelectionHighlightPopUp(popUpLabel = { x, y -> "${String.format("%.1f", y)}" })
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.LightGray.copy(alpha = 0.5f))
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Monthly Performance Trend", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                lineChartData = lineChartData
            )
        }
    }
}

@Composable
fun NotesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Text(
            text = "Note: Performance ratings are updated monthly by your manager. This is a read-only view for your reference.",
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF43A047),
            fontSize = 12.sp
        )
    }
}