package com.example.vcquizo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vcquizo.ui.util.QuizResult
import com.example.vcquizo.ui.util.QuizUI
import com.example.vcquizo.ui.util.RankingUser


@Composable
fun QuizCard(quiz: QuizUI) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(24.dp)

    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = quiz.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Outlined.Category,
                    contentDescription = "Categoria",
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                 Row(
                  verticalAlignment = Alignment.CenterVertically
                 ){
                     Icon(
                         imageVector = Icons.Outlined.Info,
                         contentDescription = "Questões",
                         modifier = Modifier.padding(16.dp)
                     )
                     Spacer(modifier = Modifier.width(4.dp))
                     Text(
                         text = "${quiz.questions.size} questões",
                         style = MaterialTheme.typography.bodyMedium,
                         fontWeight = FontWeight.Normal
                     )
                 }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Outlined.Timer,
                            contentDescription = "Tempo",
                            modifier = Modifier.padding(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${quiz.timeLimitMinutes} min",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

            }

            }
        }
    }

}

@Composable
fun HistoryCard(result : QuizResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = result.quizTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = result.category,
                style = MaterialTheme.typography.bodySmall
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.inversePrimary,
                )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Pontuação",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "${result.score}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Acertos",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "${(result.accuracy * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Tempo",
                        style = MaterialTheme.typography.labelSmall
                    )
                    val minutes = result.timeTakenInSeconds / 60
                    val seconds = result.timeTakenInSeconds % 60
                    Text(
                        text = "%02d:%02d".format(minutes, seconds),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}


@Composable
fun RankingItem(user: RankingUser, isCurrentUser: Boolean = false) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) {
                MaterialTheme.colorScheme.primary
            }else{
                MaterialTheme.colorScheme.primaryContainer
            }
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.width(30.dp),
                    contentAlignment = Alignment.Center
                ){
                    when (user.rank){
                        1 -> Icon(Icons.Default.WorkspacePremium,
                            "Ouro", tint = Color(0xFFFFD700)
                            )
                        2 -> Icon(Icons.Default.WorkspacePremium,
                            "Prata", tint = Color(0xFFC0C0C0)
                        )
                        3 -> Icon(Icons.Default.WorkspacePremium,
                            "Bronze", tint = Color(0xFFCD7F32)
                        )
                        else -> Text(
                            text = "${user.rank}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(30.dp)
                        )
                    }
                }




                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }


            Text(
                text = "${user.score} pts",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.primary
            )
        }
    }
}