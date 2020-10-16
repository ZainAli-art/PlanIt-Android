package com.thetechannel.android.planit.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.MatcherAssert.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.thetechannel.android.planit.data.source.database.DbDay
import com.thetechannel.android.planit.data.source.database.PlanItDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DaysDaoTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

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
    fun insertDay_getByDate() = runBlockingTest {
        val day = DbDay(System.currentTimeMillis(), 0, 0)

        database.daysDao.insert(day)

        val loadedDay = database.daysDao.getByDate(day.date)

        assertThat<DbDay>(loadedDay as DbDay, notNullValue())
        assertThat(loadedDay.date, `is`(day.date))
        assertThat(loadedDay.startAt, `is`(day.startAt))
        assertThat(loadedDay.endAt, `is`(day.endAt))
    }
}