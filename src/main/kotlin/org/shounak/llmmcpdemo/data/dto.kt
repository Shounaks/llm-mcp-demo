package org.shounak.llmmcpdemo.data

data class OpenWeatherResponse(
    val name: String? = null,
    val main: Main? = null,
    val weather: List<Weather>? = null,
    val wind: Wind? = null
)

data class Main(val temp: Double? = null, val feels_like: Double? = null, val humidity: Int? = null)
data class Weather(val description: String? = null)
data class Wind(val speed: Double? = null)
