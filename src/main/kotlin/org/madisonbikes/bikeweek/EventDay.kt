package org.madisonbikes.bikeweek

import java.text.SimpleDateFormat
import java.util.*

/** simple representation of which day an event is held, can be changed to support other months/years in future */
data class EventDay(val dayInSeptember: Int) : Comparable<EventDay> {
    companion object {
        val DAYS: SortedSet<EventDay> by lazy {
            val retval = sortedSetOf<EventDay>()
            for (dayIndex in 12..20) {
                retval += EventDay(dayIndex)
            }
            retval
        }

        val ALL_WEEK = EventDay(0)

        val ALL: List<EventDay> by lazy {
            val retval = DAYS.toMutableList()
            retval += ALL_WEEK
            return@lazy retval
        }
    }

    override fun compareTo(other: EventDay): Int {
        return dayInSeptember.compareTo(other.dayInSeptember)
    }

    fun renderAsString(): String {
        return if (this == ALL_WEEK) {
            "Week-long discounts and activities"
        } else {
            val calendar = Calendar.getInstance()
            calendar.set(2020, 9, dayInSeptember)
            val formatter = SimpleDateFormat("EEEEEEEEEEEE, MMMMMMM d", Locale.US)
            formatter.format(calendar.time)
        }
    }
}