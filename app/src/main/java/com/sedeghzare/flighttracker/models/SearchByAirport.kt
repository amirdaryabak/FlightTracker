package com.sedeghzare.flighttracker.models

data class SearchByAirport(
    val airport: String,
    val incomingFlights: List<IncomingFlight>,
    val outgoingFlights: List<OutgoingFlight>,
    val trackedFlights: List<String>
)