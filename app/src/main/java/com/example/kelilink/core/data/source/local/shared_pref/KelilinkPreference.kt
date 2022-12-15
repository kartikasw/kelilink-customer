package com.example.kelilink.core.data.source.local.shared_pref

import android.content.Context
import android.content.SharedPreferences
import com.example.kelilink.core.data.helper.Constants.PREFERENCE_NAME
import com.example.kelilink.core.data.helper.Constants.PreferenceValue.FCM_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KelilinkPreference @Inject constructor(@ApplicationContext context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val editor = pref.edit()

    fun setFcmToken(token: String) {
        editor.putString(FCM_TOKEN, token)
        editor.apply()
    }

    fun getFcmToken(): String? =
        pref.getString(FCM_TOKEN, "")

}