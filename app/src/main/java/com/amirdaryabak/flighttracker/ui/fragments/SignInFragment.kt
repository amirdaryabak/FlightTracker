package com.amirdaryabak.flighttracker.ui.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amirdaryabak.flighttracker.ui.viewmodels.MainViewModel
import com.amirdaryabak.flighttracker.R
import com.amirdaryabak.flighttracker.api.Resource
import com.amirdaryabak.flighttracker.other.Constants.USER_NAME
import com.amirdaryabak.flighttracker.other.Constants.USER_TOKEN
import com.amirdaryabak.flighttracker.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.et_password_sign_in
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    lateinit var loading: Dialog

    val TAG = "SignInFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = showLoading(requireContext())

        btn_sing_up.setOnClickListener {
            if (validateUserInput()) {
                Toasty.error(requireContext(), getString(R.string.fill_all_fields)).show()
            } else {
                singInUser()
            }
        }
        setUpSingUpObserver()
    }

    private fun singInUser() {
        sharedPref.edit().putString(USER_NAME, et_name_sign_in.text.toString()).apply()
        sharedPref.edit().putString(USER_TOKEN, "token").apply()
        findNavController().navigate(R.id.action_signInFragment_to_searchFragment)

//        viewModel.signIn(UserInformation(et_name_sign_in.text.toString(), et_password_sign_in.text.toString()))
    }

    private fun validateUserInput(): Boolean {
        return et_name_sign_in.text.toString().trim().isEmpty() || et_password_sign_in.text.toString().trim().isEmpty()
    }

    private fun setUpSingUpObserver() {
        viewModel.signIn.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let { result ->
                        sharedPref.edit().putString(USER_NAME, et_name_sign_in.text.toString()).apply()
                        sharedPref.edit().putString(USER_TOKEN, result.token).apply()
                        Toasty.success(requireContext(), result.result).show()
                        findNavController().navigate(R.id.action_signInFragment_to_searchFragment)
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
