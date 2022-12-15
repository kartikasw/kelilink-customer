package com.example.kelilink.features.profile.edit_password

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kelilink.R
import com.example.kelilink.core.data.source.remote.response.UserResponse
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.databinding.ActivityEditProfileFormBinding
import com.example.kelilink.features.profile.ProfileActivity
import com.example.kelilink.util.costum_view.KelilinkLoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileFormBinding

    private val viewModel: EditPasswordViewModel by viewModels()

    private lateinit var loading: KelilinkLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = KelilinkLoadingDialog(this)

        setUpToolbar()

        setUpView()

        setOnClickListener()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.epToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_edit_password)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setUpView() {
        with(binding) {
            epEtField1.hint = resources.getString(R.string.hint_password_old)
            epEtField1.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            epEtField1.transformationMethod = PasswordTransformationMethod.getInstance()
            epEtField2.hint = resources.getString(R.string.hint_password_new)
            epEtField2.transformationMethod = PasswordTransformationMethod.getInstance()
            epEtField2.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun setOnClickListener() {
       binding.lBtnSave.setOnClickListener {
           with(binding) {
               val oldPassword = epEtField1
               val newPassword = epEtField2
               val oldPasswordData = oldPassword.text.toString()
               val newPasswordData = newPassword.text.toString()

               if(oldPassword.error == null && newPassword.error == null
                   && oldPasswordData.isNotEmpty() && newPasswordData.isNotEmpty()) {
                   viewModel.updatePassword(oldPasswordData, newPasswordData)
                       .observe(this@EditPasswordActivity, ::response)
               } else {
                   Toast.makeText(this@EditPasswordActivity, resources.getString(R.string.error_field), Toast.LENGTH_SHORT).show()
               }
           }
       }
    }

    private fun response(resource: Resource<UserResponse>) {
        when(resource) {
            is Resource.Success -> {
                loading.dismiss()
                if(resource.data == null) {
                    Toast.makeText(this, "Gagal ganti kata sandi", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}