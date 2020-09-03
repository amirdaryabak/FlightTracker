package com.amirdaryabak.flighttracker.ui.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.amirdaryabak.flighttracker.R
import com.amirdaryabak.flighttracker.adapters.SearchAdapter
import com.amirdaryabak.flighttracker.api.Resource
import com.amirdaryabak.flighttracker.models.GetAirports
import com.amirdaryabak.flighttracker.models.IncomingFlight
import com.amirdaryabak.flighttracker.models.SearchByAirport
import com.amirdaryabak.flighttracker.other.Constants.IS_FIRST_TIME_OPEN_APP
import com.amirdaryabak.flighttracker.other.Constants.USER_TOKEN
import com.amirdaryabak.flighttracker.ui.viewmodels.MainViewModel
import com.amirdaryabak.flighttracker.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_search_by_airport.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchByAirportFragment : Fragment(R.layout.fragment_search_by_airport) {

    private val viewModel: MainViewModel by viewModels()
    lateinit var searchAdapter: SearchAdapter
    lateinit var loading: Dialog

    @Inject
    lateinit var shared: SharedPreferences
    lateinit var airports: List<GetAirports>

    /*lateinit var receiver: MyBroadcastReceiver*/

    val TAG = "SearchByAirportFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = showLoading(requireContext())

        /*receiver = MyBroadcastReceiver()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            requireContext().registerReceiver(receiver, it)
        }*/

        /*// StartService
        Intent(requireContext(), MyService::class.java).also {
            requireContext().startService(it)
        }

        // StopService
        Intent(requireContext(), MyService::class.java).also {
            val data = "AmirService"
            it.putExtra("EXTRA_DATA", data)
            requireContext().stopService(it)
        }

        // send data
        Intent(requireContext(), MyService::class.java).also {
            val data = GetAirports("1","2")
            it.putExtra("EXTRA_DATA", data)
            requireContext().startService(it)
        }*/

        /*val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(requireContext()).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        createNotificationChannel()
        val notification = NotificationCompat.Builder(requireContext(), CHANEL_ID)
            .setContentTitle("ContentTitle")
            .setContentText("ContentText")
            .setSmallIcon(R.drawable.ic_baseline_flight_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        val notificationManager = NotificationManagerCompat.from(requireContext())
        notificationManager.notify(NOTIFICATION_ID, notification)*/

        if (isFirstTimeOpenApp()) {
            getAirportsFromServer()
        } else {
            getAirportsLocally()
        }

        btn_search_by_airport.setOnClickListener {
            searchByAirport()
        }

        setUpSearchByAirportObserver()
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

    private fun searchByAirport() {
        setUpRecyclerView(
            SearchByAirport(
                "",
                arrayListOf(),
                arrayListOf(),
                arrayListOf()
            ), true
        )

        /*viewModel.getSearchByAirport(
            "Bearer " + shared.getString(USER_TOKEN, ""),
            airports[spinner_airport.selectedItemPosition].key
        )*/
    }

    private fun isFirstTimeOpenApp(): Boolean {
        return false
//        return shared.getBoolean(IS_FIRST_TIME_OPEN_APP, true)
    }

    private fun setUpSearchByAirportObserver() {
        viewModel.searchByAirport.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let { result ->
                        if (result.incomingFlights.isEmpty() && result.outgoingFlights.isEmpty())
                            txt_empty_list.visibility = View.VISIBLE
                        else if (radio_arrival.isChecked && result.incomingFlights.isEmpty())
                            txt_empty_list.visibility = View.VISIBLE
                        else if (radio_departure.isChecked && result.outgoingFlights.isEmpty())
                            txt_empty_list.visibility = View.VISIBLE
                        else
                            txt_empty_list.visibility = View.GONE

                        setUpRecyclerView(result, radio_arrival.isChecked)
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
        val myArrayList: ArrayList<String> = ArrayList()
        for (i in 1..30) {
            myArrayList.add("Airport $i")
        }
        setArrayListToSpinner(myArrayList)

        /*viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
            airports = it
            val myArrayList: ArrayList<String> = ArrayList()
            for (item in it) {
                myArrayList.add(item.value)
            }
            setArrayListToSpinner(myArrayList)
        })*/
    }

    private fun setArrayListToSpinner(myArrayList: ArrayList<String>) {
        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, myArrayList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_airport.adapter = aa
    }

    private fun getAirportsFromServer() {
        viewModel.getAirports()

        viewModel.getAirports.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { result ->
                        val myArrayList: ArrayList<String> = ArrayList()
                        for (item in result) {
                            viewModel.insertAirports(item)
                            myArrayList.add(item.value)
                        }
                        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
                            airports = it
                        })
                        setArrayListToSpinner(myArrayList)
                        shared.edit().putBoolean(IS_FIRST_TIME_OPEN_APP, false).apply()
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })
    }

    private fun setUpRecyclerView(searchByAirport: SearchByAirport, isArrival: Boolean) {
        if (isArrival) {
            searchAdapter = SearchAdapter(isArrival, searchByAirport.trackedFlights)
            searchAdapter.differ.submitList(getFakeResult())
            searchAdapter.setOnItemClickListenerIncomingFlight {
                loading.show()
                Handler().postDelayed({
                    loading.dismiss()
                }, 3000)
                /*viewModel.star(
                    "Bearer " + shared.getString(USER_TOKEN, ""),
                    it.flightNumber
                )*/
            }
        } else {
            searchAdapter = SearchAdapter(isArrival, searchByAirport.trackedFlights)
            searchAdapter.differ2.submitList(searchByAirport.outgoingFlights)
            searchAdapter.setOnItemClickListenerOutgoingFlight {
                viewModel.star(
                    "Bearer " + shared.getString(USER_TOKEN, ""),
                    it.flightNumber
                )
            }
        }
        rv_search_by_airport.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getFakeResult(): List<IncomingFlight>? {
        val myArrayList: ArrayList<IncomingFlight> = ArrayList()
        for (i in 1..30) {
            myArrayList.add(
                IncomingFlight(
                    "actualTime $i",
                    "aircraft $i",
                    "airline $i",
                    "destination $i",
                    "flightNumber $i",
                    "origin $i",
                    "scheduledTime $i",
                    i,
                    i
                )
            )
        }
        return myArrayList
    }

    /*fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANEL_ID,
                CHANEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

        }
    }*/

    override fun onStop() {
        super.onStop()
        /*requireContext().unregisterReceiver(receiver)*/
    }
}
