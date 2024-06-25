package com.loc.newsapp.data.entity

import com.squareup.moshi.Json

data class OnBoardPage(
    val title:String,
    val description:String,
    val imageURl:String
)

data class LateTopics(
    @Json(name = "topic")val topic:String
)