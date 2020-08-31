package org.madisonbikes.bikeweek

import okio.buffer
import okio.sink
import org.supercsv.io.CsvMapReader
import org.supercsv.prefs.CsvPreference
import java.io.File
import java.io.FileReader

class ProcessEvents(
        val csvFile: File,
        val buildDir: File,
        val eventsMarkdownFile: File,
        val eventsMarkdownForPrintFile: File,
        val locationFilePath: File
) : Runnable {

    override fun run() {
        val events = mutableListOf<Event>()

        CsvMapReader(FileReader(csvFile), CsvPreference.STANDARD_PREFERENCE).use { mapReader ->
            val header = mapReader.getHeader(true)

            var line: Map<String, String>?
            while (true) {
                line = mapReader.read(*header)
                if (line == null) break

                val eventItem = Event.fromMapPairs(line
                        .withDefault { "" }
                        .mapValues { if (it.value == null) "" else it.value }
                )
                events += eventItem
            }

            /*
            buildDir.mkdirs()
            File(buildDir, "events.dump").sink().buffer().use {
                it.writeUtf8(events.toString())
            }
             */

            eventsMarkdownFile.parentFile?.mkdirs()
            eventsMarkdownFile.sink().buffer().use {
                CalendarWriter(calendarSink = it, events = events, forPrint = false)
                        .writeCalendarMarkdown()
            }

            /*
            eventsMarkdownForPrintFile.parentFile?.mkdirs()
            eventsMarkdownForPrintFile.sink().buffer().use {
                CalendarWriter(calendarSink = it, events = events, forPrint = true)
                        .writeCalendarMarkdown()
            }
             */

            /*
            locationFilePath.mkdirs()
            val locationDisambiguator = LocationDisambiguator()
            EventDay.ALL.forEach { day ->
                val name = "${day.renderAsString()} locations.csv"
                val dayFile = File(locationFilePath, name)
                dayFile.sink().buffer().use {
                    LocationWriter(
                            locationsSink = it,
                            events = events,
                            day = day,
                            locationDisambiguator = locationDisambiguator
                    )
                            .writeLocationsToCsv()
                }
            }
             */

        }
    }
}
