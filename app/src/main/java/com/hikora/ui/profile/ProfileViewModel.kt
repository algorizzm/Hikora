package com.hikora.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hikora.data.model.User
import com.hikora.data.repository.UserRepository

class ProfileViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> = _updateStatus

    fun loadUser() {
        repository.fetchUser {
            _user.postValue(it)
        }
    }

    fun updateName(newName: String) {
        repository.updateUserName(newName) { success ->
            _updateStatus.postValue(success)

            if (success) {
                // refresh UI after update
                loadUser()
            }
        }
    }
}