package com.amirdaryabak.flighttracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "outgoingFlight"
)
data class OutgoingFlight(
    val actualTime: String,
    val aircraft: String,
    val airline: String,
//    val changedTime: Any,
//    val counters: List<Int>,
    val destination: String,
    @PrimaryKey
    val flightNumber: String,
    val origin: String,
    val scheduledTime: String,
    val secondLevelStatus: Int,
    val status: Int,
    val track: String = "Track"
)