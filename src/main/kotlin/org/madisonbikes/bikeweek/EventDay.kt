package org.madisonbikes.bikeweek

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/** simple representation of which day an event is held, can be changed to support other months/years in future */
data class EventDay(val localDate: LocalDate) : Comparable<EventDay> {
    companion object {
        const val START_DATE = "2020-09-11"
        const val END_DATE = "2020-09-20"

        private val dateParser = DateTimeFormatter.ofPattern("uuuu-MM-dd")

        val DAYS: SortedSet<EventDay> by lazy {
            val start = LocalDate.from(dateParser.parse(START_DATE))
            val end = LocalDate.from(dateParser.parse(END_DATE))

            val retval = sortedSetOf<EventDay>()

            for (i in start.toEpochDay()..end.toEpochDay()) {
                retval += EventDay(LocalDate.ofEpochDay(i))
            }
            retval
        }

        val ALL_WEEK = EventDay(LocalDate.MIN)

        val ALL: List<EventDay> by lazy {
            val retval = mutableListOf(ALL_WEEK)
            retval += DAYS
            return@lazy retval
        }
    }

    override fun compareTo(other: EventDay): Int {
        return localDate.compareTo(other.localDate)
    }

    val headerString by lazy {
        if (this == ALL_WEEK) {
            "Week-long discounts and activities"
        } else {
            val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
            formatter.format(localDate)
        }
    }

    val calendarDate by lazy {
        if (this == ALL_WEEK) {
            "Week-long discounts and activities"
        } else {
            val formatter = DateTimeFormatter.ofPattern("MM-dd", Locale.US)
            formatter.format(localDate)
        }
    }

    val dayOfMonth by lazy {
        if (this == ALL_WEEK) {
            0
        } else {
            localDate.dayOfMonth
        }
    }
}