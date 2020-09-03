package org.madisonbikes.bikeweek

enum class EventType(val description: String, val fontAwesomeIcon: String? = null) {
    STATION_FOOD("Bike Station (with food)", "utensils"),
    STATION_TUNEUPS("Bike Station (with tuneups)", "tools"),
    DISCOUNTS("Rider discounts", "percent"),
    RIDE("Rides and Routes", "map-marked-alt"),
    PART_OF_EVENT("Part of Larger Event"),
    EVENT("Event"),
    VIRTUAL_EVENT("Virtual Event", "video"),
    DONATION_SITE("Donation Site", "hand-holding-heart"),
    GIVEAWAYS("Giveaways", "gift"),
    PAID("Paid Event", "dollar-sign")
}