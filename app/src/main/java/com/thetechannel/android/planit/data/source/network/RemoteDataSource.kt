package com.thetechannel.android.planit.data.source.network

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppDataSource
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.util.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

private const val TAG = "RemoteDataSource"

object RemoteDataSource : AppDataSource {
    private val db = Firebase.firestore
    private val categoriesCollection = db.collection("categories")
    private val taskMethodsCollection = db.collection("taskMethods")
    private val tasksCollection = db.collection("tasks")

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

    override suspend fun getCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        Log.d(TAG, "getCategories: called")
        return@withContext try {
            val categories = categoriesCollection.get().await()
                .toObjects(NetworkCategory::class.java)
            Log.d(TAG, "fetched categoreis: $categories")
            Result.Success(categories.map { it.asDomainModel() })
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    override suspend fun getCategory(id: Int): Result<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskMethods(): Result<List<TaskMethod>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val methods = taskMethodsCollection.get().await()
                .toObjects(NetworkTaskMethod::class.java)
            Result.Success(methods.map { it.asDomainModel() })
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    override suspend fun getTaskMethod(id: Int): Result<TaskMethod> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val tasks = tasksCollection.get().await().toObjects(NetworkTask::class.java)
            Result.Success(tasks.map { it.asDomainModel() })
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    override suspend fun getTasks(day: Date): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(id: String): Result<Task> = withContext(Dispatchers.IO) {
        return@withContext try {
            val task = tasksCollection.document(id).get().await()
                .toObject(NetworkTask::class.java)
            task?.let {
                return@withContext Result.Success(it.asDomainModel())
            }
            Result.Error(Exception("Task with requested id not found"))
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    override suspend fun getTaskDetails(): Result<List<TaskDetail>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskDetail(id: String): Result<TaskDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun saveCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTaskMethod(taskMethod: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        try {
            withContext(Dispatchers.IO) {
                tasksCollection.document(task.id).set(task).await()
            }
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