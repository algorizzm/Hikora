package com.hikora.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hikora.data.model.User
import com.hikora.data.repository.UserRepository

class HomeViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun loadUser() {
        repository.getCurrentUser { user ->
            _user.postValue(user)
        }
    }
}