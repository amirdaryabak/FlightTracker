package com.sedeghzare.flighttracker.ui.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sedeghzare.flighttracker.ui.viewmodels.MainViewModel
import com.sedeghzare.flighttracker.R
import com.sedeghzare.flighttracker.adapters.SearchAdapter
import com.sedeghzare.flighttracker.adapters.SearchByRouteAdapter
import com.sedeghzare.flighttracker.api.Resource
import com.sedeghzare.flighttracker.models.GetAirports
import com.sedeghzare.flighttracker.models.IncomingFlight
import com.sedeghzare.flighttracker.models.SearchByAirport
import com.sedeghzare.flighttracker.models.SearchByFlightNumberAndRoute
import com.sedeghzare.flighttracker.other.Constants
import com.sedeghzare.flighttracker.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_search_by_airport.*
import kotlinx.android.synthetic.main.fragment_search_by_route.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchByRouteFragment : Fragment(R.layout.fragment_search_by_route) {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var shared: SharedPreferences
    lateinit var airports: List<GetAirports>
    lateinit var loading: Dialog
    lateinit var searchByRouteAdapter: SearchByRouteAdapter

    val TAG = "SearchByRouteFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = showLoading(requireContext())

        getAirportsLocally()

        btn_search_by_route.setOnClickListener {
            setUpRecyclerView(getFakeResult())
            /*viewModel.getSearchByRoute(
                airports[spinner_origin_by_route.selectedItemPosition].key,
                airports[spinner_destination_by_route.selectedItemPosition].key
            )*/
        }

        setUpGetSearchByRouteObserver()
        setUpStarObserver()

    }

    private fun setUpGetSearchByRouteObserver() {
        viewModel.getSearchByRoute.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let { result ->
                        setUpRecyclerView(result)
                    }
                }
                is Resource.Error -> {
                    loading.dismiss()
                    Toasty.error(requireContext(), getString(R.string.noInternet)).show()
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        })
    }

    private fun getAirportsLocally() {
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
            airports = it
            val myArrayList: ArrayList<String> = ArrayList()
            for (item in it) {
                myArrayList.add(item.value)
            }
            setArrayListToSpinner(myArrayList)
        })
    }

    private fun setArrayListToSpinner(myArrayList: ArrayList<String>) {
        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, myArrayList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_origin_by_route.adapter = aa
        spinner_destination_by_route.adapter = aa
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

    private fun setUpRecyclerView(searchByFlightNumberAndRoute: List<SearchByFlightNumberAndRoute>) {
        searchByRouteAdapter = SearchByRouteAdapter(1)
        searchByRouteAdapter.differ.submitList(searchByFlightNumberAndRoute)
        searchByRouteAdapter.setOnItemClickListenerIncomingFlight {
            viewModel.star("Bearer " + shared.getString(Constants.USER_TOKEN, ""), it.flightNumber)
        }
        rv_search_by_route.apply {
            adapter = searchByRouteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getFakeResult(): List<SearchByFlightNumberAndRoute> {
        val myArrayList: ArrayList<SearchByFlightNumberAndRoute> = ArrayList()
        for (i in 1..30) {
            myArrayList.add(
                SearchByFlightNumberAndRoute(
                    "aircraft $i",
                    "airline $i",
                    "arrivalActualTime $i",
                    "arrivalScheduledTime $i",
                    "departureActualTime $i",
                    "departureScheduledTime $i",
                    "destination $i",
                    "flightNumber $i",
                    "origin $i"
                )
            )
        }
        return myArrayList
    }

}
