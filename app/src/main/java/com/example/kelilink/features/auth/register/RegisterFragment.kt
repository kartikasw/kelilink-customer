package com.example.kelilink.features.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.kelilink.R
import com.example.kelilink.core.data.helper.Constants
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_EMAIL
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.databinding.FragmentRegisterBinding
import com.example.kelilink.util.costum_view.KelilinkLoadingDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var loading: KelilinkLoadingDialog

    private lateinit var emailVerification : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = KelilinkLoadingDialog(requireContext())

        setOnClickListener()
    }

    private fun register() {
        val name = binding.rEtName
        val phoneNumber = binding.rEtPhoneNumber
        val email = binding.rEtEmail
        val password = binding.rEtPassword
        val passwordConfirmation = binding.rEtPasswordConfirmation

        val nameData = name.text.toString()
        val phoneNumberData = phoneNumber.text.toString()
        val emailData = email.text.toString()
        val passwordData = password.text.toString()
        val passwordConfirmationData = passwordConfirmation.text.toString()
        val token = viewModel.getFcmToken()

        if(
            name.error == null && phoneNumber.error == null && email.error == null
            && password.error == null && nameData.isNotEmpty() && phoneNumberData.isNotEmpty()
            && emailData.isNotEmpty() && passwordData.isNotEmpty() && token.isNotEmpty()
            && passwordConfirmation.error == null
        ) {
            if(passwordData == passwordConfirmationData) {
                emailVerification = emailData
                val user = mutableMapOf(
                    Constants.DatabaseColumn.EMAIL_COLUMN to emailData,
                    Constants.DatabaseColumn.NAME_COLUMN to nameData,
                    Constants.DatabaseColumn.PHONE_NUMBER_COLUMN to phoneNumberData as Any,
                    Constants.DatabaseColumn.FCM_TOKEN_COLUMN to token
                )

                viewModel
                    .register(emailData, passwordData, user)
                    .observe(viewLifecycleOwner, ::registerResponse)
            } else {
                val errorText = requireContext().resources.getString(R.string.error_password_confirmation)
                passwordConfirmation.error = errorText
            }
        } else {
            Snackbar.make(binding.root, requireContext().resources.getString(R.string.error_field), Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun registerResponse(data: Resource<Unit>) {
        when(data) {
            is Resource.Success -> {
                loading.dismiss()
                val intent = Intent(requireContext(), VerifyEmailActivity::class.java)
                intent.putExtra(EXTRA_EMAIL, emailVerification)
                startActivity(intent).also {
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

    private fun setOnClickListener() {
        with(binding) {
            lBtnRegister.setOnClickListener { register() }

            lBtnLogin.setOnClickListener {
                it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "RegisterFragment"
    }

}