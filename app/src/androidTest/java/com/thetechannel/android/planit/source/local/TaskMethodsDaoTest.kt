package com.thetechannel.android.planit.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.thetechannel.android.planit.data.source.database.DbTaskMethod
import com.thetechannel.android.planit.data.source.database.PlanItDatabase
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
        val taskMetod = DbTaskMethod(2, "test", 5000, 2000, "https://www.google.com")
        database.taskMethodsDao.insert(taskMetod)

        val loadedTaskType = database.taskMethodsDao.getById(taskMetod.id)

        assertThat<DbTaskMethod>(loadedTaskType as DbTaskMethod, notNullValue())
        assertThat(loadedTaskType.id, `is`(taskMetod.id))
        assertThat(loadedTaskType.name, `is`(taskMetod.name))
        assertThat(loadedTaskType.workLapse, `is`(taskMetod.workLapse))
        assertThat(loadedTaskType.breakLapse, `is`(taskMetod.breakLapse))
        assertThat(loadedTaskType.iconUrl, `is`(taskMetod.iconUrl))
    }
}