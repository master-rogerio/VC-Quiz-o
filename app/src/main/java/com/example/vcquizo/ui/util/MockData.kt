package com.example.vcquizo.ui.util

import androidx.compose.runtime.mutableStateListOf

data class QuestionUI(
        val text: String,
        val options: List<String>,
        val correctOptionIndex: Int
        )

data class QuizUI(
    val title: String,
    val questions: List<QuestionUI>,
    val timeLimitMinutes: Int
)

data class QuizInfo(
    val id: String,
    val title: String,
    val category: String,
    val questionCount: Int,
    val timeLimitMinutes: Int
)

data class QuizResult(
    val quizTitle: String,
    val category: String,
    val score: Int,
    val accuracy: Double,
    val timeTakeMinutes: Int
)

object MockData{

    val techQuiz = QuizUI(
        title = "Tecnologia",
        timeLimitMinutes = 10,
        questions = listOf(
            QuestionUI(
                text = "Qual framework o Jetpack Compose foi feito para substituir?",
                options = listOf("XML Layouts", "Flutter", "React Native", "Ionic"),
                correctOptionIndex = 0
            ),
            QuestionUI(
                text = "Qual linguagem de programação é a oficial para o desenvolvimento Android?",
                options = listOf("Kotlin", "Java", "Dart", "Swift"),
                correctOptionIndex = 0
            ),
            QuestionUI(
                text = "O que significa a sigla 'API'?",
                options = listOf(
                    "Application Programming Interface",
                    "Advanced Program Interaction",
                    "Application Protocol Interface",
                    "Automated Programming Input"
                ),
                correctOptionIndex = 0
            )
        )
    )

    val availableQuizzes = listOf(
        QuizInfo("q1","Fundamentos de Kotlin", "Tecnologia", 10, 15),
        QuizInfo("q2", "História do Brasil", "História", 15, 20),
        QuizInfo("q3", "Universo Marvel", "Cinema", 20, 10),
        QuizInfo("q4", "Biologia Celular", "Ciência", 12, 15),
        QuizInfo("q5", "Matemática Básica", "Matemática", 8, 10),
        QuizInfo("q6", "Arte Contemporânea", "Arte", 18, 25),
        QuizInfo("q7", "Física Quantica", "Ciência", 14, 18),
        QuizInfo("q8", "Geografia Geodésica", "Geografia", 16, 22),
        QuizInfo("q9", "Literatura Clássica", "Literatura", 10, 12)
    )

    val userHistory = mutableStateListOf(
        QuizResult("Fundamentos de Kotlin", "Tecnologia", 80, 0.8, 12),
        QuizResult("História do Brasil", "História", 120, 0.8, 18),
        QuizResult("Universo Marvel", "Cinema", 90, 0.9, 8)
    )



}