package com.loc.newsapp.data.entity

sealed class ConnectivityClass {
    data object Connected : ConnectivityClass()
    data object NotConnected:ConnectivityClass()

}