package com.thetechannel.android.planit.data.source.network

import android.util.Log
import androidx.lifecycle.LiveData
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppDataSource
import com.thetechannel.android.planit.data.source.database.TasksOverView
import com.thetechannel.android.planit.data.source.database.TodayProgress
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.util.asDataTransferObject
import com.thetechannel.android.planit.util.asDomainModel
import java.util.*

private const val TAG = "RemoteDataSource"

class RemoteDataSource(private val remoteService: RemoteService) : AppDataSource {

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

    override fun observeTasksOverView(): LiveData<Result<TasksOverView>> {
        TODO("Not yet implemented")
    }

    override fun observeTodayProgress(): LiveData<Result<TodayProgress>> {
        TODO("Not yet implemented")
    }

    override fun observeTodayPieEntries(): LiveData<Result<List<PieEntry>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val categories = remoteService.getCategories().await()
            Result.Success(categories.map { it.asDomainModel() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCategory(id: Int): Result<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskMethods(): Result<List<TaskMethod>> {
        return try {
            val methods = remoteService.getTaskMethods().await()
            Result.Success(methods.map { it.asDomainModel() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTaskMethod(id: Int): Result<TaskMethod> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return try {
            val tasks = remoteService.getTasks().await()
            Result.Success(tasks.map { it.asDomainModel() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTasks(day: Date): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(id: String): Result<Task> {
        return try {
            val task = remoteService.getTask(id).await()
            Result.Success(task.asDomainModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTaskDetails(): Result<List<TaskDetail>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskDetail(id: String): Result<TaskDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksOverView(): Result<TasksOverView> {
        TODO("Not yet implemented")
    }

    override suspend fun getTodayProgress(): Result<TodayProgress> {
        TODO("Not yet implemented")
    }

    override suspend fun getTodayPieEntries(): Result<List<PieEntry>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveCategory(category: Category) {
        remoteService.insertCategory(category.asDataTransferObject()).await()
    }

    override suspend fun saveTaskMethod(taskMethod: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        Log.d(TAG, "saveTask: $task")
        try {
            remoteService.insertTask(task.asDataTransferObject()).await()
        } catch (e: Exception) {
            Log.e(TAG, "saveTask: error ${e.message}")
        }
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