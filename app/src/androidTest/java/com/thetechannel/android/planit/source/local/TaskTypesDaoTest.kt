package com.thetechannel.android.planit.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.thetechannel.android.planit.data.POMODORRO
import com.thetechannel.android.planit.data.TaskType
import com.thetechannel.android.planit.data.source.local.PlanItDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
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
class TaskTypesDaoTest {
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
    fun insertTaskType_getById() = runBlockingTest {
        val taskType = TaskType(2, "test", Time(5000), Time(2000))
        database.taskTypesDao().insert(taskType)

        val loadedTaskType = database.taskTypesDao().getById(taskType.id)

        assertThat<TaskType>(loadedTaskType as TaskType, notNullValue())
        assertThat(loadedTaskType.id, `is`(taskType.id))
        assertThat(loadedTaskType.name, `is`(taskType.name))
        assertThat(loadedTaskType.workLapse, `is`(taskType.workLapse))
        assertThat(loadedTaskType.breakLapse, `is`(taskType.breakLapse))
    }
}