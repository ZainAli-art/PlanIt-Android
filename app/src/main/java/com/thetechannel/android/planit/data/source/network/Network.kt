package com.thetechannel.android.planit.data.source.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface RemoteService {

}

object Network {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://localhost/planit/")
        .build()

    val remoteService = retrofit.create(RemoteService::class.java)
}