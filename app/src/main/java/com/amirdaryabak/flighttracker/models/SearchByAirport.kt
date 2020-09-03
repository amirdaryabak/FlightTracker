package com.amirdaryabak.flighttracker.models

data class SearchByAirport(
    val airport: String,
    val incomingFlights: List<IncomingFlight>,
    val outgoingFlights: List<OutgoingFlight>,
    val trackedFlights: List<String>
)