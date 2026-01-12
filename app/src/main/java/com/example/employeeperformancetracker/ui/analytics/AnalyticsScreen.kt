package com.example.employeeperformancetracker.ui.analytics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.employeeperformancetracker.data.Task
import com.example.employeeperformancetracker.data.TaskRepository
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, drawerState: DrawerState) {
    val employees by EmployeeRepository.employees.collectAsState()
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        EmployeeRepository.fetchEmployees()
        tasks = TaskRepository.getTasks()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics", fontWeight = FontWeight.Bold) },
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
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3F51B5))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCardsGrid(employees, tasks)
                PerformanceBarChart(employees)
                DepartmentPieChart(employees)
                PerformanceTrendLineChart()
                PerformanceCards(employees)
            }
        }
    }
}

@Composable
fun SummaryCardsGrid(employees: List<Employee>, tasks: List<Task>) {
    val avgRating = if (employees.isNotEmpty()) employees.mapNotNull { it.rating }.average().toFloat() else 0f
    val completedTasks = tasks.count { it.status?.lowercase()?.replace("_", " ") == "completed" }
    val pendingTasks = tasks.count { 
        val s = it.status?.lowercase()?.replace("_", " ")
        s == "pending" || s == "in progress" || s == "not started"
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummaryCard(Modifier.weight(1f), "Total Employees", employees.size.toString(), Icons.Default.Groups, Color(0xFF3F51B5))
            SummaryCard(Modifier.weight(1f), "Avg Rating", String.format(Locale.getDefault(), "%.1f", avgRating), Icons.Default.Star, Color(0xFFFFC107))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummaryCard(Modifier.weight(1f), "Tasks Completed", completedTasks.toString(), Icons.Default.CheckCircle, Color(0xFF4CAF50))
            SummaryCard(Modifier.weight(1f), "Tasks Pending", pendingTasks.toString(), Icons.Default.PendingActions, Color(0xFFFF5722))
        }
    }
}

@Composable
fun SummaryCard(modifier: Modifier, label: String, value: String, icon: ImageVector, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun PerformanceBarChart(employees: List<Employee>) {
    val primaryColor = Color(0xFF3F51B5)
    val barData = employees.take(6).mapIndexed { index, employee ->
        BarData(
            point = Point(index.toFloat(), employee.rating ?: 0f), 
            label = employee.name.take(5),
            color = primaryColor
        )
    }

    if (barData.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth().height(350.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Employee Performance Ratings", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            val xAxisData = AxisData.Builder()
                .steps(barData.size - 1)
                .labelAndAxisLinePadding(12.dp)
                .labelData { index -> if (index >= 0 && index < barData.size) barData[index].label else "" }
                .build()

            val yAxisData = AxisData.Builder()
                .steps(5)
                .labelAndAxisLinePadding(20.dp)
                .labelData { index -> index.toString() }
                .build()

            val barChartData = BarChartData(
                chartData = barData,
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                backgroundColor = Color.White,
                showYAxis = true,
                showXAxis = true,
                barStyle = BarStyle(
                    barWidth = 30.dp
                )
            )
            BarChart(modifier = Modifier.fillMaxSize(), barChartData = barChartData)
        }
    }
}

@Composable
fun DepartmentPieChart(employees: List<Employee>) {
    val departments = listOf("Engineering", "Design", "Management", "Marketing", "Analytics")
    val deptCounts = departments.map { dept ->
        employees.count { it.department?.equals(dept, ignoreCase = true) == true }
    }
    
    val slices = departments.mapIndexed { index, dept ->
        PieChartData.Slice(dept, deptCounts[index].toFloat(), listOf(Color(0xFF3F51B5), Color(0xFF4CAF50), Color(0xFFFFC107), Color(0xFFFF5722), Color(0xFF9C27B0))[index % 5])
    }

    val pieChartData = PieChartData(
        slices = slices,
        plotType = co.yml.charts.common.model.PlotType.Pie
    )

    Card(
        modifier = Modifier.fillMaxWidth().height(350.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Department Performance", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            PieChart(
                modifier = Modifier.fillMaxSize(),
                pieChartData = pieChartData,
                pieChartConfig = PieChartConfig(showSliceLabels = true, sliceLabelTextColor = Color.White)
            )
        }
    }
}

@Composable
fun PerformanceTrendLineChart() {
    val pointsData = listOf(
        Point(0f, 3.5f), Point(1f, 3.8f), Point(2f, 4.2f), Point(3f, 4.0f), Point(4f, 4.5f), Point(5f, 4.7f)
    )

    val xAxisData = AxisData.Builder()
        .steps(pointsData.size - 1)
        .labelAndAxisLinePadding(12.dp)
        .labelData { index -> listOf("Jul", "Aug", "Sep", "Oct", "Nov", "Dec").getOrElse(index) { "" } }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelAndAxisLinePadding(20.dp)
        .labelData { index -> String.format(Locale.getDefault(), "%.1f", 3.0f + index * 0.4f) }
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(color = Color(0xFF4CAF50)),
                    intersectionPoint = co.yml.charts.ui.linechart.model.IntersectionPoint(color = Color(0xFF4CAF50)),
                    selectionHighlightPoint = SelectionHighlightPoint(color = Color(0xFF4CAF50)),
                    shadowUnderLine = ShadowUnderLine(alpha = 0.1f, brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color(0xFF4CAF50), Color.Transparent))),
                    selectionHighlightPopUp = SelectionHighlightPopUp(popUpLabel = { x, y -> String.format(Locale.getDefault(), "%.1f", y) })
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color.White
    )

    Card(
        modifier = Modifier.fillMaxWidth().height(350.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Monthly Performance Trend", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            LineChart(modifier = Modifier.fillMaxSize(), lineChartData = lineChartData)
        }
    }
}

@Composable
fun PerformanceCards(employees: List<Employee>) {
    val validEmployees = employees.filter { it.rating != null && it.rating!! > 0 }
    val sorted = validEmployees.sortedByDescending { it.rating }
    val topPerformer = sorted.firstOrNull()
    val needsAttention = sorted.lastOrNull()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        topPerformer?.let { 
            PerformanceInfoCard("Top Performer", it, Color(0xFF43A047), Icons.AutoMirrored.Filled.TrendingUp)
        }
        needsAttention?.let { 
            if (it != topPerformer) {
                PerformanceInfoCard("Needs Attention", it, Color(0xFFD32F2F), Icons.AutoMirrored.Filled.TrendingDown)
            }
        }
    }
}

@Composable
fun PerformanceInfoCard(title: String, employee: Employee, color: Color, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(employee.profileImageUrl ?: "https://api.dicebear.com/7.x/avataaars/svg?seed=${employee.name}"),
                contentDescription = null,
                modifier = Modifier.size(56.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(employee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(employee.position ?: "No Position", color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(String.format(Locale.getDefault(), "%.1f", employee.rating), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
