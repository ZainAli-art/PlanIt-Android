package com.thetechannel.android.planit.data.source

import androidx.lifecycle.LiveData
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import java.util.*

interface AppRepository {
    fun observeCategories(): LiveData<Result<List<Category>>>

    fun observeCategory(id: Int): LiveData<Result<Category>>

    fun observeTaskMethods(): LiveData<Result<List<TaskMethod>>>

    fun observeTaskMethod(id: Int): LiveData<Result<TaskMethod>>

    fun observeTasks(): LiveData<Result<List<Task>>>

    fun observeTasks(day: Date): LiveData<Result<List<Task>>>

    fun observeTask(id: String): LiveData<Result<Task>>

    fun observeTaskDetails(): LiveData<Result<List<TaskDetail>>>

    fun observeTaskDetail(id: String): LiveData<Result<TaskDetail>>

    suspend fun getCategories(forceUpdate: Boolean): Result<List<Category>>

    suspend fun getCategory(id: Int, forceUpdate: Boolean): Result<Category>

    suspend fun getTaskMethods(forceUpdate: Boolean): Result<List<TaskMethod>>

    suspend fun getTaskMethod(id: Int, forceUpdate: Boolean): Result<TaskMethod>

    suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>>

    suspend fun getTasks(day: Date, forceUpdate: Boolean): Result<List<Task>>

    suspend fun getTask(id: String, forceUpdate: Boolean): Result<Task>

    suspend fun getTaskDetails(forceUpdate: Boolean): Result<List<TaskDetail>>

    suspend fun getTaskDetail(id: String, forceUpdate: Boolean): Result<TaskDetail>

    suspend fun saveCategory(category: Category)

    suspend fun saveCategories(vararg categories: Category)

    suspend fun saveTaskMethod(taskMethod: TaskMethod)

    suspend fun saveTaskMethods(vararg taskMethods: TaskMethod)

    suspend fun saveTask(task: Task)

    suspend fun saveTasks(vararg tasks: Task)

    suspend fun refreshCategories()

    suspend fun refreshTaskMethods()

    suspend fun refreshTasks()

    suspend fun completeTask(task: Task)

    suspend fun completeTask(id: String)

    suspend fun deleteCategory(category: Category)

    suspend fun deleteTaskMethod(taskMethod: TaskMethod)

    suspend fun deleteTask(task: Task)
}