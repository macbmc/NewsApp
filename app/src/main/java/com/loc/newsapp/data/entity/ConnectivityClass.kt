package com.loc.newsapp.data.entity

sealed class ConnectivityClass {
    object Connected : ConnectivityClass()
    object NotConnected:ConnectivityClass()

}