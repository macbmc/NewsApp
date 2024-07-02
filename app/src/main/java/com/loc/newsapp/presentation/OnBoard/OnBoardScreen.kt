@file:OptIn(ExperimentalFoundationApi::class)

package com.loc.newsapp.presentation.OnBoard

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.loc.newsapp.presentation.reusables.CustomButton
import com.loc.newsapp.presentation.reusables.OnBoardComponent
import com.loc.newsapp.presentation.reusables.PagerIndicator
import com.loc.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun OnBoardScreen(mViewModel: MainViewModel) {


    val strokeWidth = 5.dp
    val scope = rememberCoroutineScope()
    val boardList by mViewModel.onBoardPage.observeAsState()
    val appOpen by mViewModel.openHomeState.observeAsState()
    val isDarkMode by mViewModel.isDarkMode.observeAsState()
    val pagerState = rememberPagerState(initialPage = 0) {
        if (boardList.isNullOrEmpty()) 0 else boardList!!.size
    }
    val buttonsState = remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> listOf(null, "Next")
                1 -> listOf("Back", "Next")
                boardList!!.size - 1 -> listOf("Back", "Get Started")
                else -> listOf(null, null)
            }
        }
    }

    suspend fun animateScrollToPage(page: Int) {
        pagerState.animateScrollToPage(page = page, animationSpec = tween(durationMillis = 1000))
    }
    NewsAppTheme(darkTheme = isDarkMode!!) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), verticalArrangement = Arrangement.Center) {


            if (!appOpen!!) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()

                        .padding(strokeWidth)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.9f)
                            .fillMaxWidth()
                    )
                    {
                        if (boardList.isNullOrEmpty()) {
                            Text(text = "NULL")
                        } else {
                            HorizontalPager(state = pagerState) { index ->
                                OnBoardComponent(page = boardList!![index])

                            }


                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PagerIndicator(
                            modifier = Modifier.width(60.dp),
                            pagesSize = pagerState.pageCount,
                            selectedPage = pagerState.currentPage
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (!buttonsState.value[0].isNullOrEmpty()) CustomButton(
                                title = buttonsState.value[0].toString(),
                                onClick = {
                                    scope.launch {
                                        animateScrollToPage(pagerState.currentPage - 1)
                                    }

                                }) else null
                            Spacer(modifier = Modifier.width(10.dp))
                            if (!buttonsState.value[1].isNullOrEmpty()) CustomButton(
                                title = buttonsState.value[1].toString(),
                                onClick = {
                                    scope.launch {
                                        if (pagerState.currentPage == boardList!!.size - 1)
                                            mViewModel.setToHomeState()
                                        else animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }) else null


                        }
                    }
                }
            } else CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        }
    }
}


