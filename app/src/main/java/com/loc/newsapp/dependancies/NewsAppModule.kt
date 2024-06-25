package com.loc.newsapp.dependancies

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsAppModule {

    @Provides
    @Singleton
    fun provideSharePreference(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("APP_OPENED", 0)
    }
}