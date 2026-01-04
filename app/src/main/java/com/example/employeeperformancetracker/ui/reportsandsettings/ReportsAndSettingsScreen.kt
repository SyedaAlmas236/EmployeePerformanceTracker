package com.example.employeeperformancetracker.ui.reportsandsettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsAndSettingsScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController) },
        containerColor = Color(0xFFF2F4F7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SettingsSection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text(text = "Settings", color = Color.White) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2A3A8D))
    )
}

@Composable
private fun SettingsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            SettingItem(icon = Icons.Default.Settings, title = "General")
            Divider()
            SettingItem(icon = Icons.Default.Notifications, title = "Notifications")
            Divider()
            SettingItem(icon = Icons.Default.Security, title = "Security")
            Divider()
            SettingItem(icon = Icons.Default.Shield, title = "Privacy")
            Divider()
            SettingItem(icon = Icons.Default.CloudDownload, title = "Data and Export")
            Divider()
            SettingItem(icon = Icons.AutoMirrored.Filled.Logout, title = "Logout", isLogout = true)
        }
    }
}

@Composable
private fun SettingItem(icon: ImageVector, title: String, isLogout: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = if (isLogout) Color.Red else Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title, 
            fontWeight = FontWeight.SemiBold, 
            color = if (isLogout) Color.Red else Color.Black,
            modifier = Modifier.weight(1f)
        )
        if (!isLogout) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Arrow", tint = Color.Gray)
        }
    }
}
