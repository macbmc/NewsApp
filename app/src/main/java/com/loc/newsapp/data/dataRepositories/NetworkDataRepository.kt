package com.loc.newsapp.data.dataRepositories

import android.content.Context
import android.util.Log
import com.loc.newsapp.R
import com.loc.newsapp.data.entity.CountryInfo
import com.loc.newsapp.data.entity.LateTopics
import com.loc.newsapp.data.entity.NewsAPIModel
import com.loc.newsapp.data.entity.OnBoardPage
import com.loc.newsapp.domain.repositories.NetworkRepository
import com.loc.newsapp.network.CountryAPIService
import com.loc.newsapp.network.NetworkEndPoints
import com.loc.newsapp.network.NewsApiServices
import com.loc.newsapp.network.NewsAppOnBoardService
import com.loc.newsapp.utils.DataConversion
import com.loc.newsapp.utils.Either
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class NetworkDataRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val countryAPIService: CountryAPIService,
    private val onBoardService: NewsAppOnBoardService,
    private val newsApiServices: NewsApiServices
) : NetworkRepository {
    override suspend fun getOnBoardData(): Either<List<OnBoardPage>?> {
        try {
            Log.d("ONBOARDREPO", "called")
            val response = onBoardService.getOnBoardPage(NetworkEndPoints.ONBOARD_ENDPOINT.url)
            if (response.isSuccessful) {
                Log.d("OnBoardResponse", response.body().toString())
                return Either.Success(response.body())
            }
        } catch (e: Exception) {
            Log.e("onBoardDATAEXC", e.toString())
        }
        return Either.Failed(appContext.getString(R.string.Resource_Fetch_Error))
    }

    override suspend fun getCountryData(): Either<List<CountryInfo>?> {
        try {
            val response =
                countryAPIService.getCountryService(NetworkEndPoints.COUNTRIES_ENDPOINT.url)
            if (response.isSuccessful && response.body() != null) {
                Log.d("RESPONSE_COUNTRY", response.body().toString())

                return Either.Success(DataConversion().countryResponseToCountryData(response.body()!!))
            }
            Log.d("RESPONSE_COUNTRY", response.code().toString())

        } catch (e: Exception) {
            Log.d("RESPONSE_COUNTRY", e.toString())
            when (e) {
                is CancellationException -> throw e
            }


        }
        return Either.Failed("")
    }

    override suspend fun getLatestTopics(): Either<List<LateTopics>?> {
        try {
            Log.d("LatestTopics", "REpoCalled")
            val response = onBoardService.getLatest(NetworkEndPoints.LATEST_ENDPOINT.url)
            if (response.isSuccessful && response.body() != null) {
                Log.d("RESPONSE_COUNTY", response.body().toString())

                return Either.Success(response.body())
            }
            Log.d("RESPONSE_COUNTY", response.code().toString())

        } catch (e: Exception) {
            Log.d("RESPONSE_COUNTY", e.toString())
            when (e) {
                is CancellationException -> throw e
            }


        }
        return Either.Failed("")
    }

    override suspend fun getLatestNewsByCountry(countryCode: String): Either<NewsAPIModel?> {
        try {
            Log.d("COUNTRYNEWS", countryCode)
            val response = newsApiServices.getLatestNewsByCountry(
                NetworkEndPoints.NEWS_ENDPOINT.url,
                countryCode
            )
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("COUNTRYNEWS", response.body().toString())

                    return Either.Success(response.body())
                }
            }
            Log.d("COUNTRYNEWS", response?.code().toString())

        } catch (e: Exception) {
            Log.d("COUNTRYNEWS", e.toString())
            when (e) {
                is CancellationException -> throw e
            }
        }
        return Either.Failed("")
    }

    override suspend fun getNewsByCategory(q: String): Either<NewsAPIModel?> {
        try {
            val response =
                newsApiServices.getLatestNewsCategory(NetworkEndPoints.NEWS_ENDPOINT.url, q)
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("TopicNEWS", response.body().toString())

                    return Either.Success(response.body())
                }
            }
            Log.d("TopicNEWS", response?.code().toString())

        } catch (e: Exception) {
            Log.d("TopicNEWS", e.toString())
            when (e) {
                is CancellationException -> throw e
            }
        }
        return Either.Failed("")
    }

    override suspend fun getNewsBySearch(q: String): Either<NewsAPIModel?> {
        try {
            val response =
                newsApiServices.getLatestNewsBySearch(NetworkEndPoints.NEWS_ENDPOINT.url, q)
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("TopicNEWS", response.body().toString())

                    return Either.Success(response.body())
                }
            }
            Log.d("TopicNEWS", response?.code().toString())

        } catch (e: Exception) {
            Log.d("TopicNEWS", e.toString())
            when (e) {
                is CancellationException -> throw e
            }
        }
        return Either.Failed("")
    }
}

