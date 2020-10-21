package com.thetechannel.android.planit.data.source.network

import com.thetechannel.android.planit.data.Result;
import java.util.*

interface NetworkDataSource {
    suspend fun getAllCategories(): Result<List<NetworkCategory>>

    suspend fun getCategoryById(id: Int): Result<NetworkCategory>

    suspend fun getAllTaskMethods(): Result<List<NetworkTaskMethod>>

    suspend fun getTaskMethodById(): Result<NetworkTaskMethod>

    suspend fun getAllTasks(): Result<List<NetworkTask>>

    suspend fun getTaskById(id: Int): Result<NetworkTask>

    suspend fun getTaskByDay(day: Date): Result<NetworkTask>

    suspend fun insertCategory(category: NetworkCategory)

    suspend fun insertTaskMethod(taskMethod: NetworkTaskMethod)

    suspend fun insertTask(task: NetworkTask)

    suspend fun deleteTask(task: NetworkTask)
}