package com.hikora.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.hikora.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<FirebaseUser?>(null)
    val authState: StateFlow<FirebaseUser?> = _authState

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.observeAuthState().collect { user ->
                _authState.value = user
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        authRepository.login(email, password, onResult)
    }

    // ✅ FIXED: pass name properly
    fun signup(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        authRepository.signup(name, email, password) { success, error ->
            if (success) {
                onResult(true, null)
            } else {
                onResult(false, error)
            }
        }
    }

    fun syncUser(onDone: () -> Unit) {
        authRepository.syncUserFromFirestore(onDone)
    }

    fun logout() {
        authRepository.logout()
    }

    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }

    // ✅ NEW: used by MainActivity
    fun isUserDataReady(): Boolean {
        return authRepository.isUserCached()
    }
}