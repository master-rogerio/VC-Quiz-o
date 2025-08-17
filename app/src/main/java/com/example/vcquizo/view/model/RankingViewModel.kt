package com.example.vcquizo.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vcquizo.data.UserRepository
import com.example.vcquizo.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RankingViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)

    private val _rankingList = MutableStateFlow<List<User>>(emptyList())
    val rankingList: StateFlow<List<User>> = _rankingList.asStateFlow()

    init {
        fetchRanking()
    }
    fun refreshRanking(){
        fetchRanking()
    }

    private fun fetchRanking() {
        viewModelScope.launch{
            val users = userRepository.getRanking()
            _rankingList.value = users
        }

    }

}