package com.example.salescalltracker.data.platform

import com.example.salescalltracker.model.EventTimeFilter
import com.example.salescalltracker.model.PlatformEvent

interface EventProvider {

    suspend fun searchEvents(
        city: String,
        query: String = "",
        filter: EventTimeFilter = EventTimeFilter.UPCOMING,
    ): List<PlatformEvent>
}

class LocalEventProvider : EventProvider {

    override suspend fun searchEvents(
        city: String,
        query: String,
        filter: EventTimeFilter,
    ): List<PlatformEvent> {
        return emptyList()
    }
}
