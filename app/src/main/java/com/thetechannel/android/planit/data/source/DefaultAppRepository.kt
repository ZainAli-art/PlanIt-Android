package com.thetechannel.android.planit.data.source

import androidx.lifecycle.LiveData
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.database.TasksOverView
import com.thetechannel.android.planit.data.source.database.TodayProgress
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.util.*

class DefaultAppRepository(
    private val localDataSource: AppDataSource,
    private val remoteDataSource: AppDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppRepository {

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

    override suspend fun getCategories(forceUpdate: Boolean): Result<List<Category>> {
        if (forceUpdate) {
            try {
                updateCategoriesFromRemoteDataSouce()
            } catch (ex: Exception) {
                return Result.Error(ex)
            }
        }
        return localDataSource.getCategories()
    }

    override suspend fun getCategory(id: Int, forceUpdate: Boolean): Result<Category> {
        if (forceUpdate) {
            updateCategoryFromRemoteDataSource(id)
        }
        return localDataSource.getCategory(id)
    }

    private suspend fun updateCategoryFromRemoteDataSource(id: Int) {
        val remoteCategory = remoteDataSource.getCategory(id)

        if (remoteCategory is Result.Success) {
            localDataSource.saveCategory(remoteCategory.data)
        }
    }

    override suspend fun getTaskMethods(forceUpdate: Boolean): Result<List<TaskMethod>> {
        if (forceUpdate) {
            try {
                updateTaskMethodsFromRemoteDataSource()
            } catch (ex: Exception) {
                return Result.Error(ex)
            }
        }
        return localDataSource.getTaskMethods()
    }

    private suspend fun updateTaskMethodsFromRemoteDataSource() {
        val remoteMethods = remoteDataSource.getTaskMethods()

        if (remoteMethods is Result.Success) {
            localDataSource.deleteAllTaskMethods()
            remoteMethods.data.forEach {
                localDataSource.saveTaskMethod(it)
            }
        } else if (remoteMethods is Result.Error) {
            throw remoteMethods.exception
        }
    }

    override suspend fun getTaskMethod(id: Int, forceUpdate: Boolean): Result<TaskMethod> {
        if (forceUpdate) {
            updateTaskMethodFromRemoteDataSource(id)
        }
        return localDataSource.getTaskMethod(id)
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if (forceUpdate) {
            updateTasksFromRemoteDataSource()
        }
        return remoteDataSource.getTasks()
    }

    private suspend fun updateTasksFromRemoteDataSource() {
        val tasks = remoteDataSource.getTasks()

        if (tasks is Result.Success) {
            localDataSource.deleteAllTasks()
            tasks.data.forEach { localDataSource.saveTask(it) }
        } else if (tasks is Result.Error) {
            throw tasks.exception
        }
    }

    override suspend fun getTasks(day: Date, forceUpdate: Boolean): Result<List<Task>> {
        if (forceUpdate) {
            updateTasksFromRemoteDataSource(day)
        }
        return localDataSource.getTasks(day)
    }

    private suspend fun updateTasksFromRemoteDataSource(day: Date) {
        val tasks =  remoteDataSource.getTasks(day)
        if (tasks is Result.Success) {
            tasks.data.forEach { localDataSource.saveTask(it) }
        }
    }

    override suspend fun getTask(id: String, forceUpdate: Boolean): Result<Task> {
        if (forceUpdate) {
            updateTaskFromRemoteDataSource(id)
        }
        return localDataSource.getTask(id)
    }

    override suspend fun getTaskDetail(id: String, forceUpdate: Boolean): Result<TaskDetail> {
        if (forceUpdate) {
            val task = getTask(id, true)
            if (task is Result.Success) {
                updateCategoryFromRemoteDataSource(task.data.catId)
                updateTaskMethodFromRemoteDataSource(task.data.methodId)
            }
        }
        return localDataSource.getTaskDetail(id)
    }

    override suspend fun getTasksOverView(forceUpdate: Boolean): Result<TasksOverView> {
        if (forceUpdate) {
            updateTasksFromRemoteDataSource()
        }
        return localDataSource.getTasksOverView()
    }

    override suspend fun getTodayProgress(forceUpdate: Boolean): Result<TodayProgress> {
        if (forceUpdate) {
            updateTasksFromRemoteDataSource()
        }
        return localDataSource.getTodayProgress()
    }

    override suspend fun getTodayPieEntries(forceUpdate: Boolean): Result<List<PieEntry>> {
        if (forceUpdate) {
            updateTasksFromRemoteDataSource()
            updateCategoriesFromRemoteDataSouce()
        }
        return localDataSource.getTodayPieEntries()
    }

    override suspend fun saveCategory(category: Category) {
        remoteDataSource.saveCategory(category)
        updateCategoryFromRemoteDataSource(category.id)
    }

    override suspend fun saveCategories(vararg categories: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTaskMethod(taskMethod: TaskMethod) {
        remoteDataSource.saveTaskMethod(taskMethod)
        updateTaskMethodFromRemoteDataSource(taskMethod.id)
    }

    private suspend fun updateTaskMethodFromRemoteDataSource(id: Int) {
        val method = remoteDataSource.getTaskMethod(id)
        if (method is Result.Success) {
            localDataSource.saveTaskMethod(method.data)
        }
    }

    override suspend fun saveTaskMethods(vararg taskMethods: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        remoteDataSource.saveTask(task)
        updateTaskFromRemoteDataSource(task.id)
    }

    private suspend fun updateTaskFromRemoteDataSource(id: String) {
        val result = remoteDataSource.getTask(id)
        if (result is Result.Success) {
            localDataSource.saveTask(result.data)
        }
    }

    override suspend fun saveTasks(vararg tasks: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshCategories() {
        updateCategoriesFromRemoteDataSouce()
    }

    private suspend fun updateCategoriesFromRemoteDataSouce() {
        val remoteCategories = remoteDataSource.getCategories()

        if (remoteCategories is Result.Success) {
            localDataSource.deleteAllCategories()
            remoteCategories.data.forEach {
                localDataSource.saveCategory(it)
            }
        } else if (remoteCategories is Result.Error) {
            throw remoteCategories.exception
        }
    }

    override suspend fun refreshTaskMethods() {
        updateTaskMethodsFromRemoteDataSource()
    }

    override suspend fun refreshTasks() {
        updateTasksFromRemoteDataSource()
    }

    override suspend fun completeTask(task: Task) {
        completeTask(task.id)
    }

    override suspend fun completeTask(id: String) {
        remoteDataSource.completeTask(id)
        updateTaskFromRemoteDataSource(id)
    }

    override suspend fun deleteCategory(category: Category) {
        remoteDataSource.deleteCategory(category)
        localDataSource.deleteCategory(category)
    }

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) {
        remoteDataSource.deleteTaskMethod(taskMethod)
        localDataSource.deleteTaskMethod(taskMethod)
    }

    override suspend fun deleteTask(task: Task) {
        remoteDataSource.deleteTask(task)
        localDataSource.deleteTask(task)
    }

}
