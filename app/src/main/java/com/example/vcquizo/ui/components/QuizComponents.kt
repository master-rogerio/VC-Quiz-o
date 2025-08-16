package com.example.vcquizo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vcquizo.ui.theme.VCQuizoTheme
import com.example.vcquizo.ui.util.MockData
import kotlinx.coroutines.delay

//
//@Composable
//fun AnswerOption(
//    text: String,
//    isSelected: Boolean,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clickable(onClick = onClick),
//
//        colors = CardDefaults.cardColors(
//            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
//            else MaterialTheme.colorScheme.surfaceVariant
//        ),
//
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 4.dp
//        )
//    ) {
//        Text(
//            text = text,
//            modifier = Modifier.padding(16.dp),
//            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
//            else MaterialTheme.colorScheme.onSurface
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AnswerOptionSelectedPreview() {
//    AnswerOption(
//        text = "XML Layouts",
//        isSelected = true,
//        onClick = {}
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AnswerOptionUnselectedPreview() {
//    AnswerOption(
//        text = "Flutter",
//        isSelected = false,
//        onClick = {}
//    )
//}

//@OptIn(ExperimentalMaterial3Api::class) // Necessário para usar TopAppBar
//@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Composable
//fun QuizScreenPreview() {
//    VCQuizoTheme {
//
//        val quiz = quiz
//        var currentQuestionIndex by remember { mutableStateOf(0) }
//        var selectedOption by remember { mutableStateOf<Int?>(null) }
//        var timeRemaining by remember { mutableStateOf(600) }
//
//        LaunchedEffect(Unit) {
//            while (timeRemaining > 0) {
//                delay(1000L) // Espera 1 segundo
//                timeRemaining--
//            }
//        }
//
//        // layout da tela -- scaffold
//        Scaffold(
//            topBar = {
//                // implementação da top bar
//                TopAppBar(
//                    title = {
//                        Text(
//                            text = quiz.title,
//                            modifier = Modifier.fillMaxWidth(),
//                            style = MaterialTheme.typography.titleLarge,
//                            textAlign = TextAlign.Center,
//                            fontWeight = FontWeight.Bold
//                        )
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer
//                    )
//                )
//            }
//        ) { paddingValues ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .padding(16.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // contador de progresso (text)
//                    Text(
//                        text = "Questão ${currentQuestionIndex + 1} / ${quiz.questions.size}",
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                    // contagem regressiva
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//                    )
//                    {
//                        Icon(
//                            imageVector = Icons.Outlined.Timer,
//                            contentDescription = "Tempo Restante",
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                        Text(
//                            text = "%02d:%02d".format(
//                                timeRemaining / 60,
//                                timeRemaining % 60
//                            ),
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//
//
//                    }
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//
//                val progress = (currentQuestionIndex + 1).toFloat() / quiz.questions.size.toFloat()
//
//                // barra de progresso
//                LinearProgressIndicator(
//                    progress = {
//                        progress
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // --- implementar a lógica do quiz aqui ---
//
//                val currentQuestion = quiz.questions[currentQuestionIndex]
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(2f)
//                        .background(
//                            color = MaterialTheme.colorScheme.primary,
//                            shape = RoundedCornerShape(10.dp)
//                        ),
//                    contentAlignment = Alignment.Center
//                ){
//                /// texto da pergunta
//                Text(
//                    text = currentQuestion.text,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    fontWeight = FontWeight.SemiBold,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                }
//                Spacer(modifier = Modifier.height(24.dp))
//
//
//                // respostas -- lista
//                currentQuestion.options.forEachIndexed { index, optionText ->
//                    AnswerOption(
//                        text = optionText,
//                        isSelected = (selectedOption == index),
//                        onClick = { selectedOption = index }
//                    )
//                }
//                Spacer(Modifier.weight(1f))
//
//                // botão para a próxima pergunta
//                Button(
//                    onClick = { /* Lógica para ir para a próxima questão */ },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Próxima Pergunta", fontSize = 16.sp)
//                }
//            }
//        }
//    }
//}

