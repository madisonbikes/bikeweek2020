package org.madisonbikes.bikeweek

enum class EventType(val description: String) {
    STATION_FOOD("Bike Station (with food)"),
    STATION_TUNEUPS("Bike Station (with tuneups)"),
    DISCOUNTS("Rider discounts"),
    RIDE("Ride routes etc"),
    PART_OF_EVENT("Part of Larger Event"),
    EVENT("Event"),
    VIRTUAL_EVENT("Virtual Event"),
    DONATION_SITE("Donation Site"),
    GIVEAWAYS("Giveaways"),
}