package com.example.employeeperformancetracker.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// --- Data Models for Navigation --- //
data class NavItem(val screen: Screen)
data class ExpandableNavGroup(val title: String, val icon: ImageVector, val items: List<NavItem>)

// --- Navigation Structure --- //
val mainMenu = listOf(
    NavItem(Screen.Dashboard),
    NavItem(Screen.EmployeeList),
    NavItem(Screen.TaskList),
    NavItem(Screen.Analytics),
    NavItem(Screen.Attendance),
    NavItem(Screen.Payroll),
    NavItem(Screen.Reimbursements),
    NavItem(Screen.MeetingBooking),
    NavItem(Screen.QuickLinks) // Added Quick Links
)

val expandableModules = listOf(
    ExpandableNavGroup("Recruitment", Icons.Default.PersonAdd, listOf(
        NavItem(Screen.Dashboard), // Placeholder
        NavItem(Screen.Dashboard), // Placeholder
        NavItem(Screen.Dashboard) // Placeholder
    )),
    ExpandableNavGroup("Self Services", Icons.Default.ManageAccounts, listOf(
        NavItem(Screen.Dashboard), // Placeholder
        NavItem(Screen.Dashboard), // Placeholder
        NavItem(Screen.Dashboard), // Placeholder
        NavItem(Screen.Dashboard), // Placeholder
        NavItem(Screen.Dashboard) // Placeholder
    ))
    // Add other expandable modules here
)

val quickLinks = listOf(
    NavItem(Screen.Settings)
)

// --- Drawer Composable --- //

@Composable
fun SideNavigationDrawer(selectedRoute: String, onItemSelected: (String) -> Unit) {
    ModalDrawerSheet {
        LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
            item { NavigationSectionLabel("Main Menu") }
            items(mainMenu) {
                NavigationDrawerItem(
                    icon = { it.screen.icon?.let { icon -> Icon(icon, contentDescription = it.screen.label) } },
                    label = { Text(it.screen.label) },
                    selected = selectedRoute == it.screen.route,
                    onClick = { onItemSelected(it.screen.route) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
            item { Divider(modifier = Modifier.padding(vertical = 16.dp)) }

            items(expandableModules) {
                ExpandableNavigationGroup(group = it, selectedRoute = selectedRoute, onItemSelected = onItemSelected)
            }

            item { Divider(modifier = Modifier.padding(vertical = 16.dp)) }
            item { NavigationSectionLabel("Quick Links") }
            items(quickLinks) {
                NavigationDrawerItem(
                    icon = { it.screen.icon?.let { icon -> Icon(icon, contentDescription = it.screen.label) } },
                    label = { Text(it.screen.label) },
                    selected = selectedRoute == it.screen.route,
                    onClick = { onItemSelected(it.screen.route) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }
}

@Composable
fun NavigationSectionLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ExpandableNavigationGroup(group: ExpandableNavGroup, selectedRoute: String, onItemSelected: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(group.items.any { it.screen.route == selectedRoute }) }

    Column(modifier = Modifier.padding(horizontal = 4.dp)) {
        NavigationDrawerItem(
            icon = { Icon(group.icon, contentDescription = group.title) },
            label = { Text(group.title) },
            selected = false, // The group itself is not selectable
            badge = {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            },
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        if (isExpanded) {
            Column(modifier = Modifier.padding(start = 24.dp)) {
                group.items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { item.screen.icon?.let { icon -> Icon(icon, contentDescription = item.screen.label) } },
                        label = { Text(item.screen.label) },
                        selected = selectedRoute == item.screen.route,
                        onClick = { onItemSelected(item.screen.route) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    }
}
