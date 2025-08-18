package com.example.vcquizo.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vcquizo.ui.components.HistoryCard
import com.example.vcquizo.ui.components.QuizCard
import com.example.vcquizo.ui.components.RankingItem
import com.example.vcquizo.ui.util.RankingUser
import com.example.vcquizo.view.model.AuthViewModel
import com.example.vcquizo.view.model.RankingViewModel
import kotlinx.coroutines.launch
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vcquizo.view.model.HistoryViewModel
import com.example.vcquizo.view.model.QuizViewModel // <-- 1. IMPORTE O NOVO VIEWMODEL
import com.google.firebase.Firebase
import com.google.firebase.auth.auth



@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    rankingViewModel: RankingViewModel = viewModel(),
    quizViewModel: QuizViewModel = viewModel(),
    historyViewModel: HistoryViewModel = viewModel()// <-- 2. INSTANCIE O QUIZVIEWMODEL
) {
    val tabTitles = listOf("Quizzes", "Histórico", "Ranking")
    val pagerState = rememberPagerState(pageCount = {tabTitles.size})
    val coroutineScope = rememberCoroutineScope()

    val quizzesMap by quizViewModel.quizzesMap.collectAsState() // <-- 3. OBSERVE O MAPA DE QUIZZES

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route




    LaunchedEffect(currentRoute == "home") {
        rankingViewModel.refreshRanking()
        historyViewModel.loadHistory()
    }
    
    // Recarrega o histórico sempre que voltar para a tela home
    LaunchedEffect(Unit) {
        historyViewModel.loadHistory()
    }

    val historyList by historyViewModel.historyList.collectAsState()
    val rankingList by rankingViewModel.rankingList.collectAsState()
    val currentUserUid = Firebase.auth.currentUser?.uid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp).background(
                color = MaterialTheme.colorScheme.background
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        // Cabeçalho com ícone de logout à Direita e título centralizado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            contentAlignment = Alignment.Center,

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    imageVector = Icons.Rounded.Lightbulb,
                    contentDescription = "Logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = modifier.size(32.dp).align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "V.C. Quiz-O",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                )
            }

            IconButton(
                onClick = {
                    authViewModel.signout()
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Sair",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(
            selectedTabIndex = pagerState.currentPage
        ){ tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = { Text(
                    text = title,
                    fontWeight = if (pagerState.currentPage == index) FontWeight.Bold
                    else FontWeight.Normal
                )}
            )

        }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Top
        ){ page ->
            when (page){
                0 -> LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.Top
                )
                {
                    items(quizzesMap.entries.toList()){ (quizId, quiz) ->
                        Box(modifier = Modifier.clickable {
                            navController.navigate("quiz/$quizId") // Navega com o ID (ex: "q1")
                        }) {
                            QuizCard(quiz = quiz)
                        }
                    }
                }
                1 -> if(historyList.isEmpty()){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            "Seu histórico de quizzes aparecerá aqui.",
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.Top

                ){
                    items(historyList){ result ->
                        Box(modifier = Modifier.clickable {
                            // Navega para o resultado, passando dados de exemplo
                            val accuracy = result.accuracy
                            val score = result.score
                            val minutes =  result.timeTakenInSeconds / 60
                            val seconds = result.timeTakenInSeconds % 60
                            val time = "%02d:%02d".format(minutes, seconds)
                            
                            // Debug: Verificar dados do resultado
                            android.util.Log.d("HomeScreen", "Clicando no histórico:")
                            android.util.Log.d("HomeScreen", "QuizId: '${result.quizId}'")
                            android.util.Log.d("HomeScreen", "QuizTitle: '${result.quizTitle}'")
                            android.util.Log.d("HomeScreen", "Score: $score")
                            
                            val navigationRoute = "result/$score/$accuracy/$time/true?quizId=${result.quizId}"
                            android.util.Log.d("HomeScreen", "Navegando para: $navigationRoute")
                            
                            navController.navigate(navigationRoute)
                        }) {
                            HistoryCard(result = result)
                        }
                    }
                }
                }
                2 -> if(rankingList.isEmpty()){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("O ranking está vazio. Jogue um quiz para aparecer aqui!",
                            textAlign = TextAlign.Center
                        )
                    }

                } else {
                    LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.Top,
                )
                { item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Text(
                                text = "Pos.",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.width(46.dp)
                            )
                            Text(
                                text = "Usuário",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Text(
                            text = "Pontuação",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                    items(
                        rankingList.size
                    ) { index ->
                        val user = rankingList[index]
                        val displayName = if (!user.name.isNullOrBlank()) {
                            user.name
                        } else {
                            user.email.substringBefore('@').replaceFirstChar {
                                it.uppercase()
                            }
                        }

                        val isCurrentUser = (user.uid == currentUserUid)

                        RankingItem(
                            user = RankingUser(
                                rank = index + 1,
                                name = displayName,
                                score = user.score.toInt(),
                            ),
                            isCurrentUser = isCurrentUser
                        )
                    }
                }

                }

            }


        }

    }
}

