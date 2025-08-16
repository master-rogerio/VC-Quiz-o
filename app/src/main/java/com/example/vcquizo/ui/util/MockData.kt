package com.example.vcquizo.ui.util

import androidx.compose.runtime.mutableStateListOf

data class QuestionUI(
        val text: String,
        val options: List<String>,
        val correctOptionIndex: Int
        )

data class QuizUI(
    val title: String,
    val category: String,
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
    val timeTakenInSeconds: Int
)

data class RankingUser(
    val rank: Int,
    val name: String,
    val score: Int
)

object MockData{

    val techQuiz = QuizUI(
        title = "Fundamentos de Kotlin",
        category = "Tecnologia",
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
            ),
            QuestionUI(
                text = "Qual palavra-chave declara uma variável imutável?",
                options = listOf("var", "val", "const", "let"),
                correctOptionIndex = 1
            ),
            QuestionUI(
                text = "Qual o operador para checagem de nulo seguro?",
                options = listOf("!!", "?.", "?:", "??"),
                correctOptionIndex = 1
            ),
            QuestionUI(
                text = "Qual o nome da função para imprimir algo no console?",
                options = listOf("print()", "log()", "display()", "write()"),
                correctOptionIndex = 0
            )
        )
    )

    val availableQuizzes = listOf(
        QuizInfo("q1","Fundamentos de Kotlin", "Tecnologia", 10, 15),
    )

    val userHistory = mutableStateListOf<QuizResult>()




}