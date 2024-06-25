package com.loc.newsapp.utils

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Failed<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}

