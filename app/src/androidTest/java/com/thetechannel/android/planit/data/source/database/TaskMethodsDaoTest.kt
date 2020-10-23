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
class TaskMethodsDaoTest {
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
        val taskMethod = DbTaskMethod(2, "test", 5000, 2000, "https://www.google.com")
        database.taskMethodsDao.insert(taskMethod)

        val loadedTaskType = database.taskMethodsDao.getById(taskMethod.id)

        MatcherAssert.assertThat<DbTaskMethod>(
            loadedTaskType as DbTaskMethod,
            CoreMatchers.notNullValue()
        )
        MatcherAssert.assertThat(loadedTaskType.id, CoreMatchers.`is`(taskMethod.id))
        MatcherAssert.assertThat(loadedTaskType.name, CoreMatchers.`is`(taskMethod.name))
        MatcherAssert.assertThat(loadedTaskType.workLapse, CoreMatchers.`is`(taskMethod.workLapse))
        MatcherAssert.assertThat(
            loadedTaskType.breakLapse,
            CoreMatchers.`is`(taskMethod.breakLapse)
        )
        MatcherAssert.assertThat(loadedTaskType.iconUrl, CoreMatchers.`is`(taskMethod.iconUrl))
    }
}