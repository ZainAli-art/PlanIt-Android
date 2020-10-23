package com.thetechannel.android.planit.data.source.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

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
    fun insertCategories_fetchAllCategories_returnsAllInsertedCategories() {
        val categories = arrayOf(
            DbCategory(1, "Study"),
            DbCategory(2, "Sport"),
            DbCategory(3, "Business")
        )
    }
}