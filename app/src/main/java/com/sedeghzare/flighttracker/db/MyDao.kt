package com.sedeghzare.flighttracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sedeghzare.flighttracker.models.GetAirports
import com.sedeghzare.flighttracker.models.IncomingFlight
import com.sedeghzare.flighttracker.models.OutgoingFlight

@Dao
interface MyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirports(getAirports: GetAirports): Long

    @Query("SELECT * FROM airports")
    fun getAllAirports(): LiveData<List<GetAirports>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncomingFlight(incomingFlight: IncomingFlight): Long

    @Query("SELECT * FROM incomingFlight")
    fun getAllIncomingFlight(): LiveData<List<IncomingFlight>>

    @Query("DELETE FROM incomingFlight")
    suspend fun deleteAllIncomingFlight()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutgoingFlight(outgoingFlight: OutgoingFlight): Long

    @Query("SELECT * FROM outgoingFlight")
    fun getAllOutgoingFlight(): LiveData<List<OutgoingFlight>>

    @Query("DELETE FROM outgoingFlight")
    suspend fun deleteAllOutgoingFlight()

}