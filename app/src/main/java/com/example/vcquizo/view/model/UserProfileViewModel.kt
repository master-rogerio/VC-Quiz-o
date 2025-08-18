package com.example.vcquizo.view.model

import androidx.lifecycle.ViewModel
import com.example.vcquizo.data.UserRepository
import com.example.vcquizo.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user


}