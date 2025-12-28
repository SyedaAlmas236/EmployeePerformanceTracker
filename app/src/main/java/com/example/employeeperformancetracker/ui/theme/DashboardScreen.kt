
package com.example.employeeperformancetracker.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.employeeperformancetracker.R

val DarkBlue = Color(0xFF3A42B4)
val LightBlue = Color(0xFFE8EAF6)
val Orange = Color(0xFFF57C00)
val LightOrange = Color(0xFFFFF3E0)
val Green = Color(0xFF4CAF50)
val LightGreen = Color(0xFFE8F5E9)
val Red = Color(0xFFD32F2F)
val LightRed = Color(0xFFFFEBEE)
val LightGray = Color(0xFFF5F5F5)

@Composable
fun DashboardScreen() {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGray)
                .padding(paddingValues)
        ) {
            item { Header() }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { StatsGrid() }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { TopPerformersSection() }
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                DarkBlue,
                shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with actual profile image
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Dashboard",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Welcome back, Admin",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
fun StatsGrid() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .offset(y = (-80).dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                icon = Icons.Outlined.PeopleOutline,
                label = "Total Employees",
                value = "124",
                color = DarkBlue,
                backgroundColor = LightBlue,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Outlined.ListAlt,
                label = "Tasks Pending",
                value = "37",
                color = Orange,
                backgroundColor = LightOrange,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                icon = Icons.Outlined.StarOutline,
                label = "Average Rating",
                value = "4.2",
                color = Green,
                backgroundColor = LightGreen,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Outlined.TrendingUp,
                label = "Top Performer",
                value = "Sarah M.",
                color = Red,
                backgroundColor = LightRed,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, fontSize = 14.sp, color = Color.Gray)
                Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(backgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = label, tint = color)
            }
        }
    }
}

@Composable
fun TopPerformersSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-60).dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Top Performers",
                    tint = Green
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Top 3 Performers",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            PerformerCard(
                rank = 1,
                name = "Sarah Mitchell",
                role = "Senior Developer",
                rating = 4.9,
                imageId = R.drawable.ic_launcher_background // Replace with actual image
            )
            Spacer(modifier = Modifier.height(16.dp))
            PerformerCard(
                rank = 2,
                name = "John Anderson",
                role = "Project Manager",
                rating = 4.8,
                imageId = R.drawable.ic_launcher_background // Replace with actual image
            )
        }
    }
}

@Composable
fun PerformerCard(rank: Int, name: String, role: String, rating: Double, imageId: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(DarkBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = rank.toString(), color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = imageId),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = role, fontSize = 14.sp, color = Color.Gray)
            }
        }
        RatingBadge(rating = rating)
    }
}

@Composable
fun RatingBadge(rating: Double) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Green)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = rating.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkBlue,
                selectedTextColor = DarkBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = LightBlue
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.Groups, contentDescription = "Employees") },
            label = { Text("Employees") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkBlue,
                selectedTextColor = DarkBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = LightBlue
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.Checklist, contentDescription = "Tasks") },
            label = { Text("Tasks") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkBlue,
                selectedTextColor = DarkBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = LightBlue
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Analytics") },
            label = { Text("Analytics") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkBlue,
                selectedTextColor = DarkBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = LightBlue
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkBlue,
                selectedTextColor = DarkBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = LightBlue
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    EmployeePerformanceTrackerTheme {
        DashboardScreen()
    }
}
