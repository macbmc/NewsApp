package com.loc.newsapp.network

import com.loc.newsapp.data.entity.NewsAPIModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApiServices {

    @GET
    suspend fun getLatestNewsByCountry(
        @Url url: String,
        @Query("country") param: String
    ): Response<NewsAPIModel>?

    @GET
    suspend fun getLatestNewsCategory(@Url url: String, @Query("category") param: String): Response<NewsAPIModel>?

    @GET
    suspend fun getLatestNewsBySearch(@Url url:String,@Query("q") param:String):Response<NewsAPIModel>?
}