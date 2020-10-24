package com.thetechannel.android.planit.data.source.database

import androidx.lifecycle.LiveData
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppDataSource
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.util.asDatabaseEntity
import com.thetechannel.android.planit.util.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LocalDataSource(
    private val categoryDao: CategoriesDao,
    private val taskMethodsDao: TaskMethodsDao,
    private val tasksDao: TasksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppDataSource {
    override fun observeAllCategories(): LiveData<Result<List<Category>>> {
        TODO("Not yet implemented")
    }

    override fun observeCategoryById(id: Int): LiveData<Result<Category>> {
        TODO("Not yet implemented")
    }

    override fun observeAllTaskMethods(): LiveData<Result<List<TaskMethod>>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskMethodById(id: Int): LiveData<Result<TaskMethod>> {
        TODO("Not yet implemented")
    }

    override fun observeAllTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskById(id: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskByDay(day: Long): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskDetailsByTaskId(id: String): LiveData<Result<TaskDetail>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCategories(): Result<List<Category>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(
                categoryDao.getAll().map {
                    it.asDomainModel()
                }
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCategoryById(id: Int): Result<Category?> = withContext(ioDispatcher) {
        return@withContext try {
            val dbCategory = categoryDao.getById(id)
            if (dbCategory != null) Result.Success(dbCategory.asDomainModel())
            else                    Result.Error(Exception("category id not found"))
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun getAllTaskMethods(): Result<List<TaskMethod>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskMethodById(id: Int): Result<TaskMethod?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(id: String): Result<Task?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskByDay(day: Long): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTasks(): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskDetailsByTaskId(id: String): Result<TaskDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCategory(category: Category) = withContext(ioDispatcher) {
        categoryDao.insert(category.asDatabaseEntity())
    }

    override suspend fun insertCategories(vararg categories: Category) = withContext(ioDispatcher) {
        for (category in categories) {
            categoryDao.insert(category.asDatabaseEntity())
        }
    }

    override suspend fun insertTaskMethod(taskMethod: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun insertTask(task: DbTask) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskMethod(taskMethod: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: DbTask) {
        TODO("Not yet implemented")
    }
}