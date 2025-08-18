package com.example.vcquizo.ui.quiz



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vcquizo.data.UserRepository
import com.example.vcquizo.ui.util.QuizResult
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vcquizo.view.model.QuizViewModel


// Um enum para controlar o estado da resposta (Certo, Errado ou Neutro)
enum class AnswerState { CORRECT, INCORRECT, NEUTRAL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(navController: NavController,
               quizId: String,
               userRepository: UserRepository = UserRepository(LocalContext.current),
               quizViewModel: QuizViewModel = viewModel(),
) {

    val quizzesMap by quizViewModel.quizzesMap.collectAsState()
    val quiz = quizzesMap[quizId] // Busca o quiz específico usando o ID da navegação

    val context = LocalContext.current

    // Debug: Verificar carregamento do quiz
    android.util.Log.d("QuizScreen", "QuizId recebido: '$quizId'")
    android.util.Log.d("QuizScreen", "Quizzes disponíveis: ${quizzesMap.keys}")
    android.util.Log.d("QuizScreen", "Quiz encontrado: ${quiz != null}")

    if (quiz == null) {
        // Mostra uma tela de carregamento ou uma mensagem
        android.util.Log.d("QuizScreen", "Quiz não encontrado, mostrando loading...")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Carregando quiz...")
                if (quizzesMap.isEmpty()) {
                    Text("Carregando lista de quizzes...", style = MaterialTheme.typography.bodySmall)
                } else {
                    Text("Quiz '$quizId' não encontrado", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        return // Sai da função até que o quiz seja encontrado
    }


    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var answerState by remember { mutableStateOf(AnswerState.NEUTRAL) }
    var correctAnswersCount by remember { mutableStateOf(0) }
    var timeRemaining by remember { mutableStateOf(quiz.timeLimitMinutes * 60) } // Tempo em segundos
    val totalTime = quiz.timeLimitMinutes * 60

    // Efeito para o timer regressivo
    LaunchedEffect(key1 = currentQuestionIndex) {
        while (timeRemaining > 0 && answerState == AnswerState.NEUTRAL) {

            delay(1000L)
            timeRemaining--
        }
        if (timeRemaining == 0) {
            // Navega para o resultado quando o tempo acaba
            val accuracy = if (quiz.questions.isNotEmpty()) correctAnswersCount.toFloat() / quiz.questions.size else 0f
            val timeTaken = totalTime - timeRemaining
            val timeString = "Tempo Esgotado!"
            navController.navigate("result/0/$accuracy/$timeString/false")
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = quiz.title,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- Barra de Progresso e Timer ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Acertos: $correctAnswersCount / ${quiz.questions.size}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = "Tempo",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    if (timeRemaining == 0) {
                        Text(color = Color.Red, text = "Tempo Esgotado")
                    } else {
                        Text(text = "%02d:%02d".format(timeRemaining / 60, timeRemaining % 60))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de progresso baseada nos ACERTOS
            val progress = correctAnswersCount.toFloat() / (quiz.questions.size.toFloat())
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Pergunta ---
            val currentQuestion = quiz.questions[currentQuestionIndex]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentQuestion.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- Opções de Resposta ---
            currentQuestion.options.forEachIndexed { index, optionText ->
                val isSelected = selectedOptionIndex == index
                val isCorrectAnswer = index == currentQuestion.correctOptionIndex

                val color = when {
                    // Se esta opção for a correta e uma resposta já foi dada, fica verde.
                    isCorrectAnswer && answerState != AnswerState.NEUTRAL -> Color(0xFF388E3C) // Verde
                    // Se esta opção foi a selecionada e estava errada, fica vermelha.
                    isSelected && answerState == AnswerState.INCORRECT -> Color(0xFFD32F2F) // Vermelho
                    // Caso contrário, cor padrão.
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                val textColor = when {
                    // Se a resposta já foi dada E (esta opção foi a selecionada OU é a correta)
                    answerState != AnswerState.NEUTRAL && (isSelected || isCorrectAnswer) -> {
                        MaterialTheme.colorScheme.onPrimary  // Cor clara para fundos coloridos
                    }
                    // Em todos os outros casos
                    else -> {
                        MaterialTheme.colorScheme.onSurface // Cor padrão para o fundo padrão
                    }
                }

                val textWeight = when {
                    answerState != AnswerState.NEUTRAL && (isSelected || isCorrectAnswer) -> {
                        FontWeight.Bold
                    }

                    else -> {
                        FontWeight.Normal // Cor padrão para o fundo padrão
                    }

                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(
                            // Só permite clicar se nenhuma resposta foi dada ainda
                            enabled = answerState == AnswerState.NEUTRAL
                        ){
                            selectedOptionIndex = index
                            val isCorrect = index == currentQuestion.correctOptionIndex
                            if (isCorrect) {
                                answerState = AnswerState.CORRECT
                                correctAnswersCount++
                            } else {
                                answerState = AnswerState.INCORRECT
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = color
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = optionText,
                        modifier = Modifier.padding(16.dp),
                        color = textColor,
                        fontWeight = textWeight
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            // --- BOTÃO "PRÓXIMA PERGUNTA" (LÓGICA ALTERADA) ---
            Button(
                onClick = {
                    // Lógica para avançar para a próxima questão
                    if (currentQuestionIndex < quiz.questions.size - 1) {
                        currentQuestionIndex++
                        selectedOptionIndex = null
                        answerState = AnswerState.NEUTRAL
                    } else {
                        // Fim do quiz, navega para a tela de resultado
                        val score = correctAnswersCount * 10
                        val accuracy = correctAnswersCount.toFloat() / quiz.questions.size
                        val timeTaken = totalTime - timeRemaining
                        val user = Firebase.auth.currentUser


                        val newResult = QuizResult(
                            quizId = quizId,
                            quizTitle = quiz.title,
                            category = quiz.category,
                            score = score,
                            accuracy = accuracy.toDouble(),
                            timeTakenInSeconds = timeTaken
                        )


//
                        if (user != null){
                            android.util.Log.d("QuizScreen", "Usuário autenticado: ${user.uid}")
                            android.util.Log.d("QuizScreen", "Salvando resultado: ${newResult.quizTitle}, Score: ${newResult.score}")
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val userRepository = UserRepository(context)
                                    userRepository.updateBestScore(user.uid, quizId, score.toLong())
                                    userRepository.saveQuizResult(user.uid, newResult)
                                    android.util.Log.d("QuizScreen", "Resultado salvo com sucesso!")
                                } catch (e: Exception) {
                                    android.util.Log.e("QuizScreen", "Erro ao salvar resultado: ${e.message}")
                                }
                            }
                        } else {
                            android.util.Log.w("QuizScreen", "Usuário não autenticado, não salvando resultado")
                        }

                        val timeString = "%02d:%02d".format(timeTaken / 60, timeTaken % 60)
                        navController.navigate("result/$score/$accuracy/$timeString/false") {
                            popUpTo("home")
                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                // Habilita o botão apenas depois que uma resposta foi selecionada
                enabled = answerState != AnswerState.NEUTRAL,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                // Muda o texto do botão na última questão
                val buttonText = if (currentQuestionIndex < quiz.questions.size - 1) {
                    "Próxima Pergunta"
                } else {
                    "Finalizar Quiz"
                }
                Text(buttonText, fontSize = 16.sp)
            }
        }
    }
}

@Preview
@Composable
private fun QuizScreenPreview() {
    QuizScreen(
        navController = rememberNavController(),
        quizId = "mock"
    )

}