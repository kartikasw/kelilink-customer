package com.kartikasw.kelilink.core.data.source.remote.service.firebase.notification

import com.kartikasw.kelilink.core.data.helper.Constants.CONTENT_TYPE
import com.kartikasw.kelilink.core.data.helper.Constants.SERVER_KEY
import com.kartikasw.kelilink.core.data.source.remote.response.FcmResponse
import com.kartikasw.kelilink.core.domain.model.Fcm
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun sendFcm(
        @Body data: Fcm
    ): FcmResponse
}