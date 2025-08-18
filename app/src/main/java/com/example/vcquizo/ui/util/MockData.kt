package com.example.vcquizo.ui.util


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
    val quizId: String = "",
    val quizTitle: String = "",
    val category: String = "",
    val score: Int = 0,
    val accuracy: Double = 0.0,
    val timeTakenInSeconds: Int = 0
) {
    // Construtor vazio necessário para o Firestore
    constructor() : this("", "", "", 0, 0.0, 0)
}

data class RankingUser(
    val rank: Int,
    val name: String,
    val score: Int
)

object MockData{



}