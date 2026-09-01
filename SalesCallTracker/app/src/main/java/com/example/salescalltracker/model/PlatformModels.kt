package com.example.salescalltracker.model

data class PlatformEvent(
    val id: String,
    val title: String,
    val description: String = "",
    val city: String = "",
    val venue: String = "",
    val address: String = "",
    val startTime: Long = 0L,
    val endTime: Long? = null,
    val category: String = "",
    val imageUrl: String? = null,
    val sourceName: String = "",
    val sourceUrl: String? = null,
    val ticketUrl: String? = null,
    val price: String? = null,
    val isLive: Boolean = false,
)

data class PlatformLocation(
    val id: String,
    val name: String,
    val category: String = "",
    val address: String = "",
    val city: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val sourceName: String = "",
    val sourceUrl: String? = null,
)

data class PlatformSearchResult(
    val id: String,
    val title: String,
    val description: String = "",
    val type: String,
    val location: String? = null,
    val sourceName: String? = null,
    val sourceUrl: String? = null,
)

enum class EventTimeFilter {
    LIVE,
    TODAY,
    TOMORROW,
    THIS_WEEKEND,
    UPCOMING,
}

enum class LocationSearchType {
    ALL,
    BUSINESS,
    VENUE,
    EVENT,
    SERVICE,
}
