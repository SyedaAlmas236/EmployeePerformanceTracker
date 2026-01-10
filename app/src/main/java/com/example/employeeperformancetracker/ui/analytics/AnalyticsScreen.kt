package com.example.employeeperformancetracker.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, drawerState: DrawerState) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Analytics",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF3F51B5),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 48.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1A1C1E)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            AttendanceStatusCard()
            Spacer(modifier = Modifier.height(16.dp))
            AverageRatingCard()
            Spacer(modifier = Modifier.height(16.dp))
            DepartmentPerformanceCard()
            Spacer(modifier = Modifier.height(16.dp))
            RatingTrendsCard()
            Spacer(modifier = Modifier.height(16.dp))
            PerformanceCardsRow()
            Spacer(modifier = Modifier.height(16.dp))
            KeyInsightsCard()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AttendanceStatusCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Attendance Status (Today)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF1A1C1E),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(0.85f).fillMaxHeight().background(Color(0xFF4CAF50)))
                    Box(modifier = Modifier.weight(0.08f).fillMaxHeight().background(Color(0xFF3F51B5)))
                    Box(modifier = Modifier.weight(0.05f).fillMaxHeight().background(Color(0xFFFFC107)))
                    Box(modifier = Modifier.weight(0.02f).fillMaxHeight().background(Color(0xFF9E9E9E)))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    AttendanceLegendItem(Modifier.weight(1.2f), Color(0xFF4CAF50), "Present: 142")
                    AttendanceLegendItem(Modifier.weight(1f), Color(0xFF3F51B5), "On Leave: 8")
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    AttendanceLegendItem(Modifier.weight(1.2f), Color(0xFFFFC107), "Average: 4")
                    AttendanceLegendItem(Modifier.weight(1f), Color(0xFF9E9E9E), "Not Responded: 2")
                }
            }
        }
    }
}

@Composable
fun AttendanceLegendItem(modifier: Modifier, color: Color, text: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 13.sp, color = Color(0xFF49454F))
    }
}

@Composable
fun AverageRatingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Average Rating by Employee",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF1A1C1E),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(modifier = Modifier.fillMaxWidth().height(220.dp).padding(start = 24.dp, end = 12.dp, bottom = 8.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val yLines = 3
                    val stepY = size.height / (yLines + 2)
                    for (i in 0..yLines + 1) {
                        val y = size.height - (i * stepY)
                        drawLine(Color(0xFFF0F0F0), Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
                    }
                    drawLine(Color(0xFFCCCCCC), Offset(0f, 0f), Offset(0f, size.height), 1.dp.toPx())
                }
                
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AverageRatingBarItem("John S.", 0.75f)
                    AverageRatingBarItem("Michael B.", 0.9f)
                    AverageRatingBarItem("David L.", 0.82f)
                }
                
                Column(modifier = Modifier.fillMaxHeight().offset(x = (-20).dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Text("5", fontSize = 11.sp, color = Color.Gray)
                    Text("4", fontSize = 11.sp, color = Color.Gray)
                    Text("2", fontSize = 11.sp, color = Color.Gray)
                    Text("0", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun AverageRatingBarItem(name: String, heightFactor: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
        Box(modifier = Modifier.width(42.dp).fillMaxHeight(heightFactor).clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)).background(Color(0xFF3F51B5)))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, fontSize = 12.sp, color = Color(0xFF49454F))
    }
}

@Composable
fun DepartmentPerformanceCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Department Performance (%)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF1A1C1E),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(modifier = Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(160.dp)) {
                    drawArc(Color(0xFF3F51B5), -120f, 130f, true)
                    drawArc(Color(0xFF4CAF50), 10f, 110f, true)
                    drawArc(Color(0xFFFFC107), 120f, 90f, true)
                    drawArc(Color(0xFF9E9E9E), 210f, 30f, true)
                }

                Text(text = "Design: 92%", fontSize = 13.sp, color = Color(0xFF3F51B5), modifier = Modifier.align(Alignment.TopStart).padding(start = 16.dp))
                Text(text = "Engineering", fontSize = 13.sp, color = Color(0xFF4CAF50), modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp, bottom = 40.dp))
                Text(text = "ct: 88%", fontSize = 13.sp, color = Color(0xFFFFC107), modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 20.dp))
            }
        }
    }
}

@Composable
fun RatingTrendsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp).padding(start = 24.dp, end = 12.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val yLines = 3
                    val stepY = size.height / (yLines + 2)
                    for (i in 0..yLines + 1) {
                        val y = size.height - (i * stepY)
                        drawLine(Color(0xFFF0F0F0), Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
                    }
                    drawLine(Color(0xFFCCCCCC), Offset(0f, 0f), Offset(0f, size.height), 1.dp.toPx())

                    val points = listOf(0.4f, 0.5f, 0.6f, 0.52f, 0.75f, 0.85f)
                    val stepX = size.width / (points.size - 1)
                    val path = Path()
                    points.forEachIndexed { index, yFactor ->
                        val x = index * stepX
                        val y = size.height - (yFactor * size.height)
                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        drawCircle(Color(0xFF4CAF50), radius = 4.dp.toPx(), center = Offset(x, y))
                    }
                    drawPath(path, Color(0xFF4CAF50), style = Stroke(width = 2.dp.toPx()))
                }
                Column(modifier = Modifier.fillMaxHeight().offset(x = (-20).dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Text("4.3", fontSize = 11.sp, color = Color.Gray)
                    Text("3.9", fontSize = 11.sp, color = Color.Gray)
                    Text("3.5", fontSize = 11.sp, color = Color.Gray)
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach {
                    Text(text = it, fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun PerformanceCardsRow() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        PerformanceInfoCard(modifier = Modifier.weight(1f), title = "Top Performer", name = "Emily Chen", rating = "4.9/5.0", icon = Icons.Default.TrendingUp, color = Color(0xFF4CAF50))
        PerformanceInfoCard(modifier = Modifier.weight(1f), title = "Needs Support", name = "Anna M.", rating = "4.2/5.0", icon = Icons.Default.TrendingDown, color = Color(0xFFFF5722))
    }
}

@Composable
fun PerformanceInfoCard(modifier: Modifier, title: String, name: String, rating: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = rating, color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
        }
    }
}

@Composable
fun KeyInsightsCard() {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Key Insights", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF1A1C1E), modifier = Modifier.padding(bottom = 16.dp))
            InsightItem(Color(0xFF4CAF50), Color(0xFFE8F5E9), "Overall performance improved by 8% this month", "Design team leading with 92% efficiency")
            Spacer(modifier = Modifier.height(12.dp))
            InsightItem(Color(0xFF3F51B5), Color(0xFFE8EAF6), "91% attendance rate this week", "Highest in Q4 2024")
            Spacer(modifier = Modifier.height(12.dp))
            InsightItem(Color(0xFFFF5722), Color(0xFFFFF3E0), "28 pending tasks need attention", "Reassign or extend deadlines recommended")
        }
    }
}

@Composable
fun InsightItem(dotColor: Color, bgColor: Color, title: String, subtitle: String) {
    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(bgColor).padding(12.dp), verticalAlignment = Alignment.Top) {
        Box(modifier = Modifier.padding(top = 4.dp).size(8.dp).clip(CircleShape).background(dotColor))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1C1E))
            Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF49454F))
        }
    }
}
