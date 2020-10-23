package com.thetechannel.android.planit.data.source.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {
    private val STUDY = 1

    private lateinit var taskMethod: DbTaskMethod
    private lateinit var category: DbCategory

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PlanItDatabase

    @Before
    fun initDb() = runBlockingTest {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlanItDatabase::class.java
        )
            .build()

        taskMethod = DbTaskMethod(1, "pomodoro", 0L, 0L, "https://www.google.com")
        category = DbCategory(STUDY, "Study")
        database.categoriesDao.insert(category)
        database.taskMethodsDao.insert(taskMethod)
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTask_getById() = runBlockingTest {
        val task = DbTask(
            "TASK1",
            System.currentTimeMillis(),
            0,
            1,
            "Maths Assignment",
            STUDY,
            false
        )
        database.tasksDao.insert(task)

        val loadedTask = database.tasksDao.getById(task.id)

        MatcherAssert.assertThat<DbTask>(loadedTask as DbTask, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loadedTask.id, CoreMatchers.`is`(task.id))
        MatcherAssert.assertThat(loadedTask.day, CoreMatchers.`is`(task.day))
        MatcherAssert.assertThat(loadedTask.startAt, CoreMatchers.`is`(task.startAt))
        MatcherAssert.assertThat(loadedTask.methodId, CoreMatchers.`is`(task.methodId))
        MatcherAssert.assertThat(loadedTask.catId, CoreMatchers.`is`(task.catId))
        MatcherAssert.assertThat(loadedTask.title, CoreMatchers.`is`(task.title))
        MatcherAssert.assertThat(loadedTask.completed, CoreMatchers.`is`(task.completed))
    }

    @Test
    fun insertAndDeleteTask_getTaskById_returnsNull() = runBlockingTest {
        val task = DbTask(
            "TASK1",
            System.currentTimeMillis(),
            0,
            1,
            "Maths Assignment",
            STUDY,
            false
        )
        database.tasksDao.insert(task)
        database.tasksDao.delete(task)

        val loadedTask = database.tasksDao.getById(task.id)
        MatcherAssert.assertThat(loadedTask, CoreMatchers.nullValue())
    }

    @Test
    fun insertTaskAndTaskType_returnTaskDetails() = runBlockingTest {
        val task =
            DbTask("TASK1", System.currentTimeMillis(), 0, 1, "Maths Assignment", 1, false)
        database.tasksDao.insert(task)

        val taskDetail = database.tasksDao.getTaskDetailsByTaskId(task.id)

        MatcherAssert.assertThat<DbTaskDetail>(taskDetail, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(taskDetail.id, CoreMatchers.`is`(task.id))
        MatcherAssert.assertThat(taskDetail.method, CoreMatchers.`is`(taskMethod.name))
        MatcherAssert.assertThat(taskDetail.methodIconUrl, CoreMatchers.`is`(taskMethod.iconUrl))
        MatcherAssert.assertThat(
            taskDetail.timeLapse,
            CoreMatchers.`is`(taskMethod.workLapse + taskMethod.breakLapse)
        )
        MatcherAssert.assertThat(taskDetail.title, CoreMatchers.`is`(task.title))
        MatcherAssert.assertThat(taskDetail.workStart, CoreMatchers.`is`(task.startAt))
        MatcherAssert.assertThat(
            taskDetail.workEnd,
            CoreMatchers.`is`(task.startAt + taskMethod.workLapse)
        )
        MatcherAssert.assertThat(
            taskDetail.breakStart,
            CoreMatchers.`is`(task.startAt + taskMethod.workLapse)
        )
        MatcherAssert.assertThat(
            taskDetail.breakEnd, CoreMatchers.`is`(
                task.startAt + taskMethod.workLapse + taskMethod.breakLapse
            )
        )
    }
}