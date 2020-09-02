package org.madisonbikes.bikeweek

data class EventLocation(
    val mapsDescription: String?,
    val freeformMarkdownDescription: String?,
    val mapsQueryBase: String?,
    val mapsPlaceId: String?
) {
    companion object {
        val regionalCities = listOf("Fitchburg", "Middleton", "Verona", "Sun Prairie", "McFarland")
    }

    private val outsideMadison by lazy {
        regionalCities.any {
            mapsDescription?.endsWith(", $it") ?: false
        }
    }

    val showOnMap
        get() = mapsDescription.isNullOrBlank()

    val mapsQuery: String
        get() {
            return if (!mapsQueryBase.isNullOrBlank()) {
                mapsQueryBase
            } else {
                if (outsideMadison) {
                    "$mapsDescription, WI"
                } else {
                    "$mapsDescription Madison, WI"
                }
            }
        }
}