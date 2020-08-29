package com.sedeghzare.flighttracker.ui.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sedeghzare.flighttracker.ui.viewmodels.MainViewModel
import com.sedeghzare.flighttracker.R
import com.sedeghzare.flighttracker.api.Resource
import com.sedeghzare.flighttracker.models.UserInformation
import com.sedeghzare.flighttracker.other.Constants
import com.sedeghzare.flighttracker.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_log_in.*
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : Fragment(R.layout.fragment_log_in) {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    lateinit var loading: Dialog

    val TAG = "LogInFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = showLoading(requireContext())

        if (isTokenSet()) {
            findNavController().navigate(R.id.action_logInFragment_to_searchFragment)
            return
        } else if (isUserSkippedLogin()) {
            findNavController().navigate(R.id.action_logInFragment_to_searchFragment)
            return
        }


        btn_log_in.setOnClickListener {
            if (et_name_log_in.text.toString().trim().isEmpty() ||
                et_password_sign_in.text.toString().trim().isEmpty()) {
                Toasty.error(requireContext(), getString(R.string.fill_all_fields)).show()
            } else {
                loginUser()
            }
        }

        setUpLoginObserver()

        txt_skip_for_now.setOnClickListener {
            sharedPref.edit().putBoolean(Constants.IS_SKIPPED, true).apply()
            findNavController().navigate(R.id.action_logInFragment_to_searchFragment)
        }

        txt_sign_up.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signInFragment)
        }

    }

    private fun loginUser() {
        sharedPref.edit().putString(Constants.USER_NAME, et_password_sign_in.text.toString()).apply()
        sharedPref.edit().putString(Constants.USER_TOKEN, "token").apply()
        findNavController().navigate(R.id.action_logInFragment_to_searchFragment)

//        viewModel.login(UserInformation(et_name_log_in.text.toString(), et_password_sign_in.text.toString()))
    }

    private fun isUserSkippedLogin(): Boolean {
        return sharedPref.getBoolean(Constants.IS_SKIPPED, false)
    }

    private fun isTokenSet(): Boolean {
        return sharedPref.getString(Constants.USER_TOKEN, "") != ""
    }

    private fun setUpLoginObserver() {
        viewModel.login.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let { result ->
                        sharedPref.edit().putString(Constants.USER_NAME, et_password_sign_in.text.toString()).apply()
                        sharedPref.edit().putString(Constants.USER_TOKEN, result.token).apply()
                        Toasty.success(requireContext(), result.result).show()
                        findNavController().navigate(R.id.action_logInFragment_to_searchFragment)
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

}
