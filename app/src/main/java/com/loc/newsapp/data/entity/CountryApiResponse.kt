package com.loc.newsapp.data.entity


import com.squareup.moshi.Json


data class CountryApiResponse(
    @Json(name = "status") val status: String,
    @Json(name = "status-code") val statusCode: Int,
    @Json(name = "version") val version: String,
    @Json(name = "total") val total: Int,
    @Json(name = "limit") val limit: Int,
    @Json(name = "offset") val offset: Int,
    @Json(name = "access") val access: String,
    @Json(name = "data") val data: Map<String, CountryData>
)

data class CountryData(
    @Json(name = "country") val country: String,
    @Json(name = "region") val region: String
)

data class CountryInfo(
    val country:String,
    val countryCode:String
)