package com.sedeghzare.flighttracker.api

import com.sedeghzare.flighttracker.models.*
import retrofit2.Response
import retrofit2.http.*

interface MyApi {

    @GET("api/flights/search/airport/{airportName}")
    suspend fun getSearchByAirport(
        @Header("Authorization") Authorization: String?,
        @Path("airportName") airportName: String
    ): Response<SearchByAirport>

    @GET("api/flights/airports")
    suspend fun getAirports(): Response<List<GetAirports>>

    @GET("api/flights/search/flight/{flightNumber}")
    suspend fun getSearchByFlightNumber(
        @Header("Authorization") Authorization: String?,
        @Path("flightNumber") flightNumber: String
    ): Response<SearchByFlightNumberDM>

    @GET("api/flights/search/route/{origin}/{destination}")
    suspend fun getSearchByRoute(
        @Path("origin") origin: String,
        @Path("destination") destination: String
    ): Response<List<SearchByFlightNumberAndRoute>>

    @POST("api/user/login")
    suspend fun login(
        @Body userInformation: UserInformation
    ): Response<User>

    @POST("api/user/signup")
    suspend fun signIn(
        @Body userInformation: UserInformation
    ): Response<User>

    @GET("api/flights/tracked")
    suspend fun getMyFlights(
        @Header("Authorization") Authorization: String?
    ): Response<List<SearchByFlightNumberAndRoute>>

    @POST("api/user/star")
    suspend fun star(
        @Header("Authorization") Authorization: String?,
        @Query("flightNumber") flightNumber: String
    ): Response<StarFlight>
    
}