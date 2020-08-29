package com.sedeghzare.flighttracker.repository

import com.sedeghzare.flighttracker.api.MyApi
import com.sedeghzare.flighttracker.db.MyDao
import com.sedeghzare.flighttracker.db.MyDataBase
import com.sedeghzare.flighttracker.models.GetAirports
import com.sedeghzare.flighttracker.models.IncomingFlight
import com.sedeghzare.flighttracker.models.OutgoingFlight
import com.sedeghzare.flighttracker.models.UserInformation
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val myDao: MyDao,
    private val myApi: MyApi
) {

    // Api
    suspend fun getSearchByAirport(authorization: String, airportName: String) =
        myApi.getSearchByAirport(authorization,airportName)

    suspend fun getAirports() = myApi.getAirports()

    suspend fun getSearchByFlightNumber(Authorization: String, flightNumber: String) =
        myApi.getSearchByFlightNumber(Authorization, flightNumber)

    suspend fun getSearchByRoute(origin: String, destination: String) =
        myApi.getSearchByRoute(origin, destination)

    suspend fun login(userInformation: UserInformation) = myApi.login(userInformation)

    suspend fun signIn(userInformation: UserInformation) = myApi.signIn(userInformation)

    suspend fun getMyFlights(Authorization: String) = myApi.getMyFlights(Authorization)

    suspend fun star(Authorization: String, flightNumber: String) = myApi.star(Authorization, flightNumber)

    // DB
    suspend fun insertAirports(getAirports: GetAirports) = myDao.insertAirports(getAirports)

    fun getAllAirports() = myDao.getAllAirports()

    suspend fun insertIncomingFlight(incomingFlight: IncomingFlight) = myDao.insertIncomingFlight(incomingFlight)

    fun getAllIncomingFlight() = myDao.getAllIncomingFlight()

    suspend fun deleteAllIncomingFlight() = myDao.deleteAllIncomingFlight()

    suspend fun insertOutgoingFlight(outgoingFlight: OutgoingFlight) = myDao.insertOutgoingFlight(outgoingFlight)

    fun getAllOutgoingFlight() = myDao.getAllOutgoingFlight()

    suspend fun deleteAllOutgoingFlight() = myDao.deleteAllOutgoingFlight()


}