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
import java.util.*

class FakeDataSource(
    var categories: MutableList<Category>?,
    var methods: MutableList<TaskMethod>?,
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
        TODO("Not yet implemented")
    }

    override suspend fun getCategory(id: Int): Result<Category?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskMethods(): Result<List<TaskMethod>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskMethod(id: Int): Result<TaskMethod?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(day: Date): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(id: String): Result<Task?> {
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

    override suspend fun insertCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun insertCategories(vararg categories: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun insertTaskMethod(taskMethod: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun insertTaskMethods(vararg taskMethods: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun insertTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun insertTasks(vararg tasks: Task) {
        TODO("Not yet implemented")
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

    override suspend fun deleteTaskMethod(taskMethod: TaskMethod) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }

}
