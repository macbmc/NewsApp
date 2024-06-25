package com.loc.newsapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject

class GetAppOpenSharedPreferenceUseCase (private val sharedPreferences: SharedPreferences) {


    fun execute(): Boolean {
        return sharedPreferences.getBoolean("isAppOpened", false)
    }

    fun setAppOpen() {
        sharedPreferences.edit().putBoolean("isAppOpened", true).apply()
    }


}