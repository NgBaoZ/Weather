package com.example.weather

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val apiKey = "4c71ec5d7be8e3ba64c2108c9b2428cc" // Thay bằng API key từ OpenWeatherMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ các thành phần giao diện
        val locationTextView = findViewById<TextView>(R.id.locationTextView)
        val updatedAtTextView = findViewById<TextView>(R.id.updatedAtTextView)
        val temperatureTextView = findViewById<TextView>(R.id.temperatureTextView)
        val weatherDescriptionTextView = findViewById<TextView>(R.id.weatherDescriptionTextView)
        val minTempTextView = findViewById<TextView>(R.id.minTempTextView)
        val maxTempTextView = findViewById<TextView>(R.id.maxTempTextView)
        val weatherIconImageView = findViewById<ImageView>(R.id.weatherIconImageView)
        val summaryTextView = findViewById<TextView>(R.id.summaryTextView)

        // CardViews (dự báo các ngày tới)
        val cardDay1TextView = findViewById<TextView>(R.id.cardDay1TextView)
        val cardDay1IconImageView = findViewById<ImageView>(R.id.cardDay1IconImageView)
        val cardDay1MinTempTextView = findViewById<TextView>(R.id.cardDay1MinTempTextView)
        val cardDay1MaxTempTextView = findViewById<TextView>(R.id.cardDay1MaxTempTextView)

        val cardDay2TextView = findViewById<TextView>(R.id.cardDay2TextView)
        val cardDay2IconImageView = findViewById<ImageView>(R.id.cardDay2IconImageView)
        val cardDay2MinTempTextView = findViewById<TextView>(R.id.cardDay2MinTempTextView)
        val cardDay2MaxTempTextView = findViewById<TextView>(R.id.cardDay2MaxTempTextView)

        val cardDay3TextView = findViewById<TextView>(R.id.cardDay3TextView)
        val cardDay3IconImageView = findViewById<ImageView>(R.id.cardDay3IconImageView)
        val cardDay3MinTempTextView = findViewById<TextView>(R.id.cardDay3MinTempTextView)
        val cardDay3MaxTempTextView = findViewById<TextView>(R.id.cardDay3MaxTempTextView)

        // Lấy dữ liệu thời tiết từ OpenWeatherMap
        fetchCurrentWeather("Thu Dau Mot") { response ->
            // Cập nhật dữ liệu thời tiết hiện tại
            val currentDay = getCurrentDay()
            val currentTemp = "${response.main.temp}°C"
            val currentDescription = response.weather[0].description.capitalize()

            locationTextView.text = response.name
            updatedAtTextView.text = "Updated at: ${System.currentTimeMillis() / 1000}"
            temperatureTextView.text = currentTemp
            weatherDescriptionTextView.text = currentDescription
            minTempTextView.text = "Min: ${response.main.temp_min}°C"
            maxTempTextView.text = "Max: ${response.main.temp_max}°C"

            // Hiển thị tổng quát: "Sunday, Sunny 24°C"


            summaryTextView.text = "$currentDay, $currentDescription $currentTemp"

            // Đặt icon thời tiết từ drawable
            val weatherIcon = getWeatherIcon(response.weather[0].icon)
            weatherIconImageView.setImageResource(weatherIcon)
        }

        // (Ví dụ: Dự báo thời tiết các ngày tới, nếu sử dụng gói forecast API)
        fetchForecastWeather("Thu Dau Mot") { forecastResponse ->
            // CardView 1
            cardDay1TextView.text = "Mon"
            cardDay1IconImageView.setImageResource(R.drawable.ic_sunny)
            cardDay1MinTempTextView.text = "24°C"
            cardDay1MaxTempTextView.text = "32°C"


            cardDay2TextView.text = "Tue"
            cardDay2IconImageView.setImageResource(R.drawable.ic_cloudy)
            cardDay2MinTempTextView.text = "22°C"
            cardDay2MaxTempTextView.text = "30°C"


            cardDay3TextView.text = "Wed"
            cardDay3IconImageView.setImageResource(R.drawable.ic_snowy)
            cardDay3MinTempTextView.text = "15°C"
            cardDay3MaxTempTextView.text = "20°C"
        }
    }

    // Hàm lấy ngày hiện tại (thứ trong tuần)
    private fun getCurrentDay(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    // Lấy icon thời tiết từ drawable
    private fun getWeatherIcon(weatherMain: String): Int {
        return when (weatherMain) {
            "Clear" -> R.drawable.ic_sunny
            "Clouds" -> R.drawable.ic_cloudy
            "Rain" -> R.drawable.ic_rainy
            "Snow" -> R.drawable.ic_snowy
            "Thunderstorm" -> R.drawable.ic_rainythunder
            "Drizzle" -> R.drawable.ic_windy
            else -> R.drawable.ic_drop
        }
    }

    private fun fetchCurrentWeather(city: String, callback: (WeatherResponse) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getCurrentWeather(city, apiKey)
                withContext(Dispatchers.Main) {
                    callback(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchForecastWeather(city: String, callback: (ForecastResponse) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getWeatherForecast(city, apiKey)
                withContext(Dispatchers.Main) {
                    callback(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

