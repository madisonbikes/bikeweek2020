package org.madisonbikes.bikeweek

import net.steppschuh.markdowngenerator.MarkdownElement
import net.steppschuh.markdowngenerator.link.Link
import net.steppschuh.markdowngenerator.list.ListBuilder
import net.steppschuh.markdowngenerator.text.Text
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText
import net.steppschuh.markdowngenerator.text.heading.Heading
import okhttp3.HttpUrl
import okio.BufferedSink
import java.text.DateFormat
import java.util.*

class CalendarWriter(
    private val calendarSink: BufferedSink,
    private val events: List<Event>,
    private val forPrint: Boolean
) {

    fun writeCalendarMarkdown() {
        var allEventCount = 0
        var level = 1
        try {
            level++
            EventDay.ALL.forEach { day ->

                calendarSink += "\n"
                calendarSink += Heading(day.headerString, level)

                var dayCount = 0
                events
                    .filter { it.eventDays.contains(day) }
                    .sortedBy { it.eventTimes.firstOrNull()?.startTimeAsLocalTime }
                    .forEach {
                        dayCount++
                        allEventCount++
                        outputEvent(calendarSink, it, level + 1)
                    }
                if (dayCount == 0) {
                    calendarSink += "No events\n\n"
                }
                if (!forPrint) {
                    calendarSink += createLink("\\[up\\]", "#TOC")
                    calendarSink += "\n"
                }
            }
        } finally {
            level--
        }
        if (!forPrint) {
            val updateTime = DateFormat.getDateTimeInstance().format(Date())
            calendarSink += ItalicText("Calendar last updated $updateTime")
            calendarSink += "\n"
        }

        println("$allEventCount events!")
    }

    private fun outputEvent(calendarSink: BufferedSink, event: Event, level: Int) {

        val location = event.location

        calendarSink += "\n"
        val name = createLink(event.name, event.url)
        calendarSink += Heading(name, level)
        calendarSink += "\n"

        val urlBuilder = HttpUrl.Builder()
            .scheme("https")
            .host("www.google.com")
            .addPathSegments("maps/search/")
            .addQueryParameter("api", "1")

        urlBuilder.addQueryParameter("query", location.mapsQuery)
        if (!location.mapsPlaceId.isNullOrBlank()) {
            urlBuilder.addQueryParameter("query_place_id", location.mapsPlaceId)
        }

        val listBuilder = ListBuilder()

        if (location.showOnMap) {
            val link = createLink(requireNotNull(location.mapsDescription), urlBuilder.toString())
            listBuilder.append(Text("$link"))
        } else if (!location.freeformMarkdownDescription.isNullOrBlank()) {
            var text = location.freeformMarkdownDescription
            if (forPrint) {
                text = text.stripMarkdownLinks()
            }
            listBuilder.append(Text(text))
        }
        event.renderedTime?.let {
            listBuilder.append(Text(it))
        }
        /**
         * removed now that we have good event descriptions
         *
         * val type = event.eventTypes.joinToString { it.description }
         * listBuilder.append(Text("Type: $type"))
         */
        if (event.sponsors.isNotEmpty()) {
            val sponsorText = StringBuilder()
            if (!forPrint) {
                sponsorText.append("Sponsored by ")
            }

            event.sponsors.forEachIndexed { index: Int, sponsor: String ->
                if (index > 0) {
                    if (!forPrint && index == event.sponsors.size - 1) {
                        sponsorText.append(" and ")
                    } else {
                        sponsorText.append(", ")
                    }
                }

                val url = event.sponsorUrls.getOrNull(index)
                sponsorText.append(createLink(sponsor, url).toString())
            }
            listBuilder.append("$sponsorText")
        }
        calendarSink += listBuilder.toMarkdownElement()
        calendarSink += "\n"

        if (event.description.isNotEmpty()) {
            var mdDescription = event.description
            // look for special backslash at end of line denoting a soft carriage return and preserve it
            mdDescription = mdDescription.replace("\\\n", "#SOFTCR#")
            // change normal carriage returns into doubles to force actual break
            mdDescription = mdDescription.replace("\n", "\n\n")
            // bring back the soft cr for things like lists, etc
            mdDescription = mdDescription.replace("#SOFTCR#", "\n")
            if (forPrint) {
                mdDescription = mdDescription.stripMarkdownLinks()
            }
            calendarSink += "$mdDescription\n\n"
        }
    }

    private operator fun BufferedSink.plusAssign(element: MarkdownElement) {
        this.writeUtf8(element.toString())
        this.writeUtf8("\n")
    }

    private operator fun BufferedSink.plusAssign(string: String) {
        this.writeUtf8(string)
    }

    private fun String.stripMarkdownLinks(): String {
        val linksRegexp = Regex("\\[(.*)]\\(.*\\)")
        return replace(linksRegexp, "$1")
    }

    private fun createLink(text: Any, url: String?): MarkdownElement {
        return if (forPrint || url.isNullOrBlank()) {
            Text(text)
        } else {
            Link(text, url)
        }
    }
}