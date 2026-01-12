package com.example.employeeperformancetracker.data

import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AttendanceRepository {
    private val supabase = SupabaseConfig.client
    
    // Fetch attendance for current user
    suspend fun getAttendanceForUser(userId: String): List<Attendance> {
        return try {
            supabase.from("attendance").select {
                filter {
                    eq("user_id", userId)
                }
                order("date", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
            }.decodeList<Attendance>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Mark attendance
    suspend fun markAttendance(userId: String, status: String): Result<Unit> {
        return try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            
            // Check if already marked for today
            val existing = supabase.from("attendance").select {
                filter {
                    eq("user_id", userId)
                    eq("date", currentDate)
                }
            }.decodeSingleOrNull<Attendance>()

            if (existing != null) {
                return Result.failure(Exception("Attendance already marked for today"))
            }

            val attendance = Attendance(
                userId = userId,
                date = currentDate,
                status = status,
                markedAt = currentTime
            )
            
            supabase.from("attendance").insert(attendance)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
