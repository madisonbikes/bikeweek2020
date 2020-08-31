package org.madisonbikes.bikeweek

import okio.BufferedSink
import org.supercsv.io.CsvMapWriter
import org.supercsv.prefs.CsvPreference
import java.io.OutputStreamWriter

class LocationWriter(
    private val locationsSink: BufferedSink,
    private val events: List<Event>,
    private val day: EventDay,
    private val locationDisambiguator: LocationDisambiguator
) {

    fun writeLocationsToCsv() {
        CsvMapWriter(
            OutputStreamWriter(locationsSink.outputStream(), "UTF-8"),
            CsvPreference.STANDARD_PREFERENCE
        ).use { csvWriter ->
            val columns = arrayOf(
                "Name",
                "Location",
                "Description",
                "Date",
                "Time",
                "Event Type",
                "Sponsors",
                "Detailed Location"
            )
            csvWriter.writeHeader(*columns)

            events
                .filter { it.eventDays.contains(day) }
                .filter { it.location.showOnMap }
                .forEach { event ->
                    val item = mutableMapOf<String, String>()
                    item["Name"] = event.name
                    item["Location"] = requireNotNull(event.location.mapsDescription)
                    item["Detailed Location"] = locationDisambiguator.disambiguateLocation(event.location.mapsQuery)
                    item["Description"] = event.description
                    item["Date"] = day.headerString
                    event.renderedTime?.let {
                        item["Time"] = it
                    }
                    item["Event Type"] = event.eventTypes.joinToString { it.description }
                    item["Sponsors"] = event.sponsors.joinToString()
                    csvWriter.write(item, *columns)
                }
        }
    }

}