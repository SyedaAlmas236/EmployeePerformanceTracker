package com.example.employeeperformancetracker.data.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userRole: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Admin Sign Up
    fun signUpAdmin(name: String, email: String, password: String, companyName: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signUpAdmin(name, email, password, companyName)
            _authState.value = if (result.isSuccess) {
                AuthState.Success("admin")
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Sign up failed")
            }
        }
    }

    // Sign In
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signInWithEmail(email, password)
            _authState.value = if (result.isSuccess) {
                val profile = result.getOrNull()
                AuthState.Success(profile?.role ?: "unknown")
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Sign in failed")
            }
        }
    }

    // Register Employee Credentials (Admin only)
    fun registerEmployeeCredentials(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.registerEmployeeCredentials(email, password)
            _authState.value = if (result.isSuccess) {
                AuthState.Success("employee_created")
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Employee registration failed")
            }
        }
    }

    // Sign Out
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _authState.value = AuthState.Idle
        }
    }

    // Reset state
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    // Check if logged in
    fun isLoggedIn() = authRepository.isLoggedIn()
}
