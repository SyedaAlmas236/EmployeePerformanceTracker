package com.example.employeeperformancetracker.data

import io.github.jan.supabase.postgrest.from

object TaskRepository {
    private val supabase = SupabaseConfig.client

    suspend fun getTasks(): List<Task> {
        return try {
            supabase.from("tasks").select().decodeList<Task>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addTask(task: Task): Result<Unit> {
        return try {
            supabase.from("tasks").insert(task)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTaskStatus(taskId: String, status: String): Result<Unit> {
        return try {
            supabase.from("tasks").update({
                set("status", status)
            }) {
                filter {
                    eq("id", taskId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            supabase.from("tasks").delete {
                filter {
                    eq("id", taskId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
