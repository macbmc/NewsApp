package com.loc.newsapp.network

import com.loc.newsapp.data.entity.CountryApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CountryAPIService {

    @GET
    suspend fun getCountryService(@Url url:String):Response<CountryApiResponse>
}