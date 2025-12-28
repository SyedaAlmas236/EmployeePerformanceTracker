package com.example.employeeperformancetracker.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoRow(
    icon: ImageVector,
    value: String,
    label: String? = null,
    iconContentDescription: String? = label
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = iconContentDescription, tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        if (label != null) {
            Column {
                Text(label, fontSize = 12.sp, color = Color.Gray)
                Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        } else {
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Composable
fun ActionItem(icon: ImageVector, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun SettingsToggle(icon: ImageVector, title: String, subtitle: String, initialChecked: Boolean) {
    var isChecked by remember { mutableStateOf(initialChecked) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
        Switch(checked = isChecked, onCheckedChange = { isChecked = it })
    }
}
