package com.thetechannel.android.planit.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.database.TasksOverView
import com.thetechannel.android.planit.data.source.database.TodayProgress
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.util.isToday
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList
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
                    val category = categories.data.firstOrNull { it.id == id }
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
                    val method = methods.data.firstOrNull { it.id == id }
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
        runBlocking { refreshTasks() }
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
                    val task = tasks.data.firstOrNull { it.id == id }
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(task)
                }
            }
        }
    }

    override fun observeTaskDetails(): LiveData<Result<List<TaskDetail>>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskDetail(id: String): LiveData<Result<TaskDetail>> {
        TODO("Not implemented yet")
    }

    override fun observeTasksOverView(): LiveData<Result<TasksOverView>> = observableTasks.map {
        when (it) {
            is Result.Loading -> Result.Loading
            is Result.Error -> Result.Error(it.exception)
            is Result.Success -> {
                val tasks = it.data
                val completed = tasks.filter { it.completed }.size
                val pending = tasks.filter { !it.completed }.size
                val completedToday = tasks.filter { it.completed && it.day.isToday() }.size
                Result.Success(TasksOverView(completed, pending, completedToday))
            }
        }
    }

    override fun observeTodayProgress(): LiveData<Result<TodayProgress>> = observableTasks.map {
        when (it) {
            is Result.Loading -> Result.Loading
            is Result.Error -> Result.Error(it.exception)
            is Result.Success -> Result.Success(getTodayProgress(it.data))
        }
    }

    override fun observeTodayPieEntries(): LiveData<Result<List<PieEntry>>> = observableTasks.map {
        when (it) {
            is Result.Loading -> Result.Loading
            is Result.Error -> Result.Error(it.exception)
            is Result.Success -> Result.Success(getTodayPieEntries(it.data))
        }
    }

    override suspend fun getCategories(forceUpdate: Boolean): Result<List<Category>> {
        return Result.Success(categoriesServiceData.values.toList())
    }

    override suspend fun getCategory(id: Int, forceUpdate: Boolean): Result<Category> {
        categoriesServiceData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find category"))
    }

    override suspend fun getTaskMethods(forceUpdate: Boolean): Result<List<TaskMethod>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskMethod(id: Int, forceUpdate: Boolean): Result<TaskMethod> {
        taskMethodsServiceData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find task method"))
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if (forceUpdate) refreshTasks()
        return Result.Success(ArrayList(tasksServiceData.values))
    }

    override suspend fun getTasks(day: Date, forceUpdate: Boolean): Result<List<Task>> {
        val tasks = tasksServiceData.values.filter {
            it.day == day
        }
        if (tasks.isEmpty()) return Result.Error(Exception("Could not find tasks"))
        return Result.Success(tasks)
    }

    override suspend fun getTask(id: String, forceUpdate: Boolean): Result<Task> {
        tasksServiceData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find task"))
    }

    override suspend fun getTaskDetails(forceUpdate: Boolean): Result<List<TaskDetail>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskDetail(id: String, forceUpdate: Boolean): Result<TaskDetail> {
        val task = tasksServiceData[id]
        val category = categoriesServiceData.get(task?.catId)
        val method = taskMethodsServiceData.get(task?.methodId)

        if (task == null || category == null || method == null) {
            return return Result.Error(Exception("Could not find task detail"))
        }

        val detail = getTaskDetail(category, method, task)
        return Result.Success(detail)
    }

    private fun getTaskDetail(category: Category, method: TaskMethod, task: Task) = TaskDetail(
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

    override suspend fun getTasksOverView(forceUpdate: Boolean): Result<TasksOverView> {
        val tasks = (getTasks(false) as Result.Success).data

        val completed = tasks.filter { it.completed }.size
        val pending = tasks.filter { !it.completed }.size
        val completedToday = tasks.filter { it.completed && it.day.isToday() }.size

        return Result.Success(TasksOverView(completed, pending, completedToday))
    }

    override suspend fun getTodayProgress(forceUpdate: Boolean): Result<TodayProgress> {
        return Result.Success(getTodayProgress(ArrayList(tasksServiceData.values)))
    }

    private fun getTodayProgress(tasks: List<Task>): TodayProgress {
        val todayTasks = tasks.filter { it.day.isToday() }
        val totalTasks = todayTasks.size
        val completed = todayTasks.filter { it.completed }.size

        return TodayProgress(completed * 100 / totalTasks)
    }

    override suspend fun getTodayPieEntries(forceUpdate: Boolean): Result<List<PieEntry>> {
        return Result.Success(getTodayPieEntries(ArrayList(tasksServiceData.values)))
    }

    private fun getTodayPieEntries(tasks: List<Task>): List<PieEntry> {
        val map = mutableMapOf<Int, Int>()

        for (t in tasks.filter { it.day.isToday() }) {
            map[t.catId] = map.getOrDefault(t.catId, 0) + 1
        }

        val entries = mutableListOf<PieEntry>()
        for (node in map) {
            entries.add(PieEntry(node.value.toFloat(), categoriesServiceData[node.key]?.name))
        }

        return entries
    }

    override suspend fun saveCategory(category: Category) {
        categoriesServiceData[category.id] = category
        refreshCategories()
    }

    override suspend fun saveCategories(vararg categories: Category) {
        for (c in categories) categoriesServiceData[c.id] = c
        refreshCategories()
    }

    override suspend fun saveTaskMethod(taskMethod: TaskMethod) {
        taskMethodsServiceData[taskMethod.id] = taskMethod
        refreshTaskMethods()
    }

    override suspend fun saveTaskMethods(vararg taskMethods: TaskMethod) {
        for (m in taskMethods) taskMethodsServiceData[m.id] = m
        refreshTaskMethods()
    }

    override suspend fun saveTask(task: Task) {
        tasksServiceData[task.id] = task
        refreshTasks()
    }

    override suspend fun saveTasks(vararg tasks: Task) {
        for (t in tasks) tasksServiceData[t.id] = t
    }

    override suspend fun refreshCategories() {
        observableCategories.value = getCategories(false)
    }

    override suspend fun refreshTaskMethods() {
        observableTaskMethods.value = getTaskMethods(false)
    }

    override suspend fun refreshTasks() {
        observableTasks.postValue(getTasks(false))
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
}