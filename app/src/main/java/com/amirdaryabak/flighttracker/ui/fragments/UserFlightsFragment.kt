package com.amirdaryabak.flighttracker.ui.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amirdaryabak.flighttracker.R
import com.amirdaryabak.flighttracker.adapters.SearchByRouteAdapter
import com.amirdaryabak.flighttracker.api.Resource
import com.amirdaryabak.flighttracker.models.SearchByFlightNumberAndRoute
import com.amirdaryabak.flighttracker.other.Constants
import com.amirdaryabak.flighttracker.ui.viewmodels.MainViewModel
import com.amirdaryabak.flighttracker.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_flights.*
import javax.inject.Inject

@AndroidEntryPoint
class UserFlightsFragment : Fragment(R.layout.fragment_user_flights) {

    private val viewModel: MainViewModel by viewModels()
    lateinit var loading: Dialog

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var sharedPref: SharedPreferences
    lateinit var searchByRouteAdapter: SearchByRouteAdapter

    val TAG = "UserFlightsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = showLoading(requireContext())

        if (sharedPref.getString(Constants.USER_TOKEN, "") != "") {
            btn_log_in_flights.visibility = View.GONE
            txt_inform_user.visibility = View.GONE
            getMyFlights()
            setUpGetMyFlightsObserver()
        } else {
            btn_log_in_flights.visibility = View.VISIBLE
            txt_inform_user.visibility = View.VISIBLE
        }

        btn_log_in_flights.setOnClickListener {
            sharedPref.edit().putBoolean(Constants.IS_SKIPPED, false).apply()
            findNavController().navigate(R.id.action_userFlightsFragment_to_logInFragment)
        }

    }

    private fun setUpGetMyFlightsObserver() {
        viewModel.getMyFlights.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let { result ->
                        setUpRecyclerView(result)
                    }
                }
                is Resource.Error -> {
                    loading.dismiss()
                    txt_list_empty.text = response.message.toString()
                    txt_list_empty.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        })
    }

    private fun getMyFlights() {
        setUpRecyclerView(getFakeResult())
//        viewModel.getMyFlights("Bearer " + sharedPreferences.getString(Constants.USER_TOKEN, ""))
    }

    private fun setUpRecyclerView(searchByFlightNumberAndRoute: List<SearchByFlightNumberAndRoute>) {
        searchByRouteAdapter = SearchByRouteAdapter(2)
        searchByRouteAdapter.differ.submitList(searchByFlightNumberAndRoute)
        /*searchByRouteAdapter.setOnItemClickListenerIncomingFlight {

        }*/
        rv_user_flights.apply {
            adapter = searchByRouteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getFakeResult(): List<SearchByFlightNumberAndRoute> {
        val myArrayList: ArrayList<SearchByFlightNumberAndRoute> = ArrayList()
        for (i in 1..10) {
            myArrayList.add(
                SearchByFlightNumberAndRoute(
                    "Your aircraft $i",
                    "Your airline $i",
                    "Your arrivalActualTime $i",
                    "Your arrivalScheduledTime $i",
                    "Your departureActualTime $i",
                    "Your departureScheduledTime $i",
                    "Your destination $i",
                    "Your flightNumber $i",
                    "Your origin $i"
                )
            )
        }
        return myArrayList
    }

}
