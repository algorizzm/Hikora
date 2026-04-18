package com.hikora.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hikora.data.model.User

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var cachedUser: User? = null

    fun createUser(user: User, onResult: (Boolean) -> Unit) {
        db.collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getCurrentUser(onResult: (User?) -> Unit) {

        // ✅ return cache first
        cachedUser?.let {
            onResult(it)
            return
        }

        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResult(null)
            return
        }

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                val user = doc.toObject(User::class.java)

                cachedUser = user
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun refreshUser(onResult: (User?) -> Unit) {

        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResult(null)
            return
        }

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                val user = doc.toObject(User::class.java)

                cachedUser = user
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updateUserName(newName: String, onResult: (Boolean) -> Unit) {

        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResult(false)
            return
        }

        db.collection("users")
            .document(uid)
            .update("name", newName)
            .addOnSuccessListener {

                // ✅ IMPORTANT FIX: update cache too
                cachedUser = cachedUser?.copy(name = newName)

                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun setCachedUser(user: User) {
        cachedUser = user
    }

    fun isUserCached(): Boolean {
        return cachedUser != null
    }
}