package com.example.employeeperformancetracker.ui.attendance

import androidx.lifecycle.ViewModel
import com.example.employeeperformancetracker.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AttendanceData(
    val name: String,
    val time: String,
    val status: String,
    val image: Int
)

class AttendanceViewModel : ViewModel() {

    private val _attendanceList = MutableStateFlow<List<AttendanceData>>(emptyList())
    val attendanceList: StateFlow<List<AttendanceData>> = _attendanceList.asStateFlow()

    val presentCount = "85"
    val onLeaveCount = "12"

    init {
        loadAttendanceData()
    }

    private fun loadAttendanceData() {
        _attendanceList.value = listOf(
            AttendanceData("Sarah Mitchell", "09:00 AM - 06:00 PM", "Present", R.drawable.ic_launcher_background),
            AttendanceData("John Anderson", "08:45 AM - In Progress", "Present", R.drawable.ic_launcher_background),
            AttendanceData("Emily Chen", "--- ", "On Leave", R.drawable.ic_launcher_background),
            AttendanceData("Michael Rodriguez", "10:15 AM - In Progress", "Late", R.drawable.ic_launcher_background)
        )
    }
}
