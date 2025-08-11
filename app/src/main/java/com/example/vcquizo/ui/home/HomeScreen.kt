package com.example.vcquizo.ui.home

import android.R.attr.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vcquizo.ui.components.HistoryCard
import com.example.vcquizo.ui.components.QuizCard
import com.example.vcquizo.ui.theme.VCQuizoTheme
import com.example.vcquizo.ui.util.MockData
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    val tabTitles = listOf("Quizzes", "Histórico")
    val pagerState = rememberPagerState(pageCount = {tabTitles.size})
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).background(
                color = MaterialTheme.colorScheme.background
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){
        Text(
            text = "V.C. Quiz-O",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )

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
                        QuizCard(quiz = quiz)
                    }
                }
                1 -> LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp)
                ){
                    items(MockData.userHistory){ result ->
                        HistoryCard(result = result)

                    }
                }
            }
            
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    VCQuizoTheme {
        HomeScreen()
    }

}

