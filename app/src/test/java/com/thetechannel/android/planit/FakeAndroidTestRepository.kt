package com.thetechannel.android.planit

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.sql.Time
import java.util.*
import kotlin.collections.HashMap

class FakeAndroidTestRepository : AppRepository {
    private val observableCategories: MutableLiveData<Result<List<Category>>> = MutableLiveData()
    private val observableTaskMethods: MutableLiveData<Result<List<TaskMethod>>> = MutableLiveData()
    private val observableTasks: MutableLiveData<Result<List<Task>>> = MutableLiveData()

    var categoriesServiceData: HashMap<Int, Category> = HashMap()
    var taskMethodsServiceData: HashMap<Int, TaskMethod> = HashMap()
    var tasksServiceData: HashMap<String, Task> = HashMap()

    override fun observeCategories(): LiveData<Result<List<Category>>> {
        runBlocking { refreshCategories() }
        return observableCategories
    }

    override fun observeCategory(id: Int): LiveData<Result<Category>> {
        runBlocking { refreshCategories() }
        return observableCategories.map { categories ->
            when (categories) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(categories.exception)
                is Result.Success -> {
                    val category = categories.data.firstOrNull() { it.id == id }
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(category)
                }
            }
        }
    }

    override fun observeTaskMethods(): LiveData<Result<List<TaskMethod>>> {
        runBlocking { refreshTaskMethods() }
        return observableTaskMethods
    }

    override fun observeTaskMethod(id: Int): LiveData<Result<TaskMethod>> {
        runBlocking { refreshTaskMethods() }
        return observableTaskMethods.map { methods ->
            when (methods) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(methods.exception)
                is Result.Success -> {
                    val method = methods.data.firstOrNull() { it.id == id }
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(method)
                }
            }
        }
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runBlocking { refreshTasks() }
        return observableTasks
    }

    override fun observeTasks(day: Date): LiveData<Result<List<Task>>> {
        return observableTasks.map { tasks ->
            when (tasks) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(tasks.exception)
                is Result.Success -> {
                    val requiredTasks = tasks.data.filter { it.day.time == day.time }
                    if (requiredTasks.isEmpty()) Result.Error(Exception("Not found"))
                    else Result.Success(requiredTasks)
                }
            }
        }
    }

    override fun observeTask(id: String): LiveData<Result<Task>> {
        runBlocking { refreshTasks() }
        return observableTasks.map { tasks ->
            when (tasks) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(tasks.exception)
                is Result.Success -> {
                    val task = tasks.data.firstOrNull() { it.id == id }
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(task)
                }
            }
        }
    }

    override fun observeTaskDetails(id: String): LiveData<Result<TaskDetail>> {
        TODO("Not implemented yet")
    }

    override suspend fun getCategories(): Result<List<Category>> {
        return Result.Success(categoriesServiceData.values.toList())
    }

    override suspend fun getCategory(id: Int): Result<Category?> {
        categoriesServiceData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find category"))
    }

    override suspend fun getTaskMethods(): Result<List<TaskMethod>> {
        return Result.Success(taskMethodsServiceData.values.toList())
    }

    override suspend fun getTaskMethod(id: Int): Result<TaskMethod?> {
        taskMethodsServiceData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find task method"))
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun getTasks(day: Date): Result<List<Task>> {
        val tasks = tasksServiceData.values.filter {
            it.day == day
        }
        if (tasks.isEmpty()) return Result.Error(Exception("Could not find tasks"))
        return Result.Success(tasks)
    }

    override suspend fun getTask(id: String): Result<Task?> {
        tasksServiceData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find task"))
    }

    override suspend fun getTaskDetails(id: String): Result<TaskDetail> {
        val detail = getTaskDetail(id)
            ?: return Result.Error(Exception("Could not find task detail"))
        return Result.Success(detail)
    }

    override suspend fun insertCategory(category: Category) {
        categoriesServiceData[category.id] = category
        refreshCategories()
    }

    override suspend fun insertCategories(vararg categories: Category) {
        for (c in categories) categoriesServiceData[c.id] = c
        refreshCategories()
    }

    override suspend fun insertTaskMethod(taskMethod: TaskMethod) {
        taskMethodsServiceData[taskMethod.id] = taskMethod
        refreshTaskMethods()
    }

    override suspend fun insertTaskMethods(vararg taskMethods: TaskMethod) {
        for (m in taskMethods) taskMethodsServiceData[m.id] = m
        refreshTaskMethods()
    }

    override suspend fun insertTask(task: Task) {
        tasksServiceData[task.id] = task
        refreshTasks()
    }

    override suspend fun insertTasks(vararg tasks: Task) {
        for (t in tasks) tasksServiceData[t.id] = t
    }

    override suspend fun refreshCategories() {
        observableCategories.value = getCategories()
    }

    override suspend fun refreshTaskMethods() {
        observableTaskMethods.value = getTaskMethods()
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override suspend fun completeTask(task: Task) {
        tasksServiceData[task.id]?.completed = true
        refreshTasks()
    }

    override suspend fun completeTask(id: String) {
        tasksServiceData[id]?.completed = true
        refreshTasks()
    }

    override suspend fun deleteCategory(category: Category) {
        categoriesServiceData.remove(category.id)
        refreshCategories()
    }

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) {
        taskMethodsServiceData.remove(taskMethod.id)
        refreshTaskMethods()
    }

    override suspend fun deleteTask(task: Task) {
        tasksServiceData.remove(task.id)
        refreshTasks()
    }

    fun addTasks(vararg tasks: Task) {
        for (t in tasks) tasksServiceData[t.id] = t
        runBlocking { refreshTasks() }
    }

    private fun getTaskDetail(taskId: String): TaskDetail? {
        val task = tasksServiceData[taskId]
        val category = categoriesServiceData.get(task?.catId)
        val method = taskMethodsServiceData.get(task?.methodId)

        if (task == null || category == null || method == null) {
            return null;
        }

        return getTaskDetail(category, method, task)
    }
}

@VisibleForTesting
fun getTaskDetail(category: Category, method: TaskMethod, task: Task) = TaskDetail(
    id = task.id,
    categoryName = category.name,
    methodName = method.name,
    methodIconUrl = method.iconUrl,
    timeLapse = Time(method.workLapse.time + method.breakLapse.time),
    title = task.title,
    workStart = task.startAt,
    workEnd = Time(task.startAt.time + method.workLapse.time),
    breakStart = Time(task.startAt.time + method.workLapse.time),
    breakEnd = Time(task.startAt.time + method.workLapse.time + method.breakLapse.time)
)