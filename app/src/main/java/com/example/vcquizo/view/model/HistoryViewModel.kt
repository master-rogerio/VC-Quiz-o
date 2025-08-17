package com.example.vcquizo.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vcquizo.data.UserRepository
import com.example.vcquizo.ui.util.QuizResult
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)

    private val _historyList = MutableStateFlow<List<QuizResult>>(emptyList())
    val historyList: StateFlow<List<QuizResult>> = _historyList.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory(){
        val user = Firebase.auth.currentUser
        if(user != null) {
            viewModelScope.launch {
                try {
                    val history = userRepository.getQuizHistory(user.uid)
                    _historyList.value = history
                    android.util.Log.d("HistoryViewModel", "Histórico carregado: ${history.size} itens")
                } catch (e: Exception) {
                    android.util.Log.e("HistoryViewModel", "Erro ao carregar histórico: ${e.message}")
                    _historyList.value = emptyList()
                }
            }
        } else {
            android.util.Log.w("HistoryViewModel", "Usuário não autenticado")
            _historyList.value = emptyList()
        }
    }
}