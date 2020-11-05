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
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class FakeDataSource(
    var categories: MutableList<Category>?,
    var taskMethods: MutableList<TaskMethod>?,
    var tasks: MutableList<Task>?
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
        categories?.let { return Result.Success(it) }
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
        taskMethods?.let { return Result.Success(it) }
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
        tasks?.let { return Result.Success(it) }
        return Result.Error(
            Exception("Tasks not found")
        )
    }

    override suspend fun getTasks(day: Date): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(id: String): Result<Task> {
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
        categories?.add(category)
    }

    override suspend fun saveTaskMethod(taskMethod: TaskMethod) {
        taskMethods?.add(taskMethod)
    }

    override suspend fun saveTask(task: Task) {
        tasks?.add(task)
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
        categories?.clear()
    }

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTaskMethods() {
        taskMethods?.clear()
    }

    override suspend fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        tasks?.clear()
    }

}
