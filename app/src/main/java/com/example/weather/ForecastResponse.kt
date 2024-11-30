package com.example.weather

data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val main: Main,
    val weather: List<Weather>,
    val dt_txt: String
)

