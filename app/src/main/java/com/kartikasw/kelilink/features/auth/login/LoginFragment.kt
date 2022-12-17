package com.kartikasw.kelilink.features.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.kartikasw.kelilink.R
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.databinding.FragmentLoginBinding
import com.kartikasw.kelilink.features.MainActivity
import com.kartikasw.kelilink.features.auth.reset_password.ResetPasswordActivity
import com.kartikasw.kelilink.util.costum_view.KelilinkLoadingDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var loading: KelilinkLoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = KelilinkLoadingDialog(requireContext())

        setOnClickListener()
    }

    private fun setOnClickListener() {
        with(binding) {
            lBtnRegister.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            lBtnLogin.setOnClickListener {
                login()
            }

            lBtnForgotPassword.setOnClickListener {
                startActivity(
                    Intent(requireContext(), ResetPasswordActivity::class.java)
                )
            }
        }
    }

    private fun login() {
        val email = binding.lEtEmail
        val password = binding.lEtPassword
        val emailData = email.text.toString()
        val passwordData = password.text.toString()
        val token = viewModel.getFcmToken()

        if(email.error == null && password.error == null && token.isNotEmpty()
            && emailData.isNotEmpty() && passwordData.isNotEmpty()) {

            viewModel.logIn(
                emailData,
                passwordData,
                token
            ).observe(viewLifecycleOwner, ::loginResponse)
        } else {
            Snackbar.make(binding.root, requireContext().resources.getString(R.string.error_field), Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun loginResponse(data: Resource<Unit>) {
        when(data) {
            is Resource.Success -> {
                loading.dismiss()
                startActivity(Intent(requireContext(), MainActivity::class.java)).also {
                    requireActivity().finish()
                }
            }
            is Resource.Loading -> {
                loading.show()
            }
            is Resource.Error -> {
                loading.dismiss()
                Log.e(TAG, data.message.toString())
                Snackbar.make(binding.root, data.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val TAG = "LoginFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}