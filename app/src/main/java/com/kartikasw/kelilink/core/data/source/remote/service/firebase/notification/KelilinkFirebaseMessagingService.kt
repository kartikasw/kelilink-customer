package com.kartikasw.kelilink.core.data.source.remote.service.firebase.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kartikasw.kelilink.R
import com.kartikasw.kelilink.core.data.helper.Constants.CHANNEL_ID
import com.kartikasw.kelilink.core.data.helper.Constants.CHANNEL_NAME
import com.kartikasw.kelilink.core.data.source.local.shared_pref.KelilinkPreference
import com.kartikasw.kelilink.features.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class KelilinkFirebaseMessagingService: FirebaseMessagingService() {

    companion object {
        const val TAG = "FMService"

        private var sharedPref: KelilinkPreference? = null

        var token: String? = null
            set(value) {
                sharedPref?.setFcmToken(token!!)
                field = value
            }
    }

    override fun onNewToken(newToken: String) {
        Log.d(TAG, "Refreshed token: $token")
        token = newToken
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, message.toString())
        sendNotification(message.data["store_name"])
    }

    private fun sendNotification(messageBody: String?) {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)

        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            contentIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_notification)
            .setContentTitle("Pesananmu sudah siap")
            .setContentText("Yuk, segera ambil pesanan mu di $messageBody")
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH)
            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }
}