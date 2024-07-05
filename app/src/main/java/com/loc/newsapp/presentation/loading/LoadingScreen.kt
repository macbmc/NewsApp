package com.loc.newsapp.presentation.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.loc.newsapp.presentation.reusables.ShimmerEffect

@Composable
fun LoadingScreen()
{
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp))
    {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.background(Color.Red, RoundedCornerShape(20.dp)).padding(5.dp))
            {
                Text("No Internet Available" , color = Color.White)
            }
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.LightGray)
                    .padding(vertical = 30.dp),
            )
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.LightGray)
                    .padding(vertical = 30.dp),
            )
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.LightGray)
                    .padding(vertical = 30.dp),
            )
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.LightGray)
                    .padding(vertical = 30.dp),
            )
        }
    }
}