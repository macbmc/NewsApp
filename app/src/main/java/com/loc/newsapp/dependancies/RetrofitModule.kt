package com.loc.newsapp.dependancies

import com.loc.newsapp.data.dataRepositories.NetworkDataRepository
import com.loc.newsapp.domain.repositories.NetworkRepository
import com.loc.newsapp.network.CountryAPIService
import com.loc.newsapp.network.NewsAppOnBoardService
import com.loc.newsapp.network.NetworkEndPoints
import com.loc.newsapp.network.NewsApiServices
import com.loc.newsapp.utils.RetrofitInterceptors
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Provides
    @Singleton
    fun provideOnBoardingRetrofit(): NewsAppOnBoardService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(NetworkEndPoints.BASE_URL_GIT.url)
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(NewsAppOnBoardService::class.java)
    }

    @Provides
    fun provideNetworkRepo(implementation: NetworkDataRepository): NetworkRepository {
        return implementation
    }

    @Provides
    @Singleton
    fun provideCountriesRetrofit(): CountryAPIService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(NetworkEndPoints.BASE_URL_COUNTRIES.url)
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(CountryAPIService::class.java)
    }
    @Provides
    @Singleton
    fun provideNewsRetrofit(): NewsApiServices{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(RetrofitInterceptors.NewsApiInterceptor)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(NetworkEndPoints.BASE_URL_NEWS.url)
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(NewsApiServices::class.java)
    }
}

