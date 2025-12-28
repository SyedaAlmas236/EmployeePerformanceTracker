package com.example.employeeperformancetracker.ui.reportsandsettings

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.io.File

data class ReportData(
    val employeeName: String,
    val department: String,
    val rating: Double,
    val tasksCompleted: Int,
    val status: String
)

class ReportsViewModel : ViewModel() {

    private val _department = MutableStateFlow("All Departments")
    val department: StateFlow<String> = _department.asStateFlow()

    private val _dateRange = MutableStateFlow("Last Month")
    val dateRange: StateFlow<String> = _dateRange.asStateFlow()

    private val _ratingRange = MutableStateFlow("All Ratings")
    val ratingRange: StateFlow<String> = _ratingRange.asStateFlow()

    private val _reportData = MutableStateFlow<List<ReportData>>(emptyList())
    val reportData: StateFlow<List<ReportData>> = _reportData.asStateFlow()

    private val allReportData = listOf(
        ReportData("John Doe", "Engineering", 4.5, 23, "Active"),
        ReportData("Jane Smith", "Design", 4.8, 15, "Active"),
        ReportData("Peter Jones", "Management", 4.2, 10, "On Leave"),
        ReportData("Mary Johnson", "Marketing", 4.9, 30, "Active"),
        ReportData("Chris Lee", "Engineering", 3.8, 18, "Active"),
        ReportData("Patricia Brown", "Design", 4.0, 22, "Active"),
        ReportData("Robert Williams", "Marketing", 3.5, 12, "Terminated"),
        ReportData("Linda Davis", "Analysis", 4.6, 25, "Active"),
    )

    init {
        viewModelScope.launch {
            combine(department, dateRange, ratingRange) { dept, date, rating ->
                filterReportData(dept, date, rating)
            }.collect { filteredList ->
                _reportData.value = filteredList
            }
        }
    }

    private fun filterReportData(department: String, dateRange: String, ratingRange: String): List<ReportData> {
        // Date range filtering is not yet implemented
        var filteredList = allReportData

        if (department != "All Departments") {
            filteredList = filteredList.filter { it.department == department }
        }

        filteredList = when (ratingRange) {
            "5 Stars" -> filteredList.filter { it.rating >= 5 }
            "4–5 Stars" -> filteredList.filter { it.rating >= 4 && it.rating <= 5 }
            "3–4 Stars" -> filteredList.filter { it.rating >= 3 && it.rating <= 4 }
            "Below 3 Stars" -> filteredList.filter { it.rating < 3 }
            else -> filteredList
        }

        return filteredList
    }

    fun setDepartment(value: String) {
        _department.value = value
    }

    fun setDateRange(value: String) {
        _dateRange.value = value
    }

    fun setRatingRange(value: String) {
        _ratingRange.value = value
    }

    fun exportToCSV(context: Context) {
        val csvData = generateCsvString()
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, "employee_reports.csv")
            file.writeText(csvData)
            Toast.makeText(context, "Exported Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Export Failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun generateCsvString(): String {
        val header = "Employee Name,Department,Rating,Tasks Completed,Status\n"
        return header + reportData.value.joinToString("\n") {
            "${it.employeeName},${it.department},${it.rating},${it.tasksCompleted},${it.status}"
        }
    }
}
