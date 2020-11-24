package com.thetechannel.android.planit.data.source.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RemoteService {
    @GET("get-all-categories.php")
    fun getCategories(): Deferred<List<NetworkCategory>>

    @GET("get-all-task-methods.php")
    fun getTaskMethods(): Deferred<List<NetworkTaskMethod>>

    @GET("get-all-tasks.php")
    fun getTasks(): Deferred<List<NetworkTask>>

    @FormUrlEncoded
    @POST
    fun getTask(@Query("id") id: String): Deferred<NetworkTask>

    @POST("insert-category.php")
    fun insertCategory(@Body category: NetworkCategory): Deferred<Void>

    @POST("insert-task.php")
    fun insertTask(@Body task: NetworkTask): Deferred<Void>
}

object Network {
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.43.171/planit/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val remoteService = retrofit.create(RemoteService::class.java)
}