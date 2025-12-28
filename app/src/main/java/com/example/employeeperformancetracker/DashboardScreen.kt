
package com.example.employeeperformancetracker

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
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme

// Colors
val DarkBlue = Color(0xFF3A42B4)
val LightBlue = Color(0xFFE8EAF6)
val Orange = Color(0xFFF57C00)
val LightOrange = Color(0xFFFFEBEE) // A bit lighter orange for background
val Green = Color(0xFF388E3C)
val LightGreen = Color(0xFFE8F5E9)
val Red = Color(0xFFD32F2F)
val LightRed = Color(0xFFFFEBEE)
val LightGray = Color(0xFFF5F5F5)

@Composable
fun DashboardScreen() {
    Scaffold(
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGray)
                .padding(paddingValues)
        ) {
            item { HeaderSection() }
            item { Spacer(modifier = Modifier.height(60.dp)) } // Space for cards to overlap
            item { StatsCardGrid() }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { TopPerformersSection() }
        }
    }
}

@Composable
fun HeaderSection() {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    DarkBlue,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
                    painter = painterResource(id = R.drawable.ic_launcher_background), // Placeholder
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
        }
    }
}

@Composable
fun StatsCardGrid() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .offset(y = (-80).dp) // Negative offset to pull cards up
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.PeopleOutline,
                label = "Total Employees",
                value = "124",
                iconColor = DarkBlue,
                iconBackgroundColor = LightBlue
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Assignment,
                label = "Tasks Pending",
                value = "37",
                iconColor = Orange,
                iconBackgroundColor = LightOrange
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.StarBorder,
                label = "Average Rating",
                value = "4.2",
                iconColor = Green,
                iconBackgroundColor = LightGreen
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.TrendingUp,
                label = "Top Performer",
                value = "Sarah M.",
                iconColor = Red,
                iconBackgroundColor = LightRed
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color,
    iconBackgroundColor: Color
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = iconColor)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBackgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = label, tint = iconColor)
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
            .offset(y = (-60).dp), // Adjust position
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
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            PerformerCard(
                rank = 1,
                name = "Sarah Mitchell",
                role = "Senior Developer",
                rating = 4.9,
                imageId = R.drawable.ic_launcher_background // Placeholder
            )
            Spacer(modifier = Modifier.height(16.dp))
            PerformerCard(
                rank = 2,
                name = "John Anderson",
                role = "Project Manager",
                rating = 4.8,
                imageId = R.drawable.ic_launcher_background // Placeholder
            )
        }
    }
}

@Composable
fun PerformerCard(rank: Int, name: String, role: String, rating: Double, imageId: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(DarkBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(rank.toString(), color = Color.White, fontWeight = FontWeight.Bold)
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
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(role, fontSize = 14.sp, color = Color.Gray)
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
            Text(rating.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        val items = listOf("Home", "Employees", "Tasks", "Analytics", "Settings")
        val icons = listOf(
            Icons.Default.Home,
            Icons.Default.Groups,
            Icons.Default.Checklist,
            Icons.Default.BarChart,
            Icons.Default.Settings
        )

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == 0,
                onClick = { /* TODO */ },
                label = { Text(item) },
                icon = { Icon(icons[index], contentDescription = item) },
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
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun DashboardScreenPreview() {
    EmployeePerformanceTrackerTheme {
        DashboardScreen()
    }
}
