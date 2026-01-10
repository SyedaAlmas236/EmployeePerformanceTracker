package com.example.employeeperformancetracker.ui.adminprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.employeeperformancetracker.R
import com.example.employeeperformancetracker.ui.navigation.Screen

@Composable
fun AdminProfileScreen(navController: NavController) {
    var pushNotifications by remember { mutableStateOf(true) }
    var emailNotifications by remember { mutableStateOf(true) }
    var taskReminders by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(false) }
    var autoSync by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.4f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(24.dp))
                        Text(
                            text = "Profile & Settings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2A3A8D)
                        )
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Profile Info Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF3F51B5))
                        ) {
                            Row(
                                modifier = Modifier.padding(24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_background), // Using existing placeholder
                                    contentDescription = "Profile Photo",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Column {
                                    Text(
                                        text = "Admin User",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "System Administrator",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "EMP-0001",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        SectionTitle("Contact Information")
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
                        ) {
                            Column {
                                ContactItem(Icons.Default.Email, "Email", "admin@workforce.com", Color(0xFFE8EAF6), Color(0xFF3F51B5))
                                Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF0F0F0))
                                ContactItem(Icons.Default.Phone, "Phone", "+1 (555) 123-4567", Color(0xFFE8F5E9), Color(0xFF4CAF50))
                                Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF0F0F0))
                                ContactItem(Icons.Default.LocationOn, "Location", "San Francisco, CA", Color(0xFFFFF3E0), Color(0xFFFF9800))
                            }
                        }

                        SectionTitle("Quick Actions")
                        ActionItem(Icons.Default.PersonOutline, "Edit Profile", { })
                        Spacer(modifier = Modifier.height(8.dp))
                        ActionItem(Icons.Default.LockOpen, "Change Password", { })

                        SectionTitle("Notification Settings")
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
                        ) {
                            Column {
                                SettingSwitchItem(
                                    Icons.Default.NotificationsNone,
                                    "Push Notifications",
                                    "Receive app notifications",
                                    pushNotifications,
                                    { pushNotifications = it },
                                    Color(0xFFE8EAF6),
                                    Color(0xFF3F51B5)
                                )
                                Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF0F0F0))
                                SettingSwitchItem(
                                    Icons.Default.MailOutline,
                                    "Email Notifications",
                                    "Receive email updates",
                                    emailNotifications,
                                    { emailNotifications = it },
                                    Color(0xFFE8F5E9),
                                    Color(0xFF4CAF50)
                                )
                                Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF0F0F0))
                                SettingSwitchItem(
                                    Icons.Default.NotificationsActive,
                                    "Task Reminders",
                                    "Get reminded about tasks",
                                    taskReminders,
                                    { taskReminders = it },
                                    Color(0xFFFFF3E0),
                                    Color(0xFFFF9800)
                                )
                            }
                        }

                        SectionTitle("App Preferences")
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
                        ) {
                            Column {
                                SettingSwitchItem(
                                    Icons.Default.Settings,
                                    "Dark Mode",
                                    "Enable dark theme",
                                    darkMode,
                                    { darkMode = it },
                                    Color(0xFFE8EAF6),
                                    Color(0xFF3F51B5)
                                )
                                Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF0F0F0))
                                SettingSwitchItem(
                                    Icons.Default.Sync,
                                    "Auto-sync",
                                    "Sync data automatically",
                                    autoSync,
                                    { autoSync = it },
                                    Color(0xFFE8F5E9),
                                    Color(0xFF4CAF50)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Bottom Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD0D0D0))
                        ) {
                            Text("Close", color = Color.Black)
                        }
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                        ) {
                            Text("Save Changes", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(vertical = 12.dp),
        fontSize = 14.sp,
        color = Color.Gray
    )
}

@Composable
fun ContactItem(icon: ImageVector, label: String, value: String, bgColor: Color, iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ActionItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE8EAF6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, modifier = Modifier.weight(1f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
fun SettingSwitchItem(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, bgColor: Color, iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF3F51B5),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD0D0D0)
            )
        )
    }
}