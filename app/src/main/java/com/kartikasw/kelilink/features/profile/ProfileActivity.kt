package com.kartikasw.kelilink.features.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kartikasw.kelilink.R
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_USER_NAME
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_USER_PHONE_NUMBER
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.User
import com.kartikasw.kelilink.databinding.ActivityProfileBinding
import com.kartikasw.kelilink.features.auth.AuthActivity
import com.kartikasw.kelilink.features.profile.edit_password.EditPasswordActivity
import com.kartikasw.kelilink.features.profile.edit_profile.EditProfileActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var name: String
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        showProfileInfo()

        setOnClickListener()
    }

    private fun showProfileInfo() {
        viewModel.getMyProfile().observe(this) {
            when(it) {
                is Resource.Success -> {
                    if(it.data != null) {
                        name = it.data.name
                        phoneNumber = it.data.phone_number
                        setUpProfileView(it.data)
                        showLoadingState(false)
                        showProfileInfo(true)
                    }
                }
                is Resource.Loading -> {
                    showProfileInfo(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showProfileInfo(false)
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
            }

        }
    }

    private fun setUpProfileView(user: User) {
        with(binding.pLayoutProfile) {
            cpTvName.text = user.name
            cpTvEmail.text = user.email
            cpTvPhoneNumber.text = user.phone_number
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.pLoading.root.isVisible = state
    }

    private fun showProfileInfo(state: Boolean) {
        if(state) {
            binding.pLayoutProfile.root.visibility = View.VISIBLE
        } else {
            binding.pLayoutProfile.root.visibility = View.INVISIBLE
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.pToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_profile)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            pBtnEditProfile.setOnClickListener {
                if(name.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(this@ProfileActivity, "Tunggu sampai loading selesai", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
                    intent
                        .putExtra(EXTRA_USER_NAME, name)
                        .putExtra(EXTRA_USER_PHONE_NUMBER, phoneNumber)
                    startActivity(intent)
                }
            }

            pBtnEditPassword.setOnClickListener {
                startActivity(
                    Intent(this@ProfileActivity, EditPasswordActivity::class.java)
                )
            }

            pBtnReport.setOnClickListener {
                val message = resources.getString(R.string.placeholder_whatsapp_message)
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse("http://api.whatsapp.com/send?phone=+6281352247312&text=$message")
                }

                startActivity(sendIntent)
            }

            pBtnLogout.setOnClickListener {
                MaterialAlertDialogBuilder(this@ProfileActivity)
                    .setTitle(resources.getString(R.string.title_logout))
                    .setMessage(resources.getString(R.string.content_logout))
                    .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.btn_logout)) { _, _ ->
                        viewModel.logout()
                        val intent = Intent(this@ProfileActivity, AuthActivity::class.java)
                        startActivity(intent).also {
                            finishAffinity()
                        }
                    }
                    .show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "ProfileActivity"
    }
}