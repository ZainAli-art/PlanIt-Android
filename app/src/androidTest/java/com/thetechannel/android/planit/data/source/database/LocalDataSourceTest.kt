package com.thetechannel.android.planit.data.source.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.succeeded
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Test

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
    fun insertCategories_fetchAllCategories_returnsAllInsertedCategories() = runBlocking {
        val categories = arrayOf(
            Category(1, "Study"),
            Category(2, "Sport"),
            Category(3, "Business")
        )
        dataSource.insertCategories(*categories)

        val result = dataSource.getAllCategories()
        assertThat(result.succeeded, `is`(true))
        result as Result.Success

        val loaded = result.data

        assertThat(categories.size, `is`(loaded.size))
        for (i in categories.indices) {
            val insertedCat = categories.get(i)
            val loadedCat = loaded.get(i)

            assertThat(insertedCat.id, `is`(loadedCat.id))
            assertThat(insertedCat.name, `is`(loadedCat.name))
        }
    }

    @Test
    fun insertCategory_fetchById_returnsInsertedCategory() = runBlocking {
        val category = Category(1, "Business")
        dataSource.insertCategory(category)

        val result = dataSource.getCategoryById(category.id)
        assertThat(result.succeeded, `is`(true))
        result as Result.Success<Category>

        assertThat(category.id, `is`(result.data.id))
        assertThat(category.name, `is`(result.data.name))
    }
}