package com.loc.newsapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import com.loc.newsapp.data.entity.NewsAPIModel
import com.loc.newsapp.domain.repositories.NetworkRepository
import com.loc.newsapp.utils.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLatestNewsUseCase(
    private val networkRepository: NetworkRepository,
    private val sharedPreference: SharedPreferences
) {
    suspend fun execute(): Flow<List<NewsAPIModel.Article>?> {
        val countryCode = sharedPreference.getString("DEVICE_LOCATION_COUNTRY_CODE", "us")
        return flow {
            when (val response = networkRepository.getLatestNewsByCountry(countryCode.toString())) {
                is Either.Success -> {
                    emit(response.data?.articles as List<NewsAPIModel.Article>)
                }

                else -> emit(null)
            }
        }
    }

    suspend fun newsByCategory(q: String): Flow<List<NewsAPIModel.Article>?> {
        return flow {
            when (val response = networkRepository.getNewsByCategory(q)) {
                is Either.Success -> {
                    emit(response.data?.articles as List<NewsAPIModel.Article>)
                }

                else -> emit(null)
            }
        }
    }

    suspend fun newsByTopic(q: String): Flow<List<NewsAPIModel.Article>?> {
        return flow {
            when (val response = networkRepository.getNewsBySearch(q)) {
                is Either.Success -> {
                    emit(response.data?.articles as List<NewsAPIModel.Article>)
                }

                else -> {
                    Log.d("LatestNewsNull",response.toString())
                    emit(null)
                }
            }
        }
    }
}