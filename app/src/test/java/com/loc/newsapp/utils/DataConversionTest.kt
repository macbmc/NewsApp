package com.loc.newsapp.utils

import com.loc.newsapp.data.entity.CountryApiResponse
import com.loc.newsapp.data.entity.CountryData
import com.loc.newsapp.data.entity.CountryInfo
import org.junit.Assert.assertEquals
import org.junit.Test


class DataConversionTest {
    @Test
    fun getCorrectDataConversionTest() {
        val data = CountryApiResponse(
            "OK", 200, "1.0", 249, limit = 100, offset = 0, access = "public",
            mapOf("IN" to CountryData("India", "Asia"), "US" to CountryData("United States", "Asia"))
        )
        val result = DataConversion().countryResponseToCountryData(data)
        val expResult = listOf(CountryInfo("India", "IN"), CountryInfo("United States", "US"))
        assertEquals(expResult, result)
    }
}