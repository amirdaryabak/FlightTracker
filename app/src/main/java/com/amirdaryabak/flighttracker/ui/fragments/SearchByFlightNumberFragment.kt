package com.amirdaryabak.flighttracker.ui.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amirdaryabak.flighttracker.ui.viewmodels.MainViewModel
import com.amirdaryabak.flighttracker.R
import com.amirdaryabak.flighttracker.api.Resource
import com.amirdaryabak.flighttracker.models.SearchByFlightNumberAndRoute
import com.amirdaryabak.flighttracker.other.Constants
import com.amirdaryabak.flighttracker.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_search_by_flight_number.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchByFlightNumberFragment : Fragment(R.layout.fragment_search_by_flight_number) {

    private val viewModel: MainViewModel by viewModels()
    lateinit var flightNumber: String
    lateinit var loading: Dialog

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    val TAG = "SearchByFlightNumberFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = showLoading(requireContext())

        btn_search_by_flight_num.setOnClickListener {
            initViews(true)
            setValuesToViews(
                SearchByFlightNumberAndRoute(
                    "Aircraft Test",
                    "Airline Test",
                    "ArrivalActualTime Test",
                    "ArrivalScheduledTime Test",
                    "DepartureActualTime Test",
                    "DepartureScheduledTime Test",
                    "Destination Test",
                    "FlightNumber Test",
                    "Origin Test"
                    )
            )

//            searchByFlightNumber()
        }

        btn_track_by_flight_number.setOnClickListener {
            Toasty.info(requireContext(), "Track clicked").show()
//            viewModel.star("Bearer " + sharedPreferences.getString(Constants.USER_TOKEN, ""), flightNumber)
        }

        setUpSearchByFlightNumberObserver()
        setUpStarObserver()

    }

    private fun setUpStarObserver() {
        viewModel.star.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let { result ->
                        Toasty.success(requireContext(), "Tracked Successfully").show()
                    }
                }
                is Resource.Error -> {
                    loading.dismiss()
                    Toasty.error(requireContext(), response.message.toString()).show()
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        })
    }

    private fun setUpSearchByFlightNumberObserver() {
        viewModel.searchByFlightNumber.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let { result ->
                        initViews(true)
                        flightNumber = result.response.flightNumber
                        setValuesToViews(result.response)
                    }
                }
                is Resource.Error -> {
                    loading.dismiss()
                    initViews(false)
                    Toasty.error(requireContext(), getString(R.string.noInternet)).show()
                }
                is Resource.Loading -> {
                    loading.show()
                    initViews(false)
                }
            }
        })
    }

    private fun searchByFlightNumber() {
        viewModel.getSearchByFlightNumber(
            "Bearer " + sharedPreferences.getString(Constants.USER_TOKEN, ""),
            et_search_by_flight_num.text.toString()
        )
    }

    private fun setValuesToViews(searchByFlightNumberAndRoute: SearchByFlightNumberAndRoute) {
        searchByFlightNumberAndRoute.let {
            txt_departure_time.text = it.departureScheduledTime
            txt_airline.text = it.airline
            txt_flightNumber.text = it.flightNumber
            txt_origin.text = it.origin
            txt_aircraft.text = it.aircraft
            txt_destination.text = it.destination
            txt_arrival_time.text = it.arrivalScheduledTime
        }
    }

    private fun initViews(isVisible: Boolean) {
        if (isVisible) {
            txt_departure.visibility = View.VISIBLE
            txt_departure_time.visibility = View.VISIBLE
            txt_airline.visibility = View.VISIBLE
            txt_flightNumber.visibility = View.VISIBLE
            txt_origin.visibility = View.VISIBLE
            txt_aircraft.visibility = View.VISIBLE
            txt_destination.visibility = View.VISIBLE
            txt_airline_dummy.visibility = View.VISIBLE
            txt_flightNumber_dummy.visibility = View.VISIBLE
            txt_origin_dummy.visibility = View.VISIBLE
            txt_aircraft_dummy.visibility = View.VISIBLE
            txt_destination_dummy.visibility = View.VISIBLE
            txt_arrival.visibility = View.VISIBLE
            txt_arrival_time.visibility = View.VISIBLE
            btn_track_by_flight_number.visibility = View.VISIBLE
        } else {
            txt_departure.visibility = View.INVISIBLE
            txt_departure_time.visibility = View.INVISIBLE
            txt_airline.visibility = View.INVISIBLE
            txt_flightNumber.visibility = View.INVISIBLE
            txt_origin.visibility = View.INVISIBLE
            txt_aircraft.visibility = View.INVISIBLE
            txt_destination.visibility = View.INVISIBLE
            txt_airline_dummy.visibility = View.INVISIBLE
            txt_flightNumber_dummy.visibility = View.INVISIBLE
            txt_origin_dummy.visibility = View.INVISIBLE
            txt_aircraft_dummy.visibility = View.INVISIBLE
            txt_destination_dummy.visibility = View.INVISIBLE
            txt_arrival.visibility = View.INVISIBLE
            txt_arrival_time.visibility = View.INVISIBLE
            btn_track_by_flight_number.visibility = View.INVISIBLE
        }
    }

}
