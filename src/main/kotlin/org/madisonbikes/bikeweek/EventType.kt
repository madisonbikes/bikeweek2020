package org.madisonbikes.bikeweek

enum class EventType(val description: String, val fontAwesomeIcon: String? = null) {
    STATION_FOOD("Bike Station (with food)"),
    STATION_TUNEUPS("Bike Station (with tuneups)", "tools"),
    DISCOUNTS("Rider discounts"),
    RIDE("Ride routes etc", "route"),
    PART_OF_EVENT("Part of Larger Event"),
    EVENT("Event"),
    VIRTUAL_EVENT("Virtual Event"),
    DONATION_SITE("Donation Site"),
    GIVEAWAYS("Giveaways", "gift"),
    PAID("Paid Event", "dollar-sign")
}