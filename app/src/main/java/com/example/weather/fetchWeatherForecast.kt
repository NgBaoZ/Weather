package com.example.weather

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// Gọi API và lấy dữ liệu dự báo thời tiết
fun fetchWeatherForecast(lat: Double, lon: Double, apiKey: String) {
    val url = "https://api.openweathermap.org/data/2.5/forecast?lat=$lat&lon=$lon&appid=$apiKey&units=metric"

    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    val json = JSONObject(responseBody)
                    val list = json.getJSONArray("list") // Dự báo theo từng 3 giờ

                    // Lấy 3 ngày đầu tiên
                    for (i in 0 until 24 step 8) { // Mỗi phần tử trong "list" tương ứng với một thời điểm 3 giờ
                        val day = list.getJSONObject(i) // Lấy dự báo cho mỗi 8h
                        val dt = day.getLong("dt") // Thời gian dự báo
                        val temp = day.getJSONObject("main").getDouble("temp") // Nhiệt độ
                        val description = day.getJSONArray("weather").getJSONObject(0).getString("description") // Mô tả
                        val icon = day.getJSONArray("weather").getJSONObject(0).getString("icon") // Icon thời tiết

                        // Hiển thị dữ liệu
                        println("Ngày: $dt, Temp: $temp°C, Mô tả: $description, Icon: $icon")
                    }
                }
            }
        }
    })
}
