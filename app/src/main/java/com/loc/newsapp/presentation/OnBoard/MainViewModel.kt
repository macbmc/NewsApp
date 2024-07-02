package com.loc.newsapp.presentation.OnBoard

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.data.entity.CountryInfo
import com.loc.newsapp.data.entity.OnBoardPage
import com.loc.newsapp.domain.repositories.NetworkRepository
import com.loc.newsapp.domain.useCases.GetAppOpenSharedPreferenceUseCase
import com.loc.newsapp.domain.useCases.NetworkOnBoardPageUseCase
import com.loc.newsapp.utils.Either
import com.loc.newsapp.utils.StoreLocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext private val appContext: Context, private val sharedPreferences: SharedPreferences, private val networkRepository: NetworkRepository) :
    ViewModel() {


    val onBoardPage = MutableLiveData<List<OnBoardPage>>()
    val setLocation = MutableLiveData<Boolean>(false)


    private val onBoardUseCase = NetworkOnBoardPageUseCase(networkRepository)
    private  val appOpenUseCase= GetAppOpenSharedPreferenceUseCase(sharedPreferences)
    private  val storeLocationUtils= StoreLocationUtils(appContext,sharedPreferences)
    val isDarkMode = MutableLiveData(sharedPreferences.getBoolean("APP_DARK_MODE", false))
    val isAppReady = MutableLiveData(false)
    val openHomeState = MutableLiveData(false)

    fun location()
    {
        viewModelScope.launch {
            getLocation()
        }
    }

   private suspend fun getLocation()
   {

           Log.d("RESPONSE_COUNTRY", "data")
           storeLocationUtils.execute().collect{
               setLocation.postValue(it)
               Log.d("LOCATIONSTATUS",it.toString())
           }


   }

    fun getOnBoardData() {
        when (appOpenUseCase.execute()) {
            true -> {
                openHomeState.postValue(true)
                isAppReady.postValue(true)

            }

            false -> {
                appOpenUseCase.setAppOpen()
                viewModelScope.launch(Dispatchers.IO) {
                    onBoardUseCase.execute().collect { onBoardData ->
                        when (onBoardData) {
                            null -> isAppReady.postValue(false)
                            else -> {
                                onBoardPage.postValue(onBoardData!!)
                                isAppReady.postValue(true)
                            }
                        }
                    }
                }
            }
        }
    }

    fun setToHomeState()
    {
        Log.e("HOMESTATE","HOME")
        openHomeState.postValue(true)
    }
}