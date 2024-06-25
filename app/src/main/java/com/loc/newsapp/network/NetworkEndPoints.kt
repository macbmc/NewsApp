package com.loc.newsapp.network

enum class NetworkEndPoints(val url:String) {
    BASE_URL_GIT("https://raw.githubusercontent.com/"),
    ONBOARD_ENDPOINT("/mac-bmc/mac-bmc.github.io/main/onBoardingData.json"),
    BASE_URL_COUNTRIES("https://api.first.org/"),
    COUNTRIES_ENDPOINT("data/v1/countries"),
    LATEST_ENDPOINT("/mac-bmc/mac-bmc.github.io/main/Topics.json"),
    BASE_URL_NEWS("https://newsapi.org/"),
    NEWS_ENDPOINT("/v2/top-headlines")


}