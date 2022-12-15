package com.example.kelilink.features.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.kelilink.features.MainActivity
import com.example.kelilink.features.auth.AuthActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }

        if(Firebase.auth.currentUser?.uid != null) {
            startActivity(Intent(this, MainActivity::class.java)).also {
                finishAffinity()
            }
        } else {
            startActivity(Intent(this, AuthActivity::class.java)).also {
                finishAffinity()
            }
        }
    }


}