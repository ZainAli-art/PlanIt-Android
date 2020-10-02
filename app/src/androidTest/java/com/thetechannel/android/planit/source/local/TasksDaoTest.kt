package com.thetechannel.android.planit.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.thetechannel.android.planit.data.Task
import com.thetechannel.android.planit.data.TaskDetail
import com.thetechannel.android.planit.data.TaskType
import com.thetechannel.android.planit.data.source.local.PlanItDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.sql.Time

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
        val task = Task("TASK1")
        database.tasksDao().insert(task)

        val loadedTask = database.tasksDao().getById(task.id)

        assertThat<Task>(loadedTask as Task, notNullValue())
        assertThat(loadedTask.id, `is`(task.id))
        assertThat(loadedTask.day, `is`(task.day))
        assertThat(loadedTask.startAt, `is`(task.startAt))
        assertThat(loadedTask.typeId, `is`(task.typeId))
    }

    @Test
    fun insertAndDeleteTask_getTaskById_returnsNull() = runBlockingTest {
        val task = Task()
        database.tasksDao().insert(task)
        database.tasksDao().delete(task)

        val loadedTask = database.tasksDao().getById(task.id)
        assertThat(loadedTask, nullValue())
    }

    @Test
    fun insertTaskAndTaskType_returnTaskDetails() = runBlockingTest {
        val task = Task("TASK1")
        val taskType = TaskType()
        database.tasksDao().insert(task)
        database.taskTypesDao().insert(taskType)

        val taskDetail = database.tasksDao().getTaskDetailsByTaskId(task.id)

        assertThat<TaskDetail>(taskDetail, notNullValue())
        assertThat(taskDetail.id, `is`(task.id))
        assertThat(taskDetail.name, `is`(taskType.name))
        assertThat(taskDetail.workStart, `is`(task.startAt))
        assertThat(taskDetail.workEnd, `is`(Time(task.startAt.time + taskType.workLapse.time)))
        assertThat(taskDetail.breakStart, `is`(Time(task.startAt.time + taskType.workLapse.time)))
        assertThat(taskDetail.breakEnd, `is`(Time(
            task.startAt.time + taskType.workLapse.time + taskType.breakLapse.time)))
    }
}