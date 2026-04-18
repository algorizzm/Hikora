package com.hikora.data.repository

import com.hikora.data.model.User
import com.hikora.data.auth.AuthManager

class AuthRepository(
    private val authManager: AuthManager = AuthManager()
) {

    private val userRepository = UserRepository()

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        authManager.login(email, password, onResult)
    }

    fun signup(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        authManager.signup(email, password) { success, firebaseUser, error ->

            if (!success || firebaseUser == null) {
                onResult(false, error)
                return@signup
            }

            val user = User(
                id = firebaseUser.uid,
                name = name,
                email = email
            )

            // 🔥 WAIT for Firestore write
            userRepository.createUser(user) { created ->

                if (created) {

                    // 🔥 CRITICAL: sync cache immediately
                    userRepository.setCachedUser(user)

                    onResult(true, null)

                } else {
                    onResult(false, "Failed to save user data")
                }
            }
        }
    }

    fun logout() {
        authManager.logout()
    }

    fun isLoggedIn(): Boolean {
        return authManager.isLoggedIn()
    }

    fun isUserCached(): Boolean {
        return userRepository.isUserCached()
    }

    fun getCurrentUserId(): String? {
        return authManager.getCurrentUser()?.uid
    }

    fun syncUserFromFirestore(onDone: () -> Unit) {
        val uid = getCurrentUserId() ?: return onDone()

        userRepository.refreshUser { user ->
            if (user != null) {
                userRepository.setCachedUser(user)
            }
            onDone()
        }
    }

    fun observeAuthState() = authManager.observeAuthState()
}