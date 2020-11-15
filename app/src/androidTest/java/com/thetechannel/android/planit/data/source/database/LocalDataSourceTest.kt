package com.thetechannel.android.planit.data.source.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.data.succeeded
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import java.net.URI
import java.sql.Time
import java.util.*

class LocalDataSourceTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PlanItDatabase
    private lateinit var dataSource: LocalDataSource

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlanItDatabase::class.java
        )
            .build()

        dataSource = LocalDataSource(database.categoriesDao, database.taskMethodsDao, database.tasksDao)
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertCategories_fetchAllCategories_returnsAllInsertedCategories() = runBlocking {
        val categories = arrayOf(
            Category(1, "Study"),
            Category(2, "Sport"),
            Category(3, "Business")
        )
        categories.forEach { dataSource.saveCategory(it) }

        val result = dataSource.getCategories()
        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        categories.sortBy { c -> c.name }

        val loaded = result.data

        assertThat(categories.size, `is`(loaded.size))
        for (i in categories.indices) {
            val insertedCat = categories.get(i)
            val loadedCat = loaded.get(i)

            assertThat(insertedCat.id, `is`(loadedCat.id))
            assertThat(insertedCat.name, `is`(loadedCat.name))
        }
    }

    @Test
    fun insertCategory_fetchById_returnsInsertedCategory() = runBlocking {
        val category = Category(1, "Business")
        dataSource.saveCategory(category)

        val result = dataSource.getCategory(category.id)
        assertThat(result.succeeded, `is`(true))
        result as Result.Success<Category>

        assertThat(category.id, `is`(result.data.id))
        assertThat(category.name, `is`(result.data.name))
    }

    @Test
    fun insertTaskMethods_fetchAll_returnsAllTheInsertedMethods() = runBlocking {
        val methods = arrayOf(
            TaskMethod(1, "pomodoro", Time(1500L), Time(500L), URI("https://localhost")),
            TaskMethod(2, "eat the devil", Time(60000L), Time(200L), URI("https://localhost"))
        )
        methods.forEach{ dataSource.saveTaskMethod(it) }

        val result = dataSource.getTaskMethods()
        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        methods.sortBy { m -> m.name }

        val loaded = result.data
        assertThat(methods.size, `is`(loaded.size))
        for (i in methods.indices) {
            val insertedMethod = methods.get(i)
            val loadedMethod = loaded.get(i)

            assertThat(insertedMethod.id, `is`(loadedMethod.id))
            assertThat(insertedMethod.name, `is`(loadedMethod.name))
            assertThat(insertedMethod.workLapse, `is`(loadedMethod.workLapse))
            assertThat(insertedMethod.breakLapse, `is`(loadedMethod.breakLapse))
            assertThat(insertedMethod.iconUrl, `is`(loadedMethod.iconUrl))
        }
    }

    @Test
    fun insertTaskMethod_fetchById_returnsTheInsertedMethod() = runBlocking {
        val method = TaskMethod(1, "pomodoro", Time(1500L), Time(500L), URI("https://localhost"))
        dataSource.saveTaskMethod(method)

        val result = dataSource.getTaskMethod(method.id)
        assertThat(result.succeeded, `is`(true))
        result as Result.Success<TaskMethod>

        val loaded = result.data
        assertThat(method.id, `is`(loaded.id))
        assertThat(method.name, `is`(loaded.name))
        assertThat(method.workLapse, `is`(loaded.workLapse))
        assertThat(method.breakLapse, `is`(loaded.breakLapse))
        assertThat(method.iconUrl, `is`(loaded.iconUrl))
    }

    @Test
    fun insertTasks_fetchAll_returnsAllInsertedTasks() = runBlocking {
        val tasks = arrayOf(
            Task(UUID.randomUUID().toString(), Date(23L), Time(1500), 1, "Maths Assignment", 1, false),
            Task(UUID.randomUUID().toString(), Date(24L), Time(1540), 1, "Read Emails", 1, false),
            Task(UUID.randomUUID().toString(), Date(25L), Time(100), 1, "Clean my room", 1, false)
        )
        tasks.forEach { dataSource.saveTask(it) }

        val result = dataSource.getTasks()
        assertThat(result.succeeded, `is`(true))
        result as Result.Success

        val loaded = result.data
        assertThat(tasks.size, `is`(loaded.size))
        for (i in tasks.indices) {
            val insertedTask = tasks.get(i)
            val loadedTask = tasks.get(i)

            assertThat(insertedTask.id, `is`(loadedTask.id))
            assertThat(insertedTask.day, `is`(loadedTask.day))
            assertThat(insertedTask.startAt, `is`(loadedTask.startAt))
            assertThat(insertedTask.methodId, `is`(loadedTask.methodId))
            assertThat(insertedTask.title, `is`(loadedTask.title))
            assertThat(insertedTask.catId, `is`(loadedTask.catId))
            assertThat(insertedTask.completed, `is`(loadedTask.completed))
        }
    }

    @Test
    fun insertTask_fetchById_returnsInsertedTask() = runBlocking {
        val task = Task(UUID.randomUUID().toString(), Date(23L), Time(1500), 1, "Maths Assignment", 1, false)
        dataSource.saveTask(task)

        val result = dataSource.getTask(task.id)
        assertThat(result.succeeded, `is`(true))
        result as Result.Success<Task>

        val loaded = result.data
        assertThat(task.id, `is`(loaded.id))
        assertThat(task.day, `is`(loaded.day))
        assertThat(task.startAt, `is`(loaded.startAt))
        assertThat(task.methodId, `is`(loaded.methodId))
        assertThat(task.title, `is`(loaded.title))
        assertThat(task.catId, `is`(loaded.catId))
        assertThat(task.completed, `is`(loaded.completed))
    }

    @Test
    fun insertTasks_fetchByDay_returnsInsertedTasks() = runBlocking {
        val today = Date(23L)
        val yesterday = Date(22L)

        val todayTask1 = Task(UUID.randomUUID().toString(), today, Time(1500L), 1, "Maths Assignment", 1, false)
        val todayTask2 =  Task(UUID.randomUUID().toString(), today, Time(1540L), 1, "Read Emails", 1, false)
        val yesterdayTask1 = Task(UUID.randomUUID().toString(), yesterday, Time(100L), 1, "Clean my room", 1, false)

        val tasks = arrayOf(
            todayTask1,
            todayTask2,
            yesterdayTask1
        )
        tasks.forEach { dataSource.saveTask(it) }

        val result = dataSource.getTasks(today)
        assertThat(result.succeeded, `is`(true))
        result as Result.Success

        val loaded = result.data
        assertThat(loaded.size, `is`(2))

        val loadedTask1 = loaded.get(0)
        val loadedTask2 = loaded.get(1)

        assertThat(todayTask1.id, `is`(loadedTask1.id))
        assertThat(todayTask1.day, `is`(loadedTask1.day))
        assertThat(todayTask1.startAt, `is`(loadedTask1.startAt))
        assertThat(todayTask1.methodId, `is`(loadedTask1.methodId))
        assertThat(todayTask1.title, `is`(loadedTask1.title))
        assertThat(todayTask1.catId, `is`(loadedTask1.catId))
        assertThat(todayTask1.completed, `is`(loadedTask1.completed))

        assertThat(todayTask2.id, `is`(loadedTask2.id))
        assertThat(todayTask2.day, `is`(loadedTask2.day))
        assertThat(todayTask2.startAt, `is`(loadedTask2.startAt))
        assertThat(todayTask2.methodId, `is`(loadedTask2.methodId))
        assertThat(todayTask2.title, `is`(loadedTask2.title))
        assertThat(todayTask2.catId, `is`(loadedTask2.catId))
        assertThat(todayTask2.completed, `is`(loadedTask2.completed))
    }

    @Test
    fun insertTasks_fetchTaskDetails_returnsTaskDetailsOfInsertedTasks() = runBlocking {
        val category = Category(1, "Study")
        val method = TaskMethod(1, "pomodoro", Time(25000L), Time(5000L), URI("https://localhost"))
        val task1 = Task("task_1", Date(23000L), Time(2000L), method.id, "Maths Assignment", category.id, false)
        val task2 = Task("task_2", Date(24000L), Time(2000L), method.id, "Half an hour jog", category.id, true)
        dataSource.saveCategory(category)
        dataSource.saveTaskMethod(method)
        arrayOf(task1, task2).forEach { dataSource.saveTask(it) }

        val result = dataSource.getTaskDetails()

        assertThat(result.succeeded, `is`(true))
        val details = (result as Result.Success).data
        assertThat(details.size, `is`(2))
        val detail1 = details[0]
        assertThat(detail1.id, `is`(task1.id))
        assertThat(detail1.categoryName, `is`(category.name))
        assertThat(detail1.methodName, `is`(method.name))
        assertThat(detail1.methodIconUrl, `is`(method.iconUrl))
        assertThat(
            detail1.timeLapse,
            `is`(Time(method.workLapse.time + method.breakLapse.time))
        )
        assertThat(detail1.title, `is`(task1.title))
        assertThat(detail1.workStart, `is`(task1.startAt))
        assertThat(detail1.workEnd, `is`(Time(task1.startAt.time + method.workLapse.time)))
        assertThat(detail1.breakStart, `is`(Time(task1.startAt.time + method.workLapse.time)))
        assertThat(
            detail1.breakEnd,
            `is`(Time(task1.startAt.time + method.workLapse.time + method.breakLapse.time))
        )
        val detail2 = details[1]
        assertThat(detail2.id, `is`(task2.id))
        assertThat(detail2.categoryName, `is`(category.name))
        assertThat(detail2.methodName, `is`(method.name))
        assertThat(detail2.methodIconUrl, `is`(method.iconUrl))
        assertThat(
            detail2.timeLapse,
            `is`(Time(method.workLapse.time + method.breakLapse.time))
        )
        assertThat(detail2.title, `is`(task2.title))
        assertThat(detail2.workStart, `is`(task2.startAt))
        assertThat(detail2.workEnd, `is`(Time(task2.startAt.time + method.workLapse.time)))
        assertThat(detail2.breakStart, `is`(Time(task2.startAt.time + method.workLapse.time)))
        assertThat(
            detail2.breakEnd,
            `is`(Time(task2.startAt.time + method.workLapse.time + method.breakLapse.time))
        )
    }

    @Test
    fun insertTask_fetchTaskDetailsById_returnsDetailsOfInsertedTask() = runBlocking {
        val category = Category(1, "Study")
        val method = TaskMethod(1, "pomodoro", Time(25000L), Time(5000L), URI("https://localhost"))
        val task = Task("task_id", Calendar.getInstance().time, Time(150L), method.id, "Maths Assignment", category.id, false)
        dataSource.saveCategory(category)
        dataSource.saveTaskMethod(method)
        dataSource.saveTask(task)

        val result = dataSource.getTaskDetail(task.id)
        assertThat(result.succeeded, `is`(true))
        result as Result.Success<TaskDetail>
        val detail = result.data

        assertThat(detail.id, `is`(task.id))
        assertThat(detail.categoryName, `is`(category.name))
        assertThat(detail.methodName, `is`(method.name))
        assertThat(detail.methodIconUrl, `is`(method.iconUrl))
        assertThat(detail.timeLapse.time, `is`(method.workLapse.time + method.breakLapse.time))
        assertThat(detail.title, `is`(task.title))
        assertThat(detail.workStart, `is`(task.startAt))
        assertThat(detail.workEnd.time, `is`(task.startAt.time + method.workLapse.time))
        assertThat(detail.breakStart.time, `is`(task.startAt.time + method.workLapse.time))
        assertThat(detail.breakEnd.time, `is`(task.startAt.time + method.workLapse.time + method.breakLapse.time))
    }

    @Test
    fun insertTask_completeTheTaskAndFetchById_returnsCompletedTask() = runBlocking {
        val task = Task(UUID.randomUUID().toString(), Date(23L), Time(1500), 1, "Maths Assignment", 1, false)
        dataSource.saveTask(task)

        dataSource.completeTask(task)
        val result = dataSource.getTask(task.id)
        assertThat(result.succeeded, `is`(true))
        result as Result.Success<Task>

        val loaded = result.data
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.completed, `is`(true))
    }

    @Test
    fun insertCategory_deleteItAndFetchById_returnsNull() = runBlocking {
        val category = Category(1, "Sport")
        dataSource.saveCategory(category)

        dataSource.deleteCategory(category)
        val result = dataSource.getCategory(category.id)
        assertThat(result.succeeded, `is`(false))
        result as Result.Error
        assertThat(result.exception.message, `is`("category id not found"))
    }

    @Test
    fun insertTaskMethod_deleteItAndFetchById_returnsNull() = runBlocking {
        val method = TaskMethod(1, "pomodoro", Time(25000L), Time(5000L), URI("http://localhost"))
        dataSource.saveTaskMethod(method)

        dataSource.deleteTaskMethod(method)

        val result = dataSource.getTaskMethod(method.id)
        assertThat(result.succeeded, `is`(false))
        result as Result.Error
        assertThat(result.exception.message, `is`("method id not found"))
    }

    @Test
    fun insertTask_deleteItAndFetchById_returnsNull() = runBlocking {
        val task = Task(UUID.randomUUID().toString(), Date(23L), Time(1500L), 1, "Maths Assignment", 1, false)
        dataSource.saveTask(task)

        dataSource.deleteTask(task)
        val result = dataSource.getTask(task.id)
        assertThat(result.succeeded, `is`(false))
        result as Result.Error
        assertThat(result.exception.message, `is`("task id not found"))
    }

    @Test
    fun insertTasks_getTasksOverview_returnsOverview() = runBlocking {
        val tasks = arrayOf(
            Task("task_1", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Maths Assignment", 1, true),
            Task("task_2", Date(10000L), Time(System.currentTimeMillis()), 1, "Read Emails", 2, true),
            Task("task_3", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Do a sprint", 3, false)
        )
        tasks.forEach { dataSource.saveTask(it) }

        val result = dataSource.getTasksOverView()
        assertThat(result.succeeded, `is`(true))
        result as Result.Success

        val overview = result.data
        assertThat(overview.completedTasks, `is`(2))
        assertThat(overview.pendingTasks, `is`(1))
        assertThat(overview.tasksCompletedToday, `is`(1))
    }

    @Test
    fun insertTasks_getTodayProgress_returnsTodayCompletedTasksPercentage() = runBlocking {
        val tasks = arrayOf(
            Task("task_1", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Maths Assignment", 1, true),
            Task("task_2", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Read Emails", 2, true),
            Task("task_3", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Do a sprint", 3, false)
        )
        tasks.forEach { dataSource.saveTask(it) }

        val result = dataSource.getTodayProgress()
        assertThat(result.succeeded, `is`(true))
        result as Result.Success

        val progress = result.data
        assertThat(progress.percentage, `is`(66))
    }

    @Test
    fun insertTasks_getTodayPieEntries_returnsTodayPieEntries() = runBlocking {
        val categories = arrayOf(
            Category(1, "Study"),
            Category(2, "Sport"),
            Category(3, "Business")
        )
        categories.forEach { dataSource.saveCategory(it) }
        val method = TaskMethod(1, "pomodoro", Time(25000L), Time(5000L), URI("http://localhost"))
        dataSource.saveTaskMethod(method)
        val tasks = arrayOf(
            Task("task_1", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Maths Assignment", 1, true),
            Task("task_2", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Read Emails", 2, true),
            Task("task_3", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Do a sprint", 3, false)
        )
        tasks.forEach { dataSource.saveTask(it) }

        val result = dataSource.getTodayPieEntries()
        assertThat(result.succeeded, `is`(true))
        result as Result.Success

        val entries = result.data
        assertThat(entries.size, `is`(3))
        val entryData = mutableMapOf<String, PieEntry>()
        for (e in entries) entryData[e.label] = e

        assertThat(entryData["Study"]?.value, `is`(1f))
        assertThat(entryData["Business"]?.value, `is`(1f))
        assertThat(entryData["Sport"]?.value, `is`(1f))
    }

    @Test
    fun insertCategories_deleteAllCategoriesAndGetAll_returnsEmptyList() = runBlocking {
        arrayOf(
            Category(1, "Study"),
            Category(2, "Sport"),
            Category(3, "Business")
        ).forEach { dataSource.saveCategory(it) }

        dataSource.deleteAllCategories()

        val result = dataSource.getCategories()
        assertThat(result.succeeded, `is`(true))
        val loaded = (result as Result.Success).data
        assertThat(loaded.isEmpty(), `is`(true))
    }

    @Test
    fun insertTaskMethods_deleteAllTaskMethodsAndGetAll_returnsEmptyList() = runBlocking {
        arrayOf(
            TaskMethod(1, "pomodoro", Time(1500L), Time(500L), URI("https://localhost")),
            TaskMethod(2, "eat the devil", Time(60000L), Time(200L), URI("https://localhost"))
        ).forEach{ dataSource.saveTaskMethod(it) }

        dataSource.deleteAllTaskMethods()

        val result = dataSource.getTaskMethods()
        assertThat(result.succeeded, `is`(true))
        val loaded = (result as Result.Success).data
        assertThat(loaded.isEmpty(), `is`(true))
    }

    @Test
    fun insertTasks_deleteAllTasksAndGetAll_returnsEmptyList() = runBlocking {
        arrayOf(
            Task("task_1", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Maths Assignment", 1, true),
            Task("task_2", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Read Emails", 2, true),
            Task("task_3", Calendar.getInstance().time, Time(System.currentTimeMillis()), 1, "Do a sprint", 3, false)
        ).forEach{ dataSource.saveTask(it) }

        dataSource.deleteAllTasks()

        val result = dataSource.getTasks()
        assertThat(result.succeeded, `is`(true))
        val loaded = (result as Result.Success).data
        assertThat(loaded.isEmpty(), `is`(true))
    }
}