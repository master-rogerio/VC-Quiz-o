package com.example.vcquizo.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LightbulbCircle
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.twotone.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vcquizo.data.UserRepository
import com.example.vcquizo.ui.components.HistoryCard
import com.example.vcquizo.ui.components.QuizCard
import com.example.vcquizo.ui.components.RankingItem
import com.example.vcquizo.ui.theme.VCQuizoTheme
import com.example.vcquizo.ui.util.MockData
import com.example.vcquizo.ui.util.RankingUser
import com.example.vcquizo.view.model.AuthViewModel
import com.example.vcquizo.view.model.RankingViewModel
import kotlinx.coroutines.launch




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    rankingViewModel: RankingViewModel = viewModel(),
    userRepository: UserRepository
) {
    val tabTitles = listOf("Quizzes", "Histórico", "Ranking")
    val pagerState = rememberPagerState(pageCount = {tabTitles.size})
    val coroutineScope = rememberCoroutineScope()
    val rankingList by rankingViewModel.rankingList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp).background(
                color = MaterialTheme.colorScheme.background
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
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
                text = { Text(text = title)}
            )

        }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){ page ->
            when (page){
                0 -> LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp)
                )
                {
                    items(MockData.availableQuizzes){ quiz ->
                        Box(modifier = Modifier.clickable {
                            navController.navigate("quiz/${quiz.id}")
                        }) {
                            QuizCard(quiz = quiz)
                        }
                    }
                }
                1 -> LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp)

                ){
                    items(MockData.userHistory){ result ->
                        Box(modifier = Modifier.clickable {
                            // Navega para o resultado, passando dados de exemplo
                            val accuracy = result.accuracy
                            val score = result.score
                            val time = "%02d:%02d".format(result.timeTakeMinutes, 0)
                            navController.navigate("result/$score/$accuracy/$time/true")
                        }) {
                            HistoryCard(result = result)
                        }
                    }
                }
                2 -> LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp)
                ){items(rankingList.size) { index ->
                    val user = rankingList[index]
                    val displayName = if(!user.name.isNullOrBlank()) {
                        user.name
                    }else{
                        user.email.substringBefore('@').replaceFirstChar {
                            it.uppercase()
                        }
                    }

                    RankingItem(user = RankingUser(
                        rank = index + 1,
                        name = displayName,
                        score = user.score.toInt()
                    )
                    )
                }

                }

            }


        }

    }
}


//@Preview
//@Composable
//fun HomeScreenPreview() {
//    VCQuizoTheme {
//        HomeScreen(
//            modifier = Modifier,
//            navController = NavController(LocalContext.current),
//            authViewModel = AuthViewModel()
//        )
//    }
//
//}