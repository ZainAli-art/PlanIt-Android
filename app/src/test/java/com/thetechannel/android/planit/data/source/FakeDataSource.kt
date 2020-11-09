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
import com.thetechannel.android.planit.util.isToday
import java.lang.Exception
import java.util.*

class FakeDataSource(
    var categories: MutableSet<Category>?,
    var taskMethods: MutableSet<TaskMethod>?,
    var tasks: MutableSet<Task>?
) : AppDataSource {
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

    override fun observeTask(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override fun observeTask(day: Date): LiveData<Result<List<Task>>> {
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

    override suspend fun getCategories(): Result<List<Category>> {
        categories?.let { return Result.Success(it.toList()) }
        return Result.Error(
            Exception("Categories not found")
        )
    }

    override suspend fun getCategory(id: Int): Result<Category> {
        categories?.forEach {
            if (it.id == id) return Result.Success(it)
        }
        return Result.Error(Exception("category id not found"))
    }

    override suspend fun getTaskMethods(): Result<List<TaskMethod>> {
        taskMethods?.let { return Result.Success(it.toList()) }
        return Result.Error(
            Exception("Task Methods not found")
        )
    }

    override suspend fun getTaskMethod(id: Int): Result<TaskMethod> {
        taskMethods?.forEach {
            if (it.id == id) return Result.Success(it)
        }
        return Result.Error(Exception("task method id not found"))
    }

    override suspend fun getTasks(): Result<List<Task>> {
        tasks?.let { return Result.Success(it.toList()) }
        return Result.Error(
            Exception("Tasks not found")
        )
    }

    override suspend fun getTasks(day: Date): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(id: String): Result<Task> {
        tasks?.forEach {
            if (it.id == id) return Result.Success(it)
        }
        return Result.Error(Exception("task id not found"))
    }

    override suspend fun getTaskDetail(id: String): Result<TaskDetail> {
        val task = getTask(id)
        if (task is Result.Success) {
            val category = getCategory(task.data.catId) as Result.Success
            val method = getTaskMethod(task.data.methodId) as Result.Success

            return Result.Success(
                com.thetechannel.android.planit.getTaskDetail(
                    category.data,
                    method.data,
                    task.data
                )
            )
        } else {
            return Result.Error(Exception("detail id not found"))
        }
    }

    override suspend fun getTasksOverView(): Result<TasksOverView> {
        val result = getTasks()
        when (result) {
            is Result.Success -> {
                val tasks = result.data
                val completed = tasks.filter { it.completed }.size
                val pending = tasks.filter { !it.completed }.size
                val completedToday = tasks.filter { it.completed && it.day.isToday() }.size

                return Result.Success(TasksOverView(completed, pending, completedToday))
            }
            else -> return Result.Error(Exception("error fetching tasks"))
        }
    }

    override suspend fun getTodayProgress(): Result<TodayProgress> {
        if (tasks == null) return Result.Error(Exception("no tasks"))

        val list = tasks?.toList() as List<Task>
        val total = list.filter { it.day.isToday() }.size
        val completed = list.filter { it.day.isToday() && it.completed }.size

        return Result.Success(TodayProgress(completed * 100 / total))
    }

    override suspend fun getTodayPieEntries(): Result<List<PieEntry>> {
        val catMap = mutableMapOf<Int, Category>()
        categories?.forEach { catMap[it.id] = it }
        val taskCount = mutableMapOf<Int, Int>()
        val todayTasks = tasks?.filter { it.day.isToday() }

        todayTasks?.forEach {
            taskCount[it.catId] = taskCount.getOrDefault(it.catId, 0) + 1
        }

        val entries = mutableListOf<PieEntry>()
        categories?.forEach {
            if (taskCount.containsKey(it.id)) {
                val count = taskCount[it.id]
                val name = it.name

                if (count == null) {
                    return Result.Error(Exception("invalid data in pie entry"))
                }

                entries.add(PieEntry(count.toFloat(), name))
            }
        }

        return Result.Success(entries)
    }

    override suspend fun saveCategory(category: Category) {
        categories?.add(category)
    }

    override suspend fun saveTaskMethod(taskMethod: TaskMethod) {
        taskMethods?.add(taskMethod)
    }

    override suspend fun saveTask(task: Task) {
        tasks?.add(task)
    }

    override suspend fun completeTask(task: Task) {
        completeTask(task.id)
    }

    override suspend fun completeTask(id: String) {
        tasks?.forEach {
            if (it.id == id) {
                it.completed = true
            }
        }
    }

    override suspend fun deleteCategory(category: Category) {
        categories?.remove(category)
    }

    override suspend fun deleteAllCategories() {
        categories?.clear()
    }

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) {
        taskMethods?.remove(taskMethod)
    }

    override suspend fun deleteAllTaskMethods() {
        taskMethods?.clear()
    }

    override suspend fun deleteTask(task: Task) {
        tasks?.remove(task)
    }

    override suspend fun deleteAllTasks() {
        tasks?.clear()
    }

}
