package com.loc.newsapp.network

import com.loc.newsapp.data.entity.LateTopics
import com.loc.newsapp.data.entity.OnBoardPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsAppOnBoardService {

    @GET
    suspend fun getOnBoardPage(@Url url :String) : Response<List<OnBoardPage>?>

    @GET
    suspend fun getLatest(@Url url:String) : Response<List<LateTopics>?>
}