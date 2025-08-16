package com.example.vcquizo.view.model


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vcquizo.ui.util.QuizUI
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()

    // Este StateFlow guardará um mapa de IDs (q1, q2) para os objetos QuizUI
    private val _quizzesMap = MutableStateFlow<Map<String, QuizUI>>(emptyMap())
    val quizzesMap: StateFlow<Map<String, QuizUI>> = _quizzesMap.asStateFlow()

    init {
        // Busca os dados assim que o ViewModel for criado.
        getDataFromFirebase()
    }

    private fun getDataFromFirebase() {
        viewModelScope.launch {
            database.getReference("quizzes")
                .get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val quizMap = mutableMapOf<String, QuizUI>()
                        for (snapshot in dataSnapshot.children) {
                            val quiz = snapshot.getValue(QuizUI::class.java)
                            // A chave do snapshot (ex: "q1") é o nosso ID
                            if (quiz != null && snapshot.key != null) {
                                quizMap[snapshot.key!!] = quiz
                            }
                        }
                        _quizzesMap.value = quizMap
                    }
                }
                .addOnFailureListener {
                    Log.e("Firebase", "Erro ao buscar quizzes", it)
                }
        }
    }
}