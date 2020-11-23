package com.thetechannel.android.planit.data.source.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RemoteService {
    @GET("get-all-categories.php")
    fun getCategories(): Deferred<List<Category>>

    @GET("get-all-task-methods.php")
    fun getTaskMethods(): Deferred<List<TaskMethod>>

    @GET("get-all-tasks.php")
    fun getTasks(): Deferred<List<Task>>

    @POST("insert-category.php")
    fun insertCategory(@Body category: Category) : Deferred<Void>
}

object Network {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://192.168.43.171/planit/api/")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val remoteService = retrofit.create(RemoteService::class.java)
}