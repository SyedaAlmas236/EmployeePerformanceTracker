package com.example.employeeperformancetracker.ui.payroll

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PayrollHistoryItem(
    val month: String,
    val date: String,
    val amount: String,
    val status: String
)

class PayrollViewModel : ViewModel() {

    val currentMonthSalary = "$5,200"
    val currentMonth = "November 2024"

    private val _payrollHistory = MutableStateFlow<List<PayrollHistoryItem>>(emptyList())
    val payrollHistory: StateFlow<List<PayrollHistoryItem>> = _payrollHistory.asStateFlow()

    init {
        loadPayrollHistory()
    }

    private fun loadPayrollHistory() {
        _payrollHistory.value = listOf(
            PayrollHistoryItem("November 2024", "Nov 1, 2024", "$5,200", "Paid"),
            PayrollHistoryItem("October 2024", "Oct 1, 2024", "$5,200", "Paid"),
            PayrollHistoryItem("September 2024", "Sep 1, 2024", "$5,000", "Paid")
        )
    }
}
