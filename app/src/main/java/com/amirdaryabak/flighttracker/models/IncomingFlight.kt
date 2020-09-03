package com.amirdaryabak.flighttracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "incomingFlight"
)
data class IncomingFlight(
    val actualTime: String,
    val aircraft: String,
    val airline: String,
    val destination: String,
    @PrimaryKey
    val flightNumber: String,
    val origin: String,
    val scheduledTime: String,
    val secondLevelStatus: Int,
    val status: Int,
    val track: String = "Track"
)