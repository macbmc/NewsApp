package com.loc.newsapp.utils

import com.loc.newsapp.data.entity.CountryApiResponse
import com.loc.newsapp.data.entity.CountryInfo

class DataConversion {

    fun countryResponseToCountryData(countryResponse: CountryApiResponse): List<CountryInfo> {
        val countryInfoList = mutableListOf<CountryInfo>()
        countryResponse.data.forEach { countryData ->
            val countryInfo = CountryInfo(countryData.value.country, countryData.key)
            countryInfoList.add(countryInfo)
        }

        return countryInfoList
    }
}