package com.loc.newsapp.presentation.home

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.data.entity.CountryInfo
import com.loc.newsapp.data.entity.LateTopics
import com.loc.newsapp.data.entity.NewsAPIModel
import com.loc.newsapp.domain.repositories.NetworkRepository
import com.loc.newsapp.domain.useCases.GetLatestNewsUseCase
import com.loc.newsapp.domain.useCases.GetLatestTopicUseCase
import com.loc.newsapp.utils.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class HomeViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val countryList = MutableLiveData<List<CountryInfo>>(null)
    private val latestTopicUseCase = GetLatestTopicUseCase(networkRepository)
    private val latestNewsUseCase = GetLatestNewsUseCase(networkRepository,sharedPreferences)

    val urLink = MutableLiveData<String>(null)

    val location = MutableLiveData("")
    val homeScreenState = MutableLiveData(true)

    val latestTopics = MutableLiveData<List<LateTopics>>(null)
    val latestNewsList = MutableLiveData<List<NewsAPIModel.Article>>(null)
    val subNewsList = MutableLiveData<List<NewsAPIModel.Article>>(null)


    fun initHome() {
        getLatestTopics()
        getLocation()
        getLatestNews()
    }

    fun getCountryList() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = networkRepository.getCountryData()) {
                is Either.Success -> {
                    if (response.data != null)
                        countryList.postValue(response.data!!)

                }

                else -> {}
            }
        }
    }

    private fun getLocation() {
        val country = sharedPreferences.getString("DEVICE_LOCATION_COUNTRY", "")
        Log.d("CURRENTLOCATIONCCDD", country.toString())
        if (country?.length!! > 0)
            location.postValue(country)
        else
            location.postValue("Add location")


    }

    fun updateLocation(country: String, countryCode: String) {
        sharedPreferences.edit()
            .putString("DEVICE_LOCATION_COUNTRY", country)
            .putString("DEVICE_LOCATION_COUNTRY_CODE", countryCode)
            .apply()
        location.postValue(country)
        getLatestNews()
    }

    private fun getLatestTopics() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("LatestTopics", "Called")
            latestTopicUseCase.execute().collect {
                if (!it.isNullOrEmpty()) {
                    latestTopics.postValue(it)
                    Log.d("LatestTopics", it.toString())
                }

            }
        }
    }
    private fun getLatestNews(){
        viewModelScope.launch(Dispatchers.IO) {
            latestNewsUseCase.execute().collect{
                latestNewsList.postValue(it)
            }
        }
    }

    fun newsOnCategory(q:String) {
       viewModelScope.launch(Dispatchers.IO) {
           latestNewsUseCase.newsByCategory(q).collect{
              subNewsList.postValue(it)
           }
       }
    }
    fun newsOnTopic(q:String) {
        viewModelScope.launch(Dispatchers.IO) {
            latestNewsUseCase.newsByTopic(q).collect{
                subNewsList.postValue(it)
            }
        }
    }


}