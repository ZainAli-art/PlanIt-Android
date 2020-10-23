package com.thetechannel.android.planit.data.source

import androidx.lifecycle.LiveData
import com.thetechannel.android.planit.data.Result;
import com.thetechannel.android.planit.data.source.database.DbCategory
import com.thetechannel.android.planit.data.source.database.DbTask
import com.thetechannel.android.planit.data.source.database.DbTaskDetail
import com.thetechannel.android.planit.data.source.database.DbTaskMethod

interface AppDataSource {
    fun observeAllTaskMethods(): LiveData<Result<List<DbTaskMethod>>>

    fun observeTaskMethodById(id: Int): LiveData<Result<DbTaskMethod>>

    fun observeAllTasks(): LiveData<Result<List<DbTask>>>

    fun observeTaskById(id: String): LiveData<Result<DbTask>>

    fun observeTaskByDay(day: Long): LiveData<Result<List<DbTask>>>

    fun observeTaskDetailsByTaskId(id: String): LiveData<Result<DbTaskDetail>>

    fun observeAllCategories(): LiveData<Result<List<DbCategory>>>

    fun observeCategoryById(id: Int): LiveData<Result<DbCategory>>

    suspend fun getAllTaskMethods(): Result<List<DbTaskMethod>>

    suspend fun getTaskMethodById(id: Int): Result<DbTaskMethod?>

    suspend fun getTaskById(id: String): Result<DbTask?>

    suspend fun getTaskByDay(day: Long): Result<List<DbTask>>

    suspend fun getAllTasks(): Result<List<DbTask>>

    suspend fun getTaskDetailsByTaskId(id: String): Result<DbTaskDetail>

    suspend fun getAllCategories(): Result<List<DbCategory>>

    suspend fun getCategoryById(id: Int): Result<DbCategory?>

    suspend fun insertTaskMethod(taskMethod: DbTaskMethod)

    suspend fun insertTask(task: DbTask)

    suspend fun insertCategory(category: DbCategory)

    suspend fun insertAllCategories(vararg categories: DbCategory)

    suspend fun updateTaskMethod(taskMethod: DbTaskMethod)

    suspend fun deleteTaskMethod(taskMethod: DbTaskMethod)

    suspend fun deleteTask(task: DbTask)

    suspend fun deleteCategory(category: DbCategory)
}