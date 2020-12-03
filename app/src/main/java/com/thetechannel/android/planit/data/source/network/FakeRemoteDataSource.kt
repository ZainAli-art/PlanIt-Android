package com.thetechannel.android.planit.data.source.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppDataSource
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.HashMap

object FakeRemoteDataSource : AppDataSource {
    private const val NETWORK_LATENCY_IN_MILLIS: Long = 2000L

    private val categoryServiceData = HashMap<Int, Category>()
    private val methodServiceData = HashMap<Int, TaskMethod>()
    private val taskServiceData = HashMap<String, Task>()

    override fun observeCategories(): LiveData<Result<List<Category>>> {
        TODO("Not yet implemented")
    }

    override fun observeCategory(id: Int): LiveData<Result<Category>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskMethods(): LiveData<Result<List<TaskMethod>>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskMethod(id: Int): LiveData<Result<TaskMethod>> {
        TODO("Not yet implemented")
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override fun observeTasks(day: Date): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override fun observeTask(id: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskDetails(): LiveData<Result<List<TaskDetail>>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskDetail(id: String): LiveData<Result<TaskDetail>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(): Result<List<Category>> {
        delay(NETWORK_LATENCY_IN_MILLIS)
        return Result.Success(categoryServiceData.values.toList())
    }

    override suspend fun getCategory(id: Int): Result<Category> {
        val category =
            categoryServiceData[id] ?: return Result.Error(Exception("id does not exist"))
        return Result.Success(category)
    }

    override suspend fun getTaskMethods(): Result<List<TaskMethod>> {
        return Result.Success(methodServiceData.values.toList())
    }

    override suspend fun getTaskMethod(id: Int): Result<TaskMethod> {
        val method = methodServiceData[id] ?: return Result.Error(Exception("id does not exist"))
        return Result.Success(method)
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return Result.Success(taskServiceData.values.toList())
    }

    override suspend fun getTasks(day: Date): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(id: String): Result<Task> {
        val task = taskServiceData[id] ?: return Result.Error(Exception("task id not found"))
        return Result.Success(task)
    }

    override suspend fun getTaskDetails(): Result<List<TaskDetail>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskDetail(id: String): Result<TaskDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun saveCategory(category: Category) {
        categoryServiceData[category.id] = category
    }

    override suspend fun saveTaskMethod(taskMethod: TaskMethod) {
        methodServiceData[taskMethod.id] = taskMethod
    }

    override suspend fun saveTask(task: Task) {
        taskServiceData[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCategories() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTaskMethods() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }
}