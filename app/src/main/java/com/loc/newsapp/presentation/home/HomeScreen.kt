@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.loc.newsapp.presentation.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Switch
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.loc.newsapp.R
import com.loc.newsapp.data.entity.NewsAPIModel
import com.loc.newsapp.presentation.reusables.ShimmerEffect
import com.loc.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(mViewModel: HomeViewModel) {
    val list = mViewModel.latestTopics.observeAsState()
    val screenHomeState = mViewModel.homeScreenState.observeAsState()
    val newsList = mViewModel.latestNewsList.observeAsState()
    val darkModeState = mViewModel.isDarkMode.observeAsState()
    val drawerState = rememberDrawerState(androidx.compose.material.DrawerValue.Closed)
    NewsAppTheme(darkTheme = darkModeState.value!!) {
        ModalDrawer(
            drawerState =drawerState,
            drawerContent = {
                DrawerComponent(modeCheckState =darkModeState.value!!, onChangeCheckState ={
                    mViewModel.changeUIMode(darkModeState.value!!)
                })
            }, content = {
                Box(modifier = Modifier.background(color = androidx.compose.material3.MaterialTheme.colorScheme.background) )
                {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                            .background(color = androidx.compose.material3.MaterialTheme.colorScheme.background)
                    ) {

                        TopNavRow(mViewModel, drawerState)
                        Spacer(modifier = Modifier.height(10.dp))
                        LocationComponent(mViewModel)
                        Spacer(modifier = Modifier.height(10.dp))
                        if (!list.value.isNullOrEmpty()) {
                            LazyRow() {
                                items(list.value!!) {
                                    SuggestionComponent(str = it.topic, mViewModel)
                                    Spacer(Modifier.width(5.dp))
                                }
                            }
                        }

                        if (screenHomeState.value!!) {
                            if (newsList.value != null) {
                                PageBannerComponent(newsList.value!![0],mViewModel)
                                LazyColumn {
                                    items(newsList.value!!.filter {
                                        it.content != "[Removed]"
                                    }) { message ->
                                        NewsBannerComponent(message, mViewModel)
                                    }
                                }


                            } else {
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 10.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    PageBannerComponent(null,mViewModel)
                                    NewsBannerComponent(null, mViewModel)
                                    NewsBannerComponent(null, mViewModel)
                                    NewsBannerComponent(null, mViewModel)
                                }
                            }

                        } else {
                            CategoryNewsComponents(viewModel = mViewModel)
                        }
                    }
                }



            })
    }
//

}

@Composable
fun SearchTextField(
    value: String,
    onChangeText: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchState: Boolean,
    onActiveChange: (Boolean) -> Unit
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = searchState,
        enter = slideInHorizontally { with(density) { -40.dp.roundToPx() } }) {
        OutlinedTextField(
            value = value,
            onValueChange = { text ->
                onChangeText(text)
            },
            modifier = Modifier.padding(2.dp),
            maxLines = 1,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "clear",
                    modifier = Modifier.clickable {
                        onActiveChange(searchState)
                    }
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "search",
                    modifier = Modifier.clickable {
                        onSearch(value)
                    })
            }

        )
    }

}


