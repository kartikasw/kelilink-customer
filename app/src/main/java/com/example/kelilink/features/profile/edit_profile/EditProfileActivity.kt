package com.example.kelilink.features.profile.edit_profile

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kelilink.R
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.NAME_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.PHONE_NUMBER_COLUMN
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_USER_NAME
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_USER_PHONE_NUMBER
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.databinding.ActivityEditProfileFormBinding
import com.example.kelilink.features.profile.ProfileActivity
import com.example.kelilink.util.costum_view.KelilinkLoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileFormBinding

    private val viewModel: EditProfileViewModel by viewModels()

    private lateinit var loading: KelilinkLoadingDialog

    private lateinit var userName: String

    private lateinit var userPhoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra(EXTRA_USER_NAME)!!
        userPhoneNumber = intent.getStringExtra(EXTRA_USER_PHONE_NUMBER)!!

        loading = KelilinkLoadingDialog(this)

        setUpToolbar()

        setUpView()

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.lBtnSave.setOnClickListener {
            with(binding) {
                val name = epEtField1
                val phoneNumber = epEtField2
                val nameData = name.text.toString()
                val phoneNumberData = phoneNumber.text.toString()

                if(name.error == null && phoneNumber.error == null
                    && nameData.isNotEmpty() && phoneNumberData.isNotEmpty()) {
                    viewModel.updateMyProfile(
                        mutableMapOf(
                            NAME_COLUMN to nameData,
                            PHONE_NUMBER_COLUMN to phoneNumberData
                        )
                    )
                        .observe(this@EditProfileActivity, ::response)
                } else {
                    Toast.makeText(this@EditProfileActivity, resources.getString(R.string.error_field), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun response(resource: Resource<Unit>) {
        when(resource) {
            is Resource.Success -> {
                loading.dismiss()
                val intent = Intent(this, ProfileActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            is Resource.Loading -> {
                loading.show()
            }
            is Resource.Error -> {
                loading.dismiss()
                Toast.makeText(this, resource.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.epToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_edit_profile)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setUpView() {
        with(binding) {
            epEtField1.hint = resources.getString(R.string.hint_name)
            epEtField1.setText(userName)
            epEtField1.inputType = InputType.TYPE_CLASS_TEXT
            epEtField2.hint = resources.getString(R.string.hint_phone_number)
            epEtField2.setText(userPhoneNumber)
            epEtField2.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}