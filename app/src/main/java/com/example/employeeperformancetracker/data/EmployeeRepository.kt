package com.example.employeeperformancetracker.data

import com.example.employeeperformancetracker.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object EmployeeRepository {
    private val _employees = MutableStateFlow(listOf(
        Employee("EMP001", "Sarah Mitchell", "Senior Developer", "Engineering", 4.9f, R.drawable.sarah_employee_pfp),
        Employee("EMP002", "John Anderson", "Project Manager", "Management", 4.8f, R.drawable.ic_launcher_background),
        Employee("EMP003", "Emily Chen", "UX Designer", "Design", 4.7f, R.drawable.ic_launcher_background),
        Employee("EMP004", "Michael Roberts", "Backend Developer", "Engineering", 4.6f, R.drawable.ic_launcher_background),
        Employee("EMP005", "Lisa Wang", "Marketing Manager", "Marketing", 4.5f, R.drawable.ic_launcher_background),
        Employee("EMP006", "David Kim", "Data Analyst", "Analytics", 4.4f, R.drawable.ic_launcher_background)
    ))

    val employees = _employees.asStateFlow()

    fun getEmployees(): List<Employee> = _employees.value

    fun getEmployeeByName(name: String?): Employee? = _employees.value.find { it.name == name }

    fun addEmployee(employee: Employee) {
        val currentList = _employees.value.toMutableList()
        currentList.add(employee)
        _employees.value = currentList
    }
}