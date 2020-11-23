package com.thetechannel.android.planit.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.database.TasksOverView
import com.thetechannel.android.planit.data.source.database.TodayProgress
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.util.wrapEspressoIdlingResource
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
        wrapEspressoIdlingResource {
            return localDataSource.observeCategories()
        }
    }

    override fun observeCategory(id: Int): LiveData<Result<Category>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskMethods(): LiveData<Result<List<TaskMethod>>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeTaskMethods()
        }
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
        wrapEspressoIdlingResource {
            return localDataSource.observeTasksOverView()
        }
    }

    override fun observeTodayProgress(): LiveData<Result<TodayProgress>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeTodayProgress()
        }
    }

    override fun observeTodayPieEntries(): LiveData<Result<List<PieEntry>>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeTodayPieEntries()
        }
    }

    override suspend fun getCategories(forceUpdate: Boolean): Result<List<Category>> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                try {
                    updateCategoriesFromRemoteDataSouce()
                } catch (ex: Exception) {
                    return Result.Error(ex)
                }
            }
            return localDataSource.getCategories()
        }
    }

    override suspend fun getCategory(id: Int, forceUpdate: Boolean): Result<Category> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateCategoryFromRemoteDataSource(id)
            }
            return localDataSource.getCategory(id)
        }
    }

    private suspend fun updateCategoryFromRemoteDataSource(id: Int) {
        wrapEspressoIdlingResource {
            val remoteCategory = remoteDataSource.getCategory(id)

            if (remoteCategory is Result.Success) {
                localDataSource.saveCategory(remoteCategory.data)
            }
        }
    }

    override suspend fun getTaskMethods(forceUpdate: Boolean): Result<List<TaskMethod>> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                try {
                    updateTaskMethodsFromRemoteDataSource()
                } catch (ex: Exception) {
                    return Result.Error(ex)
                }
            }
            return localDataSource.getTaskMethods()
        }
    }

    private suspend fun updateTaskMethodsFromRemoteDataSource() {
        wrapEspressoIdlingResource {
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
    }

    override suspend fun getTaskMethod(id: Int, forceUpdate: Boolean): Result<TaskMethod> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateTaskMethodFromRemoteDataSource(id)
            }
            return localDataSource.getTaskMethod(id)
        }
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateTasksFromRemoteDataSource()
            }
            return remoteDataSource.getTasks()
        }
    }

    private suspend fun updateTasksFromRemoteDataSource() {
        wrapEspressoIdlingResource {
            val tasks = remoteDataSource.getTasks()

            if (tasks is Result.Success) {
                localDataSource.deleteAllTasks()
                tasks.data.forEach { localDataSource.saveTask(it) }
            } else if (tasks is Result.Error) {
                throw tasks.exception
            }
        }
    }

    override suspend fun getTasks(day: Date, forceUpdate: Boolean): Result<List<Task>> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateTasksFromRemoteDataSource(day)
            }
            return localDataSource.getTasks(day)
        }
    }

    private suspend fun updateTasksFromRemoteDataSource(day: Date) {
        wrapEspressoIdlingResource {
            val tasks =  remoteDataSource.getTasks(day)
            if (tasks is Result.Success) {
                tasks.data.forEach { localDataSource.saveTask(it) }
            }
        }
    }

    override suspend fun getTask(id: String, forceUpdate: Boolean): Result<Task> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateTaskFromRemoteDataSource(id)
            }
            return localDataSource.getTask(id)
        }
    }

    override suspend fun getTaskDetails(forceUpdate: Boolean): Result<List<TaskDetail>> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateCategoriesFromRemoteDataSouce()
                updateTaskMethodsFromRemoteDataSource()
                updateTasksFromRemoteDataSource()
            }
            return localDataSource.getTaskDetails()
        }
    }

    override suspend fun getTaskDetail(id: String, forceUpdate: Boolean): Result<TaskDetail> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                val task = getTask(id, true)
                if (task is Result.Success) {
                    updateCategoryFromRemoteDataSource(task.data.catId)
                    updateTaskMethodFromRemoteDataSource(task.data.methodId)
                }
            }
            return localDataSource.getTaskDetail(id)
        }
    }

    override suspend fun getTasksOverView(forceUpdate: Boolean): Result<TasksOverView> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateTasksFromRemoteDataSource()
            }
            return localDataSource.getTasksOverView()
        }
    }

    override suspend fun getTodayProgress(forceUpdate: Boolean): Result<TodayProgress> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateTasksFromRemoteDataSource()
            }
            return localDataSource.getTodayProgress()
        }
    }

    override suspend fun getTodayPieEntries(forceUpdate: Boolean): Result<List<PieEntry>> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateTasksFromRemoteDataSource()
                updateCategoriesFromRemoteDataSouce()
            }
            return localDataSource.getTodayPieEntries()
        }
    }

    override suspend fun saveCategory(category: Category) {
        wrapEspressoIdlingResource {
            remoteDataSource.saveCategory(category)
            updateCategoryFromRemoteDataSource(category.id)
        }
    }

    override suspend fun saveCategories(vararg categories: Category) {
        wrapEspressoIdlingResource {
            categories.forEach { saveCategory(it) }
        }
    }

    override suspend fun saveTaskMethod(taskMethod: TaskMethod) {
        wrapEspressoIdlingResource {
            remoteDataSource.saveTaskMethod(taskMethod)
            updateTaskMethodFromRemoteDataSource(taskMethod.id)
        }
    }

    private suspend fun updateTaskMethodFromRemoteDataSource(id: Int) {
        wrapEspressoIdlingResource {
            val method = remoteDataSource.getTaskMethod(id)
            if (method is Result.Success) {
                localDataSource.saveTaskMethod(method.data)
            }
        }
    }

    override suspend fun saveTaskMethods(vararg taskMethods: TaskMethod) {
        wrapEspressoIdlingResource {
            taskMethods.forEach { saveTaskMethod(it) }
        }
    }

    override suspend fun saveTask(task: Task) {
        wrapEspressoIdlingResource {
            remoteDataSource.saveTask(task)
            updateTaskFromRemoteDataSource(task.id)
        }
    }

    private suspend fun updateTaskFromRemoteDataSource(id: String) {
        wrapEspressoIdlingResource {
            val result = remoteDataSource.getTask(id)
            if (result is Result.Success) {
                localDataSource.saveTask(result.data)
            }
        }
    }

    override suspend fun saveTasks(vararg tasks: Task) {
        wrapEspressoIdlingResource {
            tasks.forEach { saveTask(it) }
        }
    }

    override suspend fun refreshCategories() {
        wrapEspressoIdlingResource {
            updateCategoriesFromRemoteDataSouce()
        }
    }

    private suspend fun updateCategoriesFromRemoteDataSouce() {
        wrapEspressoIdlingResource {
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
    }

    override suspend fun refreshTaskMethods() {
        wrapEspressoIdlingResource {
            updateTaskMethodsFromRemoteDataSource()
        }
    }

    override suspend fun refreshTasks() {
        wrapEspressoIdlingResource {
            updateTasksFromRemoteDataSource()
        }
    }

    override suspend fun completeTask(task: Task) {
        wrapEspressoIdlingResource {
            completeTask(task.id)
        }
    }

    override suspend fun completeTask(id: String) {
        wrapEspressoIdlingResource {
            remoteDataSource.completeTask(id)
            updateTaskFromRemoteDataSource(id)
        }
    }

    override suspend fun deleteCategory(category: Category) {
        wrapEspressoIdlingResource {
            remoteDataSource.deleteCategory(category)
            localDataSource.deleteCategory(category)
        }
    }

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) {
        wrapEspressoIdlingResource {
            remoteDataSource.deleteTaskMethod(taskMethod)
            localDataSource.deleteTaskMethod(taskMethod)
        }
    }

    override suspend fun deleteTask(task: Task) {
        wrapEspressoIdlingResource {
            remoteDataSource.deleteTask(task)
            localDataSource.deleteTask(task)
        }
    }

}
