package com.amirdaryabak.flighttracker.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.amirdaryabak.flighttracker.R
import com.amirdaryabak.flighttracker.other.Constants
import com.amirdaryabak.flighttracker.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        /*navigateToUerFlightsFragment(intent)*/

        /*val radius = resources.getDimension(R.dimen.radius_small)
        val bottomNavigationViewBackground = bottomNavigationView.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()*/
        bottomNavigationView.setupWithNavController(nav_host_fragment.findNavController())
        bottomNavigationView.setOnNavigationItemReselectedListener { /* NO-OP */ }

        nav_host_fragment.findNavController()
            .addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.id) {
                    R.id.signInFragment, R.id.logInFragment -> {
                        bottomNavigationView.visibility = View.GONE
                        txt_log_out.visibility = View.GONE

                        // full-screen App
                        /*window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                                // Set the content to appear under the system bars so that the
                                // content doesn't resize when the system bars hide and show.
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                // Hide the nav bar and status bar
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_FULLSCREEN)*/
                    }
                    R.id.userFlightsFragment -> {
                        if (sharedPref.getString(Constants.USER_TOKEN, "") != "")
                            txt_log_out.visibility = View.VISIBLE
                        else
                            txt_log_out.visibility = View.GONE
                    }
                    else -> {
                        // normal-screen App
                        /*window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)*/

                        bottomNavigationView.visibility = View.VISIBLE
                        txt_log_out.visibility = View.GONE
                    }
                }
            }

        txt_log_out.setOnClickListener {
            viewModel.deleteAllIncomingFlight()
            viewModel.deleteAllOutgoingFlight()
            sharedPref.edit().putString(Constants.USER_TOKEN, "").apply()
            nav_host_fragment.findNavController().navigate(R.id.action_global_logInFragment)
        }

    }


    /*override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToUerFlightsFragment(intent)
    }

    private fun navigateToUerFlightsFragment(intent: Intent?) {
        nav_host_fragment.findNavController().navigate(R.id.action_global_userFlightsFragment)
    }*/

}