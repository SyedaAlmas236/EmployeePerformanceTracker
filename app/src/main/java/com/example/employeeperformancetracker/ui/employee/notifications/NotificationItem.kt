package com.example.employeeperformancetracker.ui.employee.notifications

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotificationItem(notification: EmployeeNotification) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(getNotificationColor(notification.type).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getNotificationIcon(notification.type),
                    contentDescription = null,
                    tint = getNotificationColor(notification.type),
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1A1C1E)
                    )
                    
                    notification.status?.let { status ->
                        StatusChip(status)
                    } ?: run {
                        if (notification.type != NotificationType.LEAVE) {
                            Text(
                                text = "Info",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .background(Color(0xFFF1F3F4), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = notification.description,
                    fontSize = 14.sp,
                    color = Color(0xFF49454F),
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.timestamp,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    
                    if (notification.type == NotificationType.MEETING && notification.meetingLink != null) {
                        TextButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notification.meetingLink))
                                context.startActivity(intent)
                            },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Join Meeting", fontSize = 14.sp, color = Color(0xFF3F51B5))
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Outlined.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: NotificationStatus) {
    val (bgColor, textColor) = when (status) {
        NotificationStatus.PENDING -> Color(0xFFFFE0B2) to Color(0xFFE65100)
        NotificationStatus.APPROVED -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        NotificationStatus.REJECTED -> Color(0xFFFFEBEE) to Color(0xFFC62828)
    }
    
    Text(
        text = status.name.lowercase().replaceFirstChar { it.uppercase() },
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = textColor,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.LEAVE -> Icons.Default.History
        NotificationType.MEETING -> Icons.Default.VideoCameraFront
        NotificationType.TASK -> Icons.Default.Assignment
        NotificationType.INFO -> Icons.Default.Campaign
    }
}

fun getNotificationColor(type: NotificationType): Color {
    return when (type) {
        NotificationType.LEAVE -> Color(0xFFE65100)
        NotificationType.MEETING -> Color(0xFF3F51B5)
        NotificationType.TASK -> Color(0xFF9C27B0)
        NotificationType.INFO -> Color(0xFF455A64)
    }
}
