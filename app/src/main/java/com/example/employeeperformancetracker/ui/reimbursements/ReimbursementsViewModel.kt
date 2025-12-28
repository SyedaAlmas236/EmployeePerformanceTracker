package com.example.employeeperformancetracker.ui.reimbursements

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ReimbursementItem(
    val title: String,
    val type: String,
    val amount: String,
    val date: String,
    val status: String
)

class ReimbursementsViewModel : ViewModel() {

    val approvedCount = "2"
    val pendingCount = "1"
    val rejectedCount = "1"

    private val _reimbursements = MutableStateFlow<List<ReimbursementItem>>(emptyList())
    val reimbursements: StateFlow<List<ReimbursementItem>> = _reimbursements.asStateFlow()

    init {
        loadReimbursements()
    }

    private fun loadReimbursements() {
        _reimbursements.value = listOf(
            ReimbursementItem("Travel Expenses - Client Meeting", "Travel", "$250", "Nov 10, 2024", "Approved"),
            ReimbursementItem("Office Supplies Purchase", "Supplies", "$85", "Nov 12, 2024", "Pending"),
            ReimbursementItem("Conference Registration", "Training", "$450", "Nov 5, 2024", "Approved"),
            ReimbursementItem("Team Lunch", "Meal", "$120", "Nov 8, 2024", "Rejected")
        )
    }
}
