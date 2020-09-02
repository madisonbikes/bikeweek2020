package org.madisonbikes.bikeweek

import java.util.*

data class Event(
    val name: String,
    val url: String?,
    val description: String,
    val sponsors: List<String>,
    val sponsorUrls: List<String>,
    val location: EventLocation,
    val eventTypes: Set<EventType>,
    val eventDays: SortedSet<EventDay>,
    val eventTimes: SortedSet<EventTime>
) {
    companion object {
        fun fromMapPairs(item: Map<String, String>): Event {
            val name = item.getValue("name").trim()
            val eventUrl = item.getValue("event_url").trim()
            val description = item.getValue("description").trim()
            val sponsors = item.getValue("sponsor")
                .split(",")
                .map { it.trim() }
            val sponsorUrls = item.getValue("sponsor_urls")
                .split(",")
                .map { it.trim() }
            val mapsDescription = item.getValue("maps_description").trim().ifBlank { null }
            val mapsQuery = item.getValue("maps_query").trim().ifBlank { null }
            val mapsPlaceId = item.getValue("maps_placeid").trim().ifBlank { null }
            val locationFreeText = item.getValue("location_free").trim().ifBlank { null }
            val type = item.getValue("type")
                .split(",")
                .map { it.trim() }
                .filterNot { it.isBlank() }
                .map { EventType.valueOf(it.toUpperCase(Locale.US)) }
                .toSet()
            val days = item.getValue("days")
                .split(",")
                .map { it.trim() }
                .filterNot { it == "?" }
                .filter { !it.isBlank() }
                .mapNotNull { dayOfMonth ->
                    val event = EventDay.ALL.firstOrNull {
                        it.dayOfMonth == dayOfMonth.toInt()
                    }
                    if(event == null) {
                        println("Unsupported event day $dayOfMonth")
                    }
                    event
                }
                .toSortedSet()
            val times = item
                .getValue("time").split(",").mapNotNull {
                    EventTime.fromString(it)
                }
                .toSortedSet()

            val eventLocation = EventLocation(
                mapsDescription = mapsDescription,
                mapsQueryBase = mapsQuery,
                mapsPlaceId = mapsPlaceId,
                freeformMarkdownDescription = locationFreeText
            )

            return Event(
                name = name,
                url = eventUrl,
                description = description,
                sponsors = sponsors,
                sponsorUrls = sponsorUrls,
                location = eventLocation,
                eventTypes = type,
                eventDays = days,
                eventTimes = times
            )
        }
    }


    val renderedTime: String?
        get() {
            val times = eventTimes
                .mapNotNull { it.renderedAsRange }

            return if (times.isEmpty()) null
            else times.joinToString()
        }
}