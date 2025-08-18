package com.example.vcquizo.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ResultScreen(
    navController: NavController,
    score: Int,
    accuracy: Float,
    timeTaken: String,
    cameFromHistory: Boolean = false,
    quizId: String? = null
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!cameFromHistory) {
            Text("Quiz finalizado!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(32.dp))
            // Cards com os resultados
            Card(modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Sua Pontuação", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "$score",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Acertos")
                            Text("${(accuracy * 100).toInt()}%", fontWeight = FontWeight.SemiBold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Tempo")
                            Text(timeTaken, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Finalizar")
            }

        } else {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Sua Pontuação", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "$score",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Acertos")
                            Text("${(accuracy * 100).toInt()}%", fontWeight = FontWeight.SemiBold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Tempo")
                            Text(timeTaken, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

        Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                // Debug: Verificar o valor do quizId
                android.util.Log.d("ResultScreen", "QuizId recebido: '$quizId'")
                android.util.Log.d("ResultScreen", "QuizId é null? ${quizId == null}")
                android.util.Log.d("ResultScreen", "QuizId é blank? ${quizId?.isBlank()}")
                
                // Navegar para o quiz correspondente
                if (quizId != null && quizId.isNotBlank()) {
                    android.util.Log.d("ResultScreen", "Navegando para quiz: $quizId")
                    navController.navigate("quiz/$quizId"){
                        popUpTo("home")
                    }
                } else {
                    android.util.Log.d("ResultScreen", "QuizId inválido, voltando para home")
                    // Se não há quizId, volta para home
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }) {
                Text("Refazer Quiz")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { navController.popBackStack() }) {
                Text("Voltar ao Histórico")
            }

        }
    }
        }
