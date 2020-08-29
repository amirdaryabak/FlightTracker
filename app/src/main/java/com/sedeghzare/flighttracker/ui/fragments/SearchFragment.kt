package com.sedeghzare.flighttracker.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sedeghzare.flighttracker.ui.viewmodels.MainViewModel
import com.sedeghzare.flighttracker.R
import com.sedeghzare.flighttracker.adapters.SearchAdapter
import com.sedeghzare.flighttracker.adapters.ViewPagerAdapter
import com.sedeghzare.flighttracker.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var loading: Dialog

    val TAG = "SearchFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = showLoading(requireContext())

        setUpViewPagerAdapter()

        setUpTabLayoutMediator(createFragmentNameList(), tabLayout, viewPager)
        
    }

    private fun setUpTabLayoutMediator(
        fragmentNameList: java.util.ArrayList<String>,
        tabLayout: TabLayout,
        viewPager: ViewPager2
    ) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentNameList[position]
        }.attach()
    }

    private fun setUpViewPagerAdapter() {
        viewPager.adapter = ViewPagerAdapter(
            createFragmentsList(),
            childFragmentManager,
            lifecycle
        )
    }

    private fun createFragmentNameList(): ArrayList<String> {
        return arrayListOf(
            "By Airport",
            "By FlightNumber",
            "By Route"
        )
    }

    private fun createFragmentsList(): ArrayList<Fragment> {
        return arrayListOf(
            SearchByAirportFragment(),
            SearchByFlightNumberFragment(),
            SearchByRouteFragment()
        )
    }

}
