package com.example.erp_aone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_aone.data.AppRepository
import com.example.erp_aone.data.UserPreferences
import com.example.erp_aone.data.entity.UserEntity
import com.example.erp_aone.util.PasswordUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val loggedInUserId: StateFlow<Long?> = userPreferences.loggedInUserId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signUp(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val existing = repository.getUserByUsername(username)
            if (existing != null) {
                _authState.value = AuthState.Error("Username already exists")
                return@launch
            }
            val hash = PasswordUtils.hashPassword(password)
            val id = repository.insertUser(UserEntity(username = username, passwordHash = hash))
            userPreferences.saveUserId(id)
            _authState.value = AuthState.Success
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = repository.getUserByUsername(username)
            if (user == null) {
                _authState.value = AuthState.Error("Invalid username or password")
                return@launch
            }
            if (PasswordUtils.verifyPassword(password, user.passwordHash)) {
                userPreferences.saveUserId(user.id)
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Invalid username or password")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clear()
            _authState.value = AuthState.Idle
        }
    }

    fun resetData() {
        viewModelScope.launch {
            val userId = loggedInUserId.value
            if (userId != null) {
                repository.resetUserData(userId)
            }
        }
    }
    
    fun clearError() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
