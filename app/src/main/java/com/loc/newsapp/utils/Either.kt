package com.loc.newsapp.utils

sealed class Either<T> {

    class Success<T>(val data:T):Either<T>()
    class Failed<T>(val msg:String):Either<T>()
}