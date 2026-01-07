package com.example.employeeperformancetracker.data

import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object EmployeeRepository {
    private val supabase = SupabaseConfig.client
    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees = _employees.asStateFlow()

    suspend fun fetchEmployees(): Result<List<Employee>> {
        return try {
            val employeeList = supabase.from("employees").select().decodeList<Employee>()
            _employees.value = employeeList
            Result.success(employeeList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUnregisteredEmployees(): List<Employee> {
        return try {
            supabase.from("employees").select {
                filter {
                    exact("user_id", null)
                }
            }.decodeList<Employee>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addEmployee(employee: Employee): Result<Unit> {
        return try {
            supabase.from("employees").insert(employee)
            fetchEmployees() // Refresh local state
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getEmployeeByName(name: String?): Employee? = _employees.value.find { it.name == name }
    
    fun getEmployees(): List<Employee> = _employees.value

    suspend fun getEmployeeById(id: String): Employee? {
        return try {
            supabase.from("employees").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull<Employee>()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateEmployee(employee: Employee): Result<Unit> {
        return try {
            supabase.from("employees").update(employee) {
                filter {
                    eq("id", employee.id ?: "")
                }
            }
            fetchEmployees() // Refresh local state
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEmployeeByAuthId(userId: String): Employee? {
        return try {
            supabase.from("employees").select {
                filter {
                    eq("user_id", userId)
                }
            }.decodeSingleOrNull<Employee>()
        } catch (e: Exception) {
            null
        }
    }
}