package com.example.employeeperformancetracker.ui.reportsandsettings

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReportsAndSettingsViewModel : ViewModel() {

    private val _department = MutableStateFlow("All Departments")
    val department = _department.asStateFlow()

    private val _dateRange = MutableStateFlow("Last Month")
    val dateRange = _dateRange.asStateFlow()

    private val _ratingRange = MutableStateFlow("All Ratings")
    val ratingRange = _ratingRange.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled = _notificationsEnabled.asStateFlow()

    private val _autoBackupEnabled = MutableStateFlow(true)
    val autoBackupEnabled = _autoBackupEnabled.asStateFlow()

    fun onDepartmentChange(newDepartment: String) {
        _department.update { newDepartment }
    }

    fun onDateRangeChange(newDateRange: String) {
        _dateRange.update { newDateRange }
    }

    fun onRatingRangeChange(newRatingRange: String) {
        _ratingRange.update { newRatingRange }
    }

    fun onNotificationsChange(enabled: Boolean) {
        _notificationsEnabled.update { enabled }
    }

    fun onAutoBackupChange(enabled: Boolean) {
        _autoBackupEnabled.update { enabled }
    }

    fun exportToCsv() {
        // In a real app, this would query data based on filters,
        // convert to CSV, and save to the device's Downloads folder.
        // This requires context and file permissions.
        Log.d("ReportsAndSettings", "Exporting to CSV with filters: ${department.value}, ${dateRange.value}, ${ratingRange.value}")
        // Here you would show a Toast, which requires a Context.
    }
}
