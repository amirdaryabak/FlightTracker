package com.amirdaryabak.flighttracker.models

data class SearchByFlightNumberAndRoute(
    val aircraft: String,
    val airline: String,
    val arrivalActualTime: String,
    val arrivalScheduledTime: String,
    val departureActualTime: String,
    val departureScheduledTime: String,
    val destination: String,
    val flightNumber: String,
    val origin: String
)