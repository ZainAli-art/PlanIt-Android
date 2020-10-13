package com.thetechannel.android.planit.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.thetechannel.android.planit.data.source.database.DbTask
import com.thetechannel.android.planit.data.source.database.DbTaskDetail
import com.thetechannel.android.planit.data.source.database.DbTaskType
import com.thetechannel.android.planit.data.source.database.PlanItDatabase
import com.thetechannel.android.planit.data.source.network.POMODORRO
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
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PlanItDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlanItDatabase::class.java
        )
            .build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTask_getById() = runBlockingTest {
        val task = DbTask("TASK1", System.currentTimeMillis(), 0, 0)
        database.tasksDao().insert(task)

        val loadedTask = database.tasksDao().getById(task.id)

        assertThat<DbTask>(loadedTask as DbTask, notNullValue())
        assertThat(loadedTask.id, `is`(task.id))
        assertThat(loadedTask.day, `is`(task.day))
        assertThat(loadedTask.startAt, `is`(task.startAt))
        assertThat(loadedTask.typeId, `is`(task.typeId))
    }

    @Test
    fun insertAndDeleteTask_getTaskById_returnsNull() = runBlockingTest {
        val task = DbTask("task_1", 1, 0, POMODORRO)
        database.tasksDao().insert(task)
        database.tasksDao().delete(task)

        val loadedTask = database.tasksDao().getById(task.id)
        assertThat(loadedTask, nullValue())
    }

    @Test
    fun insertTaskAndTaskType_returnTaskDetails() = runBlockingTest {
        val task = DbTask("TASK1", 0, 0, POMODORRO)
        val taskType = DbTaskType(POMODORRO, "pomodorro", 0, 0)
        database.tasksDao().insert(task)
        database.taskTypesDao().insert(taskType)

        val taskDetail = database.tasksDao().getTaskDetailsByTaskId(task.id)

        assertThat<DbTaskDetail>(taskDetail, notNullValue())
        assertThat(taskDetail.id, `is`(task.id))
        assertThat(taskDetail.name, `is`(taskType.name))
        assertThat(taskDetail.workStart, `is`(task.startAt))
        assertThat(taskDetail.workEnd, `is`(task.startAt + taskType.workLapse))
        assertThat(taskDetail.breakStart, `is`(task.startAt + taskType.workLapse))
        assertThat(taskDetail.breakEnd, `is`(
            task.startAt + taskType.workLapse + taskType.breakLapse))
    }
}