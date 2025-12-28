// REFACTOR: Made self-contained with Scaffold and TopAppBar
package com.example.employeeperformancetracker.ui.meetingbooking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.employeeperformancetracker.R
import com.example.employeeperformancetracker.ui.theme.EmployeePerformanceTrackerTheme
import kotlinx.coroutines.launch

private val PrimaryBlue = Color(0xFF3949AB)
private val BackgroundGray = Color(0xFFFAFAFA)
private val Green = Color(0xFF43A047)
private val Red = Color(0xFFF44336)

data class MeetingRoom(
    val id: Int,
    val name: String,
    val capacity: Int,
    val type: String,
    val isAvailable: Boolean,
    val imageId: Int
)

val availableRooms = listOf(
    MeetingRoom(1, "Phoenix Room", 12, "Conference", true, R.drawable.ic_launcher_background),
    MeetingRoom(2, "Orion Room", 6, "VC Room", false, R.drawable.ic_launcher_background),
    MeetingRoom(3, "Pegasus Room", 4, "Small", true, R.drawable.ic_launcher_background),
    MeetingRoom(4, "Draco Room", 20, "Large", true, R.drawable.ic_launcher_background)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingBookingScreen(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book a Meeting Room", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { BookingForm() }
            item { RoomFilters() }
            items(availableRooms) { room ->
                AvailableRoomCard(room = room)
            }
        }
    }
}

@Composable
fun BookingForm() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Date") }, modifier = Modifier.weight(1f), trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) })
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Start Time") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("End Time") }, modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DropdownField(label = "Building", options = listOf("A", "B", "C"), modifier = Modifier.weight(1f))
            DropdownField(label = "Floor", options = listOf("1", "2", "3"), modifier = Modifier.weight(1f))
            DropdownField(label = "Wing", options = listOf("East", "West"), modifier = Modifier.weight(1f))
        }
        OutlinedTextField(
            value = "", onValueChange = {},
            label = { Text("Search room...") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(label: String, options: List<String>, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOption = selectionOption
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun RoomFilters() {
    val filters = listOf("All Rooms", "Conference Room", "VC Room", "Small", "Large")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(filters) { filter ->
            FilterChip(selected = filter == "All Rooms", onClick = { /* TODO */ }, label = { Text(filter) })
        }
    }
}

@Composable
fun AvailableRoomCard(room: MeetingRoom) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = room.imageId),
                contentDescription = room.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(room.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Capacity: ${room.capacity}", fontSize = 14.sp, color = Color.Gray)
                Text("Type: ${room.type}", fontSize = 14.sp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(if (room.isAvailable) Green else Red, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (room.isAvailable) "Available" else "Not Available", color = if (room.isAvailable) Green else Red, fontSize = 14.sp)
                }
            }
            Button(onClick = { /* TODO */ }, enabled = room.isAvailable) {
                Text("Book Now")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun MeetingBookingScreenPreview() {
    EmployeePerformanceTrackerTheme {
        MeetingBookingScreen(rememberNavController(), rememberDrawerState(DrawerValue.Closed))
    }
}
