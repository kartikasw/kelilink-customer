package com.example.kelilink.features.auth.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Spanned
import com.example.kelilink.R
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_EMAIL
import com.example.kelilink.databinding.ActivityVerifyEmailBinding
import com.example.kelilink.features.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class VerifyEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyEmailBinding
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra(EXTRA_EMAIL)!!

        setOnClickListener()

        val text = resources.getString(R.string.content_verify_email, email)
        val styledText: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
        binding.veTvContent.text = styledText
    }

    private fun setOnClickListener() {
        binding.veBtnMove.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java)).also {
                finish()
            }
        }
    }
}