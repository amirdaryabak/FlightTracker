package com.amirdaryabak.flighttracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amirdaryabak.flighttracker.models.GetAirports
import com.amirdaryabak.flighttracker.models.IncomingFlight
import com.amirdaryabak.flighttracker.models.OutgoingFlight

@Database(
    entities = [GetAirports::class, IncomingFlight::class, OutgoingFlight::class],
    version = 1
)
abstract class MyDataBase : RoomDatabase(){

    abstract fun getMyDao(): MyDao
}