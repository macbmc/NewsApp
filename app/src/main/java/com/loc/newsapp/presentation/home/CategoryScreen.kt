package com.loc.newsapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.loc.newsapp.presentation.reusables.ShimmerEffect

@Composable
fun CategoryNewsComponents(viewModel: HomeViewModel) {
    val newsList = viewModel.subNewsList.observeAsState()
    if(!newsList.value.isNullOrEmpty())
    {
        LazyColumn {
            items(newsList.value!!) { item ->
               Box(Modifier.padding(5.dp).fillMaxWidth().border(1.dp,Color.LightGray, RoundedCornerShape(20.dp))) {
                   Column(Modifier.padding(5.dp)) {
                       Column(verticalArrangement = Arrangement.SpaceBetween) {
                           Text(
                               text =item.title.toString(),
                           )
                           Spacer(modifier = Modifier.height(5.dp))
                           Text(item.source?.name.toString(), color = Color.LightGray)
                       }
                   }
               }
            }
        }
    }
    else{
        ShimmerEffect(
            modifier = Modifier
                .fillMaxSize()
                .background(shape = RoundedCornerShape(20.dp), color = Color.LightGray)
                .padding(vertical = 20.dp),
        )
    }

    }

