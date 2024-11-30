package com.example.weather

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

data class Main(val temp: Float, val temp_min: Float, val temp_max: Float)
data class Weather(val description: String, val icon: String)