package com.sedeghzare.flighttracker.models

import com.google.gson.annotations.SerializedName

data class SearchByFlightNumberDM(
    @SerializedName("response")
    val response: SearchByFlightNumberAndRoute,
    val isTracked: Boolean
)