@Composable
fun TopNavRow(mViewModel: HomeViewModel, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val homeState = mViewModel.homeScreenState.observeAsState()
    val searchState = remember {
        mutableStateOf(false)
    }
    val isDarkMode by mViewModel.isDarkMode.observeAsState()
    val searchText = remember {
        mutableStateOf("")
    }

    Column {
        Row(

            Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                tint=MaterialTheme.colorScheme.onBackground,
                painter = if (homeState.value!!) painterResource(id =R.drawable.ic_menu_light) else painterResource(
                    id = R.drawable.ic_home
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        scope.launch {
                            if (homeState.value!!) {
                                if (drawerState.isOpen)
                                    drawerState.close()
                                else drawerState.open()
                            } else mViewModel.homeScreenState.postValue(true)


                        }
                    }
            )

            if (searchState.value) SearchTextField(
                value = searchText.value,
                onChangeText = { text ->
                    searchText.value = text
                },
                searchState = searchState.value,
                onSearch = { text ->
                    scope.launch {
                        mViewModel.newsOnTopic(searchText.value)
                        searchState.value = false
                        mViewModel.homeScreenState.postValue(false)
                    }
                },
                onActiveChange = { state ->
                    searchState.value = !state
                })
            else Spacer(modifier = Modifier.fillMaxWidth(0.4f))
            Icon(
                painter = painterResource(R.drawable.ic_search),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        searchState.value = !searchState.value
                    }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationComponent(viewModel: HomeViewModel) {

    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    val countryList by viewModel.countryList.observeAsState()
    val location by viewModel.location.observeAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Row(Modifier.clickable {
            scope.launch {
                viewModel.getCountryList()
                showBottomSheet.value = true
            }
        }) {
            Icon(painter =painterResource(id = R.drawable.ic_location), tint = MaterialTheme.colorScheme.onBackground, contentDescription = null)
            Text(text = location!!, color = MaterialTheme.colorScheme.onBackground)
        }

    }
    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxSize(),
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState
        ) {
            if (countryList != null) {
                LazyColumn {
                    items(countryList!!) { item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clickable {
                                    viewModel.updateLocation(item.country, item.countryCode)
                                    showBottomSheet.value = false
                                }, horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = item.country,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(text = item.countryCode, color = MaterialTheme.colorScheme.onPrimary)
                        }

                    }
                }
            } else CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))

        }
    }
}

@Composable
fun SuggestionComponent(str: String, mViewModel: HomeViewModel) {
    val scope = rememberCoroutineScope()
    Box(
        Modifier
            .background(Color.LightGray, shape = RoundedCornerShape(20.dp))
            .padding(5.dp)
            .clickable {
                scope.launch {
                    mViewModel.newsOnCategory(str)
                    mViewModel.homeScreenState.postValue(false)
                }
            }
    )
    {
        Text(str, color = Color.Black)
    }
}

@Composable
fun NewsBannerComponent(newsModel: NewsAPIModel.Article?, mViewModel: HomeViewModel) {
    if (newsModel != null) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(newsModel.urlToImage)
                .placeholder(R.drawable.ic_network)
                .error(R.drawable.ic_logo)
                .crossfade(true)
                .build()
        )
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        Log.d("Component", newsModel.toString())
                        mViewModel.urLink.postValue(newsModel.url.toString())

                    },
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Image(
                        painter = painter,
                        contentDescription = "BannerImage",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(100.dp)
                    )

                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = newsModel.title.toString(),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(newsModel.source?.name.toString(), color = Color.LightGray)
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    } else {
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(shape = RoundedCornerShape(20.dp), color = Color.LightGray)
                .padding(vertical = 5.dp),
        )
    }
}

@Composable
fun PageBannerComponent(newsModel: NewsAPIModel.Article?,mViewModel: HomeViewModel) {
    val scope = rememberCoroutineScope()
    if (newsModel != null) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(newsModel.urlToImage)
                .placeholder(R.drawable.ic_network)
                .error(R.drawable.ic_network)
                .crossfade(true)
                .build()
        )
        Box(
            modifier = Modifier
                .clickable {
                    scope.launch {
                        mViewModel.urLink.postValue(newsModel.url.toString())
                    }
                }
                .fillMaxWidth()
                .background(shape = RoundedCornerShape(20.dp), color = Color.Transparent)
                .height(200.dp)
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Image(
                painter = painter,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop
            )

            Box(Modifier.shadow(10.dp)) {
                Text(
                    text = newsModel.title.toString(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }


    } else {
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .background(shape = RoundedCornerShape(20.dp), color = Color.LightGray)
                .height(200.dp)
                .padding(vertical = 10.dp),
        )
    }
}

@Composable
fun DrawerComponent(modeCheckState: Boolean, onChangeCheckState: (Boolean) -> Unit) {
    val scope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .padding(10.dp)) {
        Image(painter = painterResource(id = R.drawable.ic_logo),contentDescription = null,
            Modifier
                .height(100.dp)
                .width(100.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                Text(text = "Dark Mode", color = MaterialTheme.colorScheme.onBackground)
                Switch(checked = modeCheckState, onCheckedChange = { scope.launch{
                    onChangeCheckState(modeCheckState)
                } })

        }
    }
}