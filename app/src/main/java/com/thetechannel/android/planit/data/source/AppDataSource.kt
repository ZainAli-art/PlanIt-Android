package com.thetechannel.android.planit.data.source

import androidx.lifecycle.LiveData
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import java.util.*

interface AppDataSource {
    fun observeAllCategories(): LiveData<Result<List<Category>>>

    fun observeCategoryById(id: Int): LiveData<Result<Category>>

    fun observeAllTaskMethods(): LiveData<Result<List<TaskMethod>>>

    fun observeTaskMethodById(id: Int): LiveData<Result<TaskMethod>>

    fun observeAllTasks(): LiveData<Result<List<Task>>>

    fun observeTaskById(id: String): LiveData<Result<Task>>

    fun observeTaskByDay(day: Date): LiveData<Result<List<Task>>>

    fun observeTaskDetailsByTaskId(id: String): LiveData<Result<TaskDetail>>

    suspend fun getAllCategories(): Result<List<Category>>

    suspend fun getCategoryById(id: Int): Result<Category?>

    suspend fun getAllTaskMethods(): Result<List<TaskMethod>>

    suspend fun getTaskMethodById(id: Int): Result<TaskMethod?>

    suspend fun getTaskById(id: String): Result<Task?>

    suspend fun getTasksByDay(day: Date): Result<List<Task>>

    suspend fun getAllTasks(): Result<List<Task>>

    suspend fun getTaskDetailsByTaskId(id: String): Result<TaskDetail>

    suspend fun insertCategory(category: Category)

    suspend fun insertCategories(vararg categories: Category)

    suspend fun insertTaskMethod(taskMethod: TaskMethod)

    suspend fun insertTaskMethods(vararg taskMethods: TaskMethod)

    suspend fun insertTask(task: Task)

    suspend fun insertTasks(vararg tasks: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(id: String)

    suspend fun deleteCategory(category: Category)

    suspend fun deleteTaskMethod(taskMethod: TaskMethod)

    suspend fun deleteTask(task: Task)
}