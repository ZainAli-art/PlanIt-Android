package com.thetechannel.android.planit.data.source.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppDataSource
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.util.asDatabaseEntity
import com.thetechannel.android.planit.util.asDomainModel
import com.thetechannel.android.planit.util.asPieEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class LocalDataSource(
    private val categoriesDao: CategoriesDao,
    private val taskMethodsDao: TaskMethodsDao,
    private val tasksDao: TasksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppDataSource {

    override fun observeCategories(): LiveData<Result<List<Category>>> {
        return categoriesDao.observeAll().map {
            Result.Success(
                it.map {
                    it.asDomainModel()
                }
            )
        }
    }

    override fun observeCategory(id: Int): LiveData<Result<Category>> {
        return categoriesDao.observeById(id).map {
            Result.Success(it.asDomainModel())
        }
    }

    override fun observeTaskMethods(): LiveData<Result<List<TaskMethod>>> {
        return taskMethodsDao.observeAll().map {
            Result.Success(
                it.map {
                    it.asDomainModel()
                }
            )
        }
    }

    override fun observeTaskMethod(id: Int): LiveData<Result<TaskMethod>> {
        return taskMethodsDao.observeById(id).map {
            Result.Success(it.asDomainModel())
        }
    }

    override fun observeTask(): LiveData<Result<List<Task>>> {
        return tasksDao.observeAll().map {
            Result.Success(
                it.map {
                    it.asDomainModel()
                }
            )
        }
    }

    override fun observeTask(day: Date): LiveData<Result<List<Task>>> {
        return tasksDao.observeByDay(day.time).map {
            Result.Success(
                it.map {
                    it.asDomainModel()
                }
            )
        }
    }

    override fun observeTask(id: String): LiveData<Result<Task>> {
        return tasksDao.observeById(id).map {
            Result.Success(it.asDomainModel())
        }
    }

    override fun observeTaskDetail(id: String): LiveData<Result<TaskDetail>> {
        return tasksDao.observeTaskDetailsByTaskId(id).map {
            Result.Success(
                it.asDomainModel()
            )
        }
    }

    override fun observeTasksOverView(): LiveData<Result<TasksOverView>> {
        return tasksDao.observeTasksOverView().map {
            Result.Success(it)
        }
    }

    override fun observeTodayProgress(): LiveData<Result<TodayProgress>> {
        return tasksDao.observeTodayProgress().map {
            Result.Success(it)
        }
    }

    override fun observeTodayPieEntries(): LiveData<Result<List<PieEntry>>> {
        return tasksDao.observeTodayPieDataViews().map {
            Result.Success(
                it.map {
                    it.asPieEntry()
                }
            )
        }
    }

    override suspend fun getCategories(): Result<List<Category>> = withContext(ioDispatcher) {
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

    override suspend fun getCategory(id: Int): Result<Category?> = withContext(ioDispatcher) {
        return@withContext try {
            val dbCategory = categoriesDao.getById(id)
            if (dbCategory == null) Result.Error(Exception("category id not found"))
            else Result.Success(dbCategory.asDomainModel())
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun getTaskMethods(): Result<List<TaskMethod>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(taskMethodsDao.getAll().map {
                it.asDomainModel()
            })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTaskMethod(id: Int): Result<TaskMethod?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val dbTaskMethod = taskMethodsDao.getById(id)
                if (dbTaskMethod == null) Result.Error(Exception("method id not found"))
                else Result.Success(dbTaskMethod.asDomainModel())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getTask(id: String): Result<Task?> = withContext(ioDispatcher) {
        return@withContext try {
            val dbTask = tasksDao.getById(id)
            if (dbTask == null) Result.Error(Exception("task id not found"))
            else Result.Success(dbTask.asDomainModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTasks(day: Date): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(tasksDao.getByDay(day.time).map {
                it.asDomainModel()
            })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
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

    override suspend fun getTaskDetail(id: String): Result<TaskDetail> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(
                tasksDao.getTaskDetailsByTaskId(id).asDomainModel()
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTasksOverView(): Result<TasksOverView> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(tasksDao.getTasksOverView())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTodayProgress(): Result<TodayProgress> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(tasksDao.getTodayProgress())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTodayPieEntries(): Result<List<PieEntry>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(
                tasksDao.getTodayPieDataViews().map {
                    it.asPieEntry()
                }
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

    override suspend fun insertTaskMethods(vararg taskMethods: TaskMethod) = withContext(ioDispatcher) {
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

    override suspend fun deleteAllCategories() = withContext(ioDispatcher) {
        categoriesDao.deleteAll()
    }

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) = withContext(ioDispatcher) {
        taskMethodsDao.delete(taskMethod.asDatabaseEntity())
    }

    override suspend fun deleteAllTaskMethods() = withContext(ioDispatcher) {
        taskMethodsDao.deleteAll()
    }

    override suspend fun deleteTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.delete(task.asDatabaseEntity())
    }

    override suspend fun deleteAllTasks() = withContext(ioDispatcher) {
        tasksDao.deleteAll()
    }
}