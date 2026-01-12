package com.example.employeeperformancetracker.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class LeaveRequest(
    val id: String? = null,
    val user_id: String,
    val leave_type: String,
    val from_date: String,
    val to_date: String,
    val reason: String,
    val status: String = "pending",
    val created_at: String? = null
)

object LeaveRepository {

    suspend fun applyLeave(
        supabase: SupabaseClient,
        userId: String,
        leaveType: String,
        fromDateFormatted: String,
        toDateFormatted: String,
        reason: String
    ) {
        supabase.from("leave_requests").insert(
            mapOf(
                "user_id" to userId,
                "leave_type" to leaveType,
                "from_date" to fromDateFormatted,
                "to_date" to toDateFormatted,
                "reason" to reason,
                "status" to "pending"
            )
        )
    }

    suspend fun getAllLeaveRequests(
        supabase: SupabaseClient
    ): List<LeaveRequest> {
        return supabase.from("leave_requests")
            .select()
            .decodeList<LeaveRequest>()
    }

    suspend fun approveLeave(
        supabase: SupabaseClient,
        leaveId: String
    ) {
        supabase.from("leave_requests")
            .update(mapOf("status" to "approved")) {
                filter {
                    eq("id", leaveId)
                }
            }
    }

    suspend fun rejectLeave(
        supabase: SupabaseClient,
        leaveId: String
    ) {
        supabase.from("leave_requests")
            .update(mapOf("status" to "rejected")) {
                filter {
                    eq("id", leaveId)
                }
            }
    }
}
