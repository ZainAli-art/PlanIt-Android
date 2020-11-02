package com.thetechannel.android.planit.data.source.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
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
    private lateinit var methodsDao: TaskMethodsDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlanItDatabase::class.java
        )
            .build()
        
        methodsDao = database.taskMethodsDao
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskMethods_getAll_returnsAllInsertedTaskMethods() = runBlockingTest {
        val methods = arrayOf(
            DbTaskMethod(1, "Pomodoro", 25000, 5000, "https://www.zaincheema.com"),
            DbTaskMethod(2, "Eat The Devil", 30000, 5000, "https://www.zaincheema.com")
        )
        methods.forEach { methodsDao.insert(it) }

        val loaded = methodsDao.getAll()

        assertThat(loaded.size, `is`(methods.size))
        /** Because the dao's getAll() method returns tasks in asc sorted order by name */
        methods.apply { sortBy { it.name } }
        for (i in methods.indices) {
            val insertedMethod = methods[i]
            val loadedMethod = loaded[i]

            assertThat(loadedMethod.id, `is`(insertedMethod.id))
            assertThat(loadedMethod.name, `is`(insertedMethod.name))
            assertThat(loadedMethod.workLapse, `is`(insertedMethod.workLapse))
            assertThat(
                loadedMethod.breakLapse,
                `is`(insertedMethod.breakLapse)
            )
            assertThat(loadedMethod.iconUrl, `is`(insertedMethod.iconUrl))
        }
    }

    @Test
    fun insertTaskType_getById() = runBlockingTest {
        val taskMethod = DbTaskMethod(2, "test", 5000, 2000, "https://www.google.com")
        methodsDao.insert(taskMethod)

        val loadedTaskMethod = methodsDao.getById(taskMethod.id)

        assertThat<DbTaskMethod>(
            loadedTaskMethod as DbTaskMethod,
            CoreMatchers.notNullValue()
        )
        assertThat(loadedTaskMethod.id, `is`(taskMethod.id))
        assertThat(loadedTaskMethod.name, `is`(taskMethod.name))
        assertThat(loadedTaskMethod.workLapse, `is`(taskMethod.workLapse))
        assertThat(
            loadedTaskMethod.breakLapse,
            `is`(taskMethod.breakLapse)
        )
        assertThat(loadedTaskMethod.iconUrl, `is`(taskMethod.iconUrl))
    }

    @Test
    fun insertTaskMethods_deleteAll_AllInsertedMethodsAreDeleted() = runBlockingTest {
        val methods = arrayOf(
            DbTaskMethod(1, "Pomodoro", 25000, 5000, "https://www.zaincheema.com"),
            DbTaskMethod(2, "Eat The Devil", 30000, 5000, "https://www.zaincheema.com")
        )
        methods.forEach { methodsDao.insert(it) }

        methodsDao.deleteAll()

        val loaded = methodsDao.getAll()
        assertThat(loaded.size, `is`(0))
    }
}