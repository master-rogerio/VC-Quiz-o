package com.example.vcquizo.ui.util

import androidx.compose.runtime.mutableStateListOf

data class QuestionUI(
    val text: String = "", // Adicione = ""
    val options: List<String> = emptyList(), // Adicione = emptyList()
    val correctOptionIndex: Int = 0 // Adicione = 0
)

data class QuizUI(
    val title: String = "", // Adicione = ""
    val category: String = "", // Adicione = ""
    val questions: List<QuestionUI> = emptyList(), // Adicione = emptyList()
    val timeLimitMinutes: Int = 0 // Adicione = 0
)

data class QuizResult(
    val quizTitle: String,
    val category: String,
    val score: Int,
    val accuracy: Double,
    val timeTakenInSeconds: Int
)

data class RankingUser(
    val rank: Int,
    val name: String,
    val score: Int
)

object MockData{

    val userHistory = mutableStateListOf<QuizResult>()


}