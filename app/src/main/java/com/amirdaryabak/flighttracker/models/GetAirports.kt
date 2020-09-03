package com.amirdaryabak.flighttracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "airports"
)
data class GetAirports(
    @PrimaryKey
    val key: String,
    val value: String
) : Serializable