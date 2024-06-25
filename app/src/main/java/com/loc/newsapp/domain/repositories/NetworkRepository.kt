package com.loc.newsapp.domain.repositories

import com.loc.newsapp.data.entity.CountryInfo
import com.loc.newsapp.data.entity.LateTopics
import com.loc.newsapp.data.entity.NewsAPIModel
import com.loc.newsapp.data.entity.OnBoardPage
import com.loc.newsapp.utils.Either

interface NetworkRepository {

    suspend fun getOnBoardData(): Either<List<OnBoardPage>?>

    suspend fun getCountryData():Either<List<CountryInfo>?>
    suspend fun getLatestTopics():Either<List<LateTopics>?>
    suspend fun getLatestNewsByCountry(countryCode:String):Either<NewsAPIModel?>

    suspend fun getNewsByCategory(q:String):Either<NewsAPIModel?>
    suspend fun getNewsBySearch(q: String): Either<NewsAPIModel?>
}