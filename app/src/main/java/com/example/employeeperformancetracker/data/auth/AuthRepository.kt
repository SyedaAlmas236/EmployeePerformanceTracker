package com.example.employeeperformancetracker.data.auth

import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import com.example.employeeperformancetracker.data.SupabaseConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class UserProfile(
    val id: String,
    val email: String,
    val role: String
)

class AuthRepository {
    private val supabase = SupabaseConfig.client

    // Sign up admin
    suspend fun signUpAdmin(name: String, email: String, password: String, companyName: String): Result<Unit> {
        return try {
            // Create auth user
            val user = supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("name", name)
                    put("company_name", companyName)
                    put("role", "admin")
                }
            }

            // Create profile with role='admin'
            if (user != null) {
                supabase.from("profiles").upsert(
                    buildJsonObject {
                        put("id", user.id)
                        put("email", email)
                        put("role", "admin")
                    }
                )
                Result.success(Unit)
            } else {
                Result.failure(Exception("Signup successful but user data not received. Please check if email confirmation is required."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign in with email and password
    suspend fun signInWithEmail(email: String, password: String): Result<UserProfile> {
        return try {
            // Sign in
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            // Get user role from profiles
            val userId = supabase.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("User not found"))

            val profile = supabase.from("profiles")
                .select(columns = Columns.raw("id,email,role")) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<UserProfile>()

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Register employee credentials (admin only)
    suspend fun registerEmployeeCredentials(email: String, password: String): Result<Unit> {
        return try {
            // Create auth user for employee
            val user = supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("role", "employee")
                }
            }

            // Create profile with role='employee'
            if (user != null) {
                supabase.from("profiles").upsert(
                    buildJsonObject {
                        put("id", user.id)
                        put("email", email)
                        put("role", "employee")
                    }
                )
                Result.success(Unit)
            } else {
                Result.failure(Exception("Employee credentials created but user data not received. Please check if email confirmation is required."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign out
    suspend fun signOut(): Result<Unit> {
        return try {
            supabase.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get current user
    fun getCurrentUser() = supabase.auth.currentUserOrNull()

    // Check if user is logged in
    fun isLoggedIn() = supabase.auth.currentUserOrNull() != null
}
