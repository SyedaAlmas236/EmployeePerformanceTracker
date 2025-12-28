package com.example.employeeperformancetracker.ui.adminprofile

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.employeeperformancetracker.R
import com.example.employeeperformancetracker.ui.common.ActionItem
import com.example.employeeperformancetracker.ui.common.InfoRow
import com.example.employeeperformancetracker.ui.common.SettingsToggle
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme

@Composable
fun AdminProfileContent(
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Profile & Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onClose) {
                Text("Close")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Profile block
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4C5BD4)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background), // Placeholder
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Admin User", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("System Administrator", style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f)))
                    Text("EMP-0001", style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f)))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Contact Info
        Text("Contact Information", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.Email, value = "admin@workforce.com")
        InfoRow(icon = Icons.Default.Phone, value = "+1 (555) 123-4567")
        InfoRow(icon = Icons.Default.LocationOn, value = "San Francisco, CA")

        Spacer(Modifier.height(24.dp))

        // Quick Actions
        Text("Quick Actions", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        ActionItem(icon = Icons.Default.Person, text = "Edit Profile")
        ActionItem(icon = Icons.Default.Lock, text = "Change Password")

        Spacer(Modifier.height(24.dp))

        // Notification Settings
        Text("Notification Settings", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        SettingsToggle(icon = Icons.Default.Notifications, title = "Push Notifications", subtitle = "Receive app notifications", initialChecked = true)
        SettingsToggle(icon = Icons.Default.Email, title = "Email Notifications", subtitle = "Receive email updates", initialChecked = true)
        SettingsToggle(icon = Icons.Default.Alarm, title = "Task Reminders", subtitle = "Get reminded about tasks", initialChecked = true)

        Spacer(Modifier.height(24.dp))

        // App Preferences
        Text("App Preferences", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        SettingsToggle(icon = Icons.Default.DarkMode, title = "Dark Mode", subtitle = "Enable dark theme", initialChecked = false)
        SettingsToggle(icon = Icons.Default.Sync, title = "Auto-sync", subtitle = "Sync data automatically", initialChecked = true)

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(onClick = onClose) {
                Text("Cancel")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { onClose() }) {
                Text("Save Changes")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminProfileContentPreview() {
    EmployeePerformanceTrackerTheme {
        AdminProfileContent(onClose = {})
    }
}
