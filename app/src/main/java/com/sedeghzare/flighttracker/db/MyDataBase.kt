package com.sedeghzare.flighttracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sedeghzare.flighttracker.models.GetAirports
import com.sedeghzare.flighttracker.models.IncomingFlight
import com.sedeghzare.flighttracker.models.MyDataModel
import com.sedeghzare.flighttracker.models.OutgoingFlight

@Database(
    entities = [GetAirports::class, IncomingFlight::class, OutgoingFlight::class],
    version = 1
)
abstract class MyDataBase : RoomDatabase(){

    abstract fun getMyDao(): MyDao
}