package org.madisonbikes.bikeweek

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.FormatStyle
import java.util.*

data class EventTime(val start: String, val end: String?) : Comparable<EventTime> {
    companion object {
        fun fromString(timeString: String): EventTime? {
            val ts = timeString.ifBlank { return null }

            val parsed = ts.split("-")
            require(parsed.size == 1 || parsed.size == 2) {
                "Time format not recognized: $ts"
            }

            return if (parsed.size == 1) {
                EventTime(parsed[0], null)
            } else {
                EventTime(parsed[0], parsed[1])
            }
        }
    }

    override fun compareTo(other: EventTime): Int {
        val left = startTimeAsLocalTime ?: LocalTime.MIN
        val right = other.startTimeAsLocalTime ?: LocalTime.MIN

        return left.compareTo(right)
    }


    val startTimeAsLocalTime: LocalTime? by lazy {
        val startString = start.ifEmpty { null }
        startString?.let {
            parseTime(it)
        }
    }

    val renderedAsRange: String?
        get() {
            val startString = start.ifEmpty { null }
            val endString = end?.ifEmpty { null }
            val retval = StringBuilder()
            val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(Locale.US)
            val startTime = startString?.let { parseTime(it) }
            val endTime = endString?.let { parseTime(it) }
            if (startTime != null) {
                retval.append(startTime.format(timeFormatter))
                if (endTime != null) {
                    retval.append(" - ")
                    retval.append(endTime.format(timeFormatter))
                }
            } else {
                check(endTime == null) {
                    "endtime without starttime?"
                }
            }
            return retval.toString().ifEmpty { null }
        }


    private fun parseTime(timeString: String): LocalTime {
        var ts = timeString.trim()
        while(ts.startsWith("0")) {
            ts = ts.substring(1)
        }
        ts = ts.replace("pm", "PM")
        ts = ts.replace("am", "AM")

        val allowedFormats = arrayOf("hh:mm:ss a", "hh:mm a", "h:mm:ss a", "h:mm a", "h:mma", "h a", "ha")
        allowedFormats.forEach {
            try {
                val parser = DateTimeFormatter.ofPattern(it, Locale.US)
                return LocalTime.parse(ts, parser)
            } catch (e: DateTimeParseException) {
                // ignore
            }
        }
        throw Exception("No way to parse $timeString")
    }
}