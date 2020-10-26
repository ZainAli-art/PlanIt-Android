package com.thetechannel.android.planit.data.source.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class LocalDataSource(
    private val categoriesDao: CategoriesDao,
    private val taskMethodsDao: TaskMethodsDao,
    private val tasksDao: TasksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppDataSource {

    override fun observeAllCategories(): LiveData<Result<List<Category>>> {
        return categoriesDao.observeAll().map {
            Result.Success(
                it.map {
                    it.asDomainModel()
                }
            )
        }
    }

    override fun observeCategoryById(id: Int): LiveData<Result<Category>> {
        return categoriesDao.observeById(id).map {
            Result.Success(it.asDomainModel())
        }
    }

    override fun observeAllTaskMethods(): LiveData<Result<List<TaskMethod>>> {
        return taskMethodsDao.observeAll().map {
            Result.Success(
                it.map {
                    it.asDomainModel()
                }
            )
        }
    }

    override fun observeTaskMethodById(id: Int): LiveData<Result<TaskMethod>> {
        return taskMethodsDao.observeById(id).map {
            Result.Success(it.asDomainModel())
        }
    }

    override fun observeAllTasks(): LiveData<Result<List<Task>>> {
        return tasksDao.observeAll().map {
            Result.Success(
                it.map {
                    it.asDomainModel()
                }
            )
        }
    }

    override fun observeTaskById(id: String): LiveData<Result<Task>> {
        return tasksDao.observeById(id).map {
            Result.Success(it.asDomainModel())
        }
    }

    override fun observeTaskByDay(day: Date): LiveData<Result<List<Task>>> {
        return tasksDao.observeByDay(day.time).map {
            Result.Success(
                it.map {
                    it.asDomainModel()
                }
            )
        }
    }

    override fun observeTaskDetailsByTaskId(id: String): LiveData<Result<TaskDetail>> {
        return tasksDao.observeTaskDetailsByTaskId(id).map {
            Result.Success(
                it.asDomainModel()
            )
        }
    }

    override suspend fun getAllCategories(): Result<List<Category>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(
                categoriesDao.getAll().map {
                    it.asDomainModel()
                }
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCategoryById(id: Int): Result<Category?> = withContext(ioDispatcher) {
        return@withContext try {
            val dbCategory = categoriesDao.getById(id)
            if (dbCategory == null) Result.Error(Exception("category id not found"))
            else Result.Success(dbCategory.asDomainModel())
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun getAllTaskMethods(): Result<List<TaskMethod>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(taskMethodsDao.getAll().map {
                it.asDomainModel()
            })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTaskMethodById(id: Int): Result<TaskMethod?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val dbTaskMethod = taskMethodsDao.getById(id)
                if (dbTaskMethod == null) Result.Error(Exception("method id not found"))
                else Result.Success(dbTaskMethod.asDomainModel())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getTaskById(id: String): Result<Task?> = withContext(ioDispatcher) {
        return@withContext try {
            val dbTask = tasksDao.getById(id)
            if (dbTask == null) Result.Error(Exception("task id not found"))
            else Result.Success(dbTask.asDomainModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTasksByDay(day: Date): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(tasksDao.getByDay(day.time).map {
                it.asDomainModel()
            })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAllTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(
                tasksDao.getAll().map {
                    it.asDomainModel()
                }
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTaskDetailsByTaskId(id: String): Result<TaskDetail> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(
                tasksDao.getTaskDetailsByTaskId(id).asDomainModel()
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun insertCategory(category: Category) = withContext(ioDispatcher) {
        categoriesDao.insert(category.asDatabaseEntity())
    }

    override suspend fun insertCategories(vararg categories: Category) = withContext(ioDispatcher) {
        for (category in categories) {
            categoriesDao.insert(category.asDatabaseEntity())
        }
    }

    override suspend fun insertTaskMethod(taskMethod: TaskMethod) = withContext(ioDispatcher) {
        taskMethodsDao.insert(taskMethod.asDatabaseEntity())
    }

    override suspend fun insertTaskMethods(vararg taskMethods: TaskMethod) = runBlocking {
        for (method in taskMethods) {
            taskMethodsDao.insert(method.asDatabaseEntity())
        }
    }

    override suspend fun insertTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.insert(task.asDatabaseEntity())
    }

    override suspend fun insertTasks(vararg tasks: Task) = withContext(ioDispatcher) {
        for (task in tasks) {
            tasksDao.insert(task.asDatabaseEntity())
        }
    }

    override suspend fun completeTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(task.id, true)
    }

    override suspend fun completeTask(id: String) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(id, true)
    }

    override suspend fun deleteCategory(category: Category) = withContext(ioDispatcher) {
        categoriesDao.delete(category.asDatabaseEntity())
    }

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) = withContext(ioDispatcher) {
        taskMethodsDao.delete(taskMethod.asDatabaseEntity())
    }

    override suspend fun deleteTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.delete(task.asDatabaseEntity())
    }
}