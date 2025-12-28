package com.example.employeeperformancetracker.ui.quicklinks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.R

private val PrimaryColor = Color(0xFF3949AB)
private val BackgroundColor = Color(0xFFFAFAFA)
private val AlertRed = Color(0xFFF44336)
private val OrangeAccent = Color(0xFFFB8C00)
private val GrayText = Color(0xFF9E9E9E)

// --- Data Models ---
data class NewsItem(val icon: ImageVector, val title: String, val date: String, val category: String)
data class Announcement(val title: String, val description: String, val date: String, val priority: Priority)
data class Birthday(val name: String, val department: String, val date: String, val imageRes: Int)
data class Anniversary(val name: String, val role: String, val years: Int, val date: String, val imageRes: Int)

enum class Priority { High, Medium, Normal }

// --- Sample Data ---
val newsItems = listOf(
    NewsItem(Icons.Default.TrendingUp, "Q4 Financial Results", "2024-11-14", "Company News"),
    NewsItem(Icons.Default.Shield, "New Health Benefits", "2024-11-13", "Benefits"),
    NewsItem(Icons.Default.Group, "Team Building Events", "2024-11-12", "Events")
)

val announcements = listOf(
    Announcement("Office Closure on Thanksgiving", "The office will be closed on November 28th for Thanksgiving holiday.", "2024-11-15", Priority.High),
    Announcement("New Parking Policy Effective Dec 1st", "Please review the updated parking guidelines in the employee portal.", "2024-11-14", Priority.Medium)
)

val upcomingBirthdays = listOf(
    Birthday("Sarah Mitchell", "Engineering", "November 16", R.drawable.ic_launcher_background),
    Birthday("John Anderson", "Marketing", "November 18", R.drawable.ic_launcher_background),
    Birthday("Emily Chen", "Design", "November 20", R.drawable.ic_launcher_background)
)

val workAnniversaries = listOf(
    Anniversary("Michael Rodriguez", "Operations", 5, "November 17", R.drawable.ic_launcher_background),
    Anniversary("Lisa Thompson", "HR", 3, "November 19", R.drawable.ic_launcher_background)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickLinksScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Links") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "News, Updates & Celebrations",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            NewsUpdatesCard()
            Spacer(Modifier.height(16.dp))
            AnnouncementsCard()
            Spacer(Modifier.height(16.dp))
            UpcomingBirthdaysCard()
            Spacer(Modifier.height(16.dp))
            WorkAnniversariesCard()
        }
    }
}

@Composable
fun NewsUpdatesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Feed, contentDescription = "News & Updates", tint = PrimaryColor)
                Spacer(Modifier.width(8.dp))
                Text("News & Updates", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            newsItems.forEach { item ->
                NewsItemRow(item = item)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun NewsItemRow(item: NewsItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(item.icon, contentDescription = null, tint = PrimaryColor)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(item.title, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Date", modifier = Modifier.size(14.dp), tint = GrayText)
                        Spacer(Modifier.width(4.dp))
                        Text(item.date, fontSize = 12.sp, color = GrayText)
                    }
                }
            }
            Text(item.category, fontSize = 12.sp, color = PrimaryColor, modifier = Modifier.background(PrimaryColor.copy(alpha = 0.1f), RoundedCornerShape(50)).padding(horizontal = 8.dp, vertical = 4.dp))
        }
    }
}

@Composable
fun AnnouncementsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Campaign, contentDescription = "Announcements", tint = AlertRed)
                Spacer(Modifier.width(8.dp))
                Text("Announcements", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            announcements.forEach { announcement ->
                AnnouncementItem(announcement = announcement)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AnnouncementItem(announcement: Announcement) {
    val priorityColor = when (announcement.priority) {
        Priority.High -> AlertRed
        Priority.Medium -> OrangeAccent
        Priority.Normal -> GrayText
    }
    Row(Modifier.padding(vertical = 4.dp)) {
        Box(modifier = Modifier
            .width(4.dp)
            .height(80.dp)
            .background(priorityColor, RoundedCornerShape(50)))
        Spacer(Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(announcement.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(announcement.priority.name, fontSize = 12.sp, color = priorityColor, modifier = Modifier.background(priorityColor.copy(alpha = 0.1f), RoundedCornerShape(50)).padding(horizontal = 10.dp, vertical = 4.dp), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Text(announcement.description, fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Date", modifier = Modifier.size(14.dp), tint = GrayText)
                Spacer(Modifier.width(4.dp))
                Text(announcement.date, fontSize = 12.sp, color = GrayText)
            }
        }
    }
}


@Composable
fun UpcomingBirthdaysCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Cake, contentDescription = "Upcoming Birthdays", tint = Color(0xFFEC407A))
                Spacer(Modifier.width(8.dp))
                Text("Upcoming Birthdays", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            upcomingBirthdays.forEach { birthday ->
                CelebrationItem(imageRes = birthday.imageRes, title = birthday.name, subtitle = birthday.department, date = birthday.date, icon = Icons.Default.CalendarToday, cardColor = Color(0xFFE8F5E9))
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun WorkAnniversariesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WorkspacePremium, contentDescription = "Work Anniversaries", tint = Color(0xFFFFA726))
                Spacer(Modifier.width(8.dp))
                Text("Work Anniversaries", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            workAnniversaries.forEach { anniversary ->
                val anniversaryText = "${anniversary.years} Years"
                CelebrationItem(imageRes = anniversary.imageRes, title = anniversary.name, subtitle = anniversary.role, date = anniversary.date, customText = anniversaryText, icon = Icons.Default.Star, cardColor = Color(0xFFFFF3E0))
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CelebrationItem(
    imageRes: Int,
    title: String,
    subtitle: String,
    date: String,
    icon: ImageVector,
    cardColor: Color,
    customText: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = imageRes), contentDescription = null, modifier = Modifier.size(40.dp).clip(CircleShape))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, fontSize = 12.sp, color = GrayText)
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = GrayText)
                    Spacer(Modifier.width(4.dp))
                    if (customText != null) {
                        Text(customText, fontSize = 14.sp, color = GrayText, fontWeight = FontWeight.SemiBold)
                    } else {
                         Text(date, fontSize = 14.sp, color = GrayText, fontWeight = FontWeight.SemiBold)
                    }
                }
                 if (customText != null) {
                    Text(date, fontSize = 12.sp, color = GrayText)
                 }
            }
        }
    }
}
