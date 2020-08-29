package com.sedeghzare.flighttracker.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedeghzare.flighttracker.api.Resource
import com.sedeghzare.flighttracker.models.*
import com.sedeghzare.flighttracker.other.Constants.CONNECTION_ERROR
import com.sedeghzare.flighttracker.other.Constants.NETWORK_FAILURE
import com.sedeghzare.flighttracker.repository.MainRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val searchByAirport: MutableLiveData<Resource<SearchByAirport>> = MutableLiveData()
    val getAirports: MutableLiveData<Resource<List<GetAirports>>> = MutableLiveData()
    val searchByFlightNumber: MutableLiveData<Resource<SearchByFlightNumberDM>> = MutableLiveData()
    val getSearchByRoute: MutableLiveData<Resource<List<SearchByFlightNumberAndRoute>>> = MutableLiveData()
    val login: MutableLiveData<Resource<User>> = MutableLiveData()
    val signIn: MutableLiveData<Resource<User>> = MutableLiveData()
    val getMyFlights: MutableLiveData<Resource<List<SearchByFlightNumberAndRoute>>> = MutableLiveData()
    val star: MutableLiveData<Resource<StarFlight>> = MutableLiveData()

    // Api
    fun getSearchByAirport(authorization: String, airportName: String) = viewModelScope.launch {
        try {
            searchByAirport.postValue(Resource.Loading())
            val response = mainRepository.getSearchByAirport(authorization, airportName)
            searchByAirport.postValue(handleSearchByAirportResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchByAirport.postValue((Resource.Error(NETWORK_FAILURE)))
                else -> searchByAirport.postValue(Resource.Error(CONNECTION_ERROR))
            }
        }
    }

    private fun handleSearchByAirportResponse(response: Response<SearchByAirport>):
            Resource<SearchByAirport> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getAirports() = viewModelScope.launch {
        try {
            getAirports.postValue(Resource.Loading())
            val response = mainRepository.getAirports()
            getAirports.postValue(handleGetAirportsResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getAirports.postValue((Resource.Error(NETWORK_FAILURE)))
                else -> getAirports.postValue(Resource.Error(CONNECTION_ERROR))
            }
        }
    }

    private fun handleGetAirportsResponse(response: Response<List<GetAirports>>):
            Resource<List<GetAirports>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getSearchByFlightNumber(Authorization: String, flightNumber: String) = viewModelScope.launch {
        try {
            searchByFlightNumber.postValue(Resource.Loading())
            val response = mainRepository.getSearchByFlightNumber(Authorization, flightNumber)
            searchByFlightNumber.postValue(handleSearchByFlightNumberResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchByFlightNumber.postValue((Resource.Error(NETWORK_FAILURE)))
                else -> searchByFlightNumber.postValue(Resource.Error(CONNECTION_ERROR))
            }
        }
    }

    private fun handleSearchByFlightNumberResponse(response: Response<SearchByFlightNumberDM>):
            Resource<SearchByFlightNumberDM> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getSearchByRoute(origin: String, destination: String) = viewModelScope.launch {
        try {
            getSearchByRoute.postValue(Resource.Loading())
            val response = mainRepository.getSearchByRoute(origin, destination)
            getSearchByRoute.postValue(handleGetSearchByRouteResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getSearchByRoute.postValue((Resource.Error(NETWORK_FAILURE)))
                else -> getSearchByRoute.postValue(Resource.Error(CONNECTION_ERROR))
            }
        }
    }

    private fun handleGetSearchByRouteResponse(response: Response<List<SearchByFlightNumberAndRoute>>):
            Resource<List<SearchByFlightNumberAndRoute>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun login(userInformation: UserInformation) = viewModelScope.launch {
        try {
            login.postValue(Resource.Loading())
            val response = mainRepository.login(userInformation)
            login.postValue(handleLoginResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> signIn.postValue((Resource.Error(NETWORK_FAILURE)))
                else -> signIn.postValue(Resource.Error(CONNECTION_ERROR))
            }
        }


    }

    private fun handleLoginResponse(response: Response<User>): Resource<User> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        } else if (response.code() == 500) {
            return Resource.Error("User not found")
        }
        return Resource.Error(response.message())
    }

    fun signIn(userInformation: UserInformation) = viewModelScope.launch {
        try {
            signIn.postValue(Resource.Loading())
            val response = mainRepository.signIn(userInformation)
            signIn.postValue(handleSignInResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> signIn.postValue((Resource.Error(NETWORK_FAILURE)))
                else -> signIn.postValue(Resource.Error(CONNECTION_ERROR))
            }
        }
    }

    private fun handleSignInResponse(response: Response<User>): Resource<User> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        } else if (response.code() == 500) {
            return Resource.Error("This username is already taken")
        }
        return Resource.Error(response.message())
    }

    fun getMyFlights(Authorization: String) = viewModelScope.launch {
        try {
            getMyFlights.postValue(Resource.Loading())
            val response = mainRepository.getMyFlights(Authorization)
            getMyFlights.postValue(handleGetMyFlightsResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getMyFlights.postValue((Resource.Error(NETWORK_FAILURE)))
                else -> getMyFlights.postValue(Resource.Error(CONNECTION_ERROR))
            }
        }
    }

    private fun handleGetMyFlightsResponse(response: Response<List<SearchByFlightNumberAndRoute>>):
            Resource<List<SearchByFlightNumberAndRoute>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        } else if (response.code() == 500) {
            return Resource.Error("You don't have any flight to track")
        }
        return Resource.Error(response.message())
    }

    fun star(Authorization: String, flightNumber: String) = viewModelScope.launch {
        try {
            star.postValue(Resource.Loading())
            val response = mainRepository.star(Authorization, flightNumber)
            star.postValue(handleStarResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> star.postValue((Resource.Error(NETWORK_FAILURE)))
                else -> star.postValue(Resource.Error(CONNECTION_ERROR))
            }
        }
    }

    private fun handleStarResponse(response: Response<StarFlight>):
            Resource<StarFlight> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        } else if (response.code() == 500) {
            return Resource.Error("You Tracked This Flight Before")
        }
        return Resource.Error(response.message())
    }

    // DB
    fun insertAirports(getAirports: GetAirports) = viewModelScope.launch {
        mainRepository.insertAirports(getAirports)
    }

    fun getSavedNews() = mainRepository.getAllAirports()

    fun insertIncomingFlight(incomingFlight: IncomingFlight) = viewModelScope.launch {
        mainRepository.insertIncomingFlight(incomingFlight)
    }

    fun getAllIncomingFlight() = mainRepository.getAllIncomingFlight()

    fun deleteAllIncomingFlight() = viewModelScope.launch {
        mainRepository.deleteAllIncomingFlight()
    }

    fun insertOutgoingFlight(outgoingFlight: OutgoingFlight) = viewModelScope.launch {
        mainRepository.insertOutgoingFlight(outgoingFlight)
    }

    fun getAllOutgoingFlight() = mainRepository.getAllOutgoingFlight()

    fun deleteAllOutgoingFlight() = viewModelScope.launch {
        mainRepository.deleteAllOutgoingFlight()
    }

}