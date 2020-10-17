package com.thetechannel.android.planit.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.thetechannel.android.planit.data.source.database.*
import com.thetechannel.android.planit.data.source.domain.TaskMethodId.POMODORO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
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

        taskMethod = DbTaskMethod(POMODORO.data, "pomodoro", 0L, 0L, "https://www.google.com")
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
            POMODORO.data,
            "Maths Assignment",
            STUDY,
            false
        )
        database.tasksDao.insert(task)

        val loadedTask = database.tasksDao.getById(task.id)

        assertThat<DbTask>(loadedTask as DbTask, notNullValue())
        assertThat(loadedTask.id, `is`(task.id))
        assertThat(loadedTask.day, `is`(task.day))
        assertThat(loadedTask.startAt, `is`(task.startAt))
        assertThat(loadedTask.methodId, `is`(task.methodId))
        assertThat(loadedTask.catId, `is`(task.catId))
        assertThat(loadedTask.title, `is`(task.title))
        assertThat(loadedTask.completed, `is`(task.completed))
    }

    @Test
    fun insertAndDeleteTask_getTaskById_returnsNull() = runBlockingTest {
        val task = DbTask(
            "TASK1",
            System.currentTimeMillis(),
            0,
            POMODORO.data,
            "Maths Assignment",
            STUDY,
            false
        )
        database.tasksDao.insert(task)
        database.tasksDao.delete(task)

        val loadedTask = database.tasksDao.getById(task.id)
        assertThat(loadedTask, nullValue())
    }

    @Test
    fun insertTaskAndTaskType_returnTaskDetails() = runBlockingTest {
        val task =
            DbTask("TASK1", System.currentTimeMillis(), 0, POMODORO.data, "Maths Assignment", 1, false)
        database.tasksDao.insert(task)

        val taskDetail = database.tasksDao.getTaskDetailsByTaskId(task.id)

        assertThat<DbTaskDetail>(taskDetail, notNullValue())
        assertThat(taskDetail.id, `is`(task.id))
        assertThat(taskDetail.method, `is`(taskMethod.name))
        assertThat(taskDetail.methodIconUrl, `is`(taskMethod.iconUrl))
        assertThat(taskDetail.timeLapse, `is`(taskMethod.workLapse + taskMethod.breakLapse))
        assertThat(taskDetail.title, `is`(task.title))
        assertThat(taskDetail.workStart, `is`(task.startAt))
        assertThat(taskDetail.workEnd, `is`(task.startAt + taskMethod.workLapse))
        assertThat(taskDetail.breakStart, `is`(task.startAt + taskMethod.workLapse))
        assertThat(
            taskDetail.breakEnd, `is`(
                task.startAt + taskMethod.workLapse + taskMethod.breakLapse
            )
        )
    }
}