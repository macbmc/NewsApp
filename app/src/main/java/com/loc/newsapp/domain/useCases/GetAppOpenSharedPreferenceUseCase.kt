package com.loc.newsapp.domain.useCases

import android.content.SharedPreferences

class GetAppOpenSharedPreferenceUseCase(private val sharedPreferences: SharedPreferences) {


    fun execute(): Boolean {
        return sharedPreferences.getBoolean("isAppOpened", false)
    }

    fun setAppOpen() {
        sharedPreferences.edit().putBoolean("isAppOpened", true).apply()
    }


}