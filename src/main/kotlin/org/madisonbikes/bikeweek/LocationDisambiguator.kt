package org.madisonbikes.bikeweek

/** TODO do better disambiguating that slowly circles out from fixed repeating point. This blindly advances in one direction and will fail w/more than 5. */
class LocationDisambiguator {

    class ValueMover(initialValue: Int) {
        private var diff: Int
        private var lastValue = initialValue

        init {
            if (initialValue > 5) {
                diff = -1
                lastValue = initialValue + 1
            } else {
                diff = 1
                lastValue = initialValue - 1
            }
        }

        fun getNextValue(): Int {
            lastValue += diff
            require(lastValue >= 0)
            require(lastValue < 10)
            return lastValue
        }
    }

    private val locationMap = mutableMapOf<String, Pair<ValueMover, ValueMover>>()

    fun disambiguateLocation(location: String): String {
        val dNorth = location.indexOf("\"N")
        val dWest = location.indexOf("\"W")

        if (dNorth == -1 || dWest == -1) {
            return location
        }

        var alter = locationMap[location]
        if (alter == null) {
            val north = location[dNorth - 1] - '0'
            val west = location[dWest - 1] - '0'
            alter = ValueMover(north) to ValueMover(west)
            locationMap[location] = alter
        }

        val newLocation = StringBuilder(location)
        newLocation[dNorth - 1] = '0' + alter.first.getNextValue()
        newLocation[dWest - 1] = '0' + alter.second.getNextValue()
        return newLocation.toString()
    }
}