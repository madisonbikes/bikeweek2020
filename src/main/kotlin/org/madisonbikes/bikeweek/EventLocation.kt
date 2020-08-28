package org.madisonbikes.bikeweek

data class EventLocation(
    val mapsDescription: String?,
    val freeformMarkdownDescription: String?,
    val mapsQueryBase: String?,
    val mapsPlaceId: String?,
    val outsideOfMadison: Boolean
) {

    val showOnMap: Boolean
        get() = mapsDescription?.isNotBlank() ?: false

    val mapsQuery: String
        get() {
            return if (!mapsQueryBase.isNullOrBlank()) {
                mapsQueryBase
            } else {
                if (outsideOfMadison) {
                    "$mapsDescription, WI"
                } else {
                    "$mapsDescription Madison, WI"
                }
            }
        }
}