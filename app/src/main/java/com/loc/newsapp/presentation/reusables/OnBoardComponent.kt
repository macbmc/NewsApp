package com.loc.newsapp.presentation.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.loc.newsapp.R
import com.loc.newsapp.data.entity.OnBoardPage

@Composable
fun OnBoardComponent(page: OnBoardPage, modifier: Modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(page.imageURl)
            .placeholder(R.drawable.ic_network)
            .crossfade(true)
            .build()
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight((0.8f)))
        {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }


        Spacer(modifier = Modifier.fillMaxHeight(0.03f))
        Text(

            text = page.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.02f))
        Text(
            modifier= Modifier.padding(horizontal = 5.dp),
            text = page.description,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
        )
    }
}