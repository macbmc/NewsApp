package com.loc.newsapp.utils

import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptors {

    object NewsApiInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val httpUrl = request.url
            val httpUrlWithKey = httpUrl.newBuilder().addQueryParameter("apiKey","ac65611975024f74b219d06a720dec57").build()
            val requestBuilder  = request.newBuilder().url(httpUrlWithKey).build()

            return chain.proceed(requestBuilder)
        }

    }
}