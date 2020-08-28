package org.madisonbikes.bikeweek

import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.impl.type.FileArgumentType
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.inf.ArgumentType
import org.madisonbikes.bikeweek.ProcessEvents
import java.io.File

class ProcessEventsTask {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val argumentParser = ArgumentParsers.newFor("MadisonBikeWeek")
                    .fromFilePrefix("@")
                    .build()
            argumentParser.apply {
                addArgument("csvInput")
                        .type(FileArgumentType().verifyCanRead())
                        .setDefault("events.csv")
                        .required(true)
                addArgument("output")
                        .type(FileArgumentType().verifyCanCreate())
                        .setDefault("output")

                try {
                    val parsedArgs = argumentParser.parseArgs(args)
                    val input = parsedArgs.get<File>("csvInput")
                    val output = parsedArgs.get<File>("output")
                    output.mkdirs()

                    val locations = File(output, "locations")
                    locations.mkdirs()

                    val task = ProcessEvents(
                        csvFile = input,
                        buildDir = output,
                        eventsMarkdownFile = File(output, "events.md"),
                        locationFilePath = locations,
                        eventsMarkdownForPrintFile = File(output, "events_for_print.md")
                    )
                    task.run()
                } catch (e: ArgumentParserException) {
                    argumentParser.handleError(e)
                }
            }
        }
    }
}
