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
class CategoriesDaoTest {
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
    fun insertCategory_fetchById_returnsInsertedCategory() = runBlockingTest {
        val category = DbCategory(1, "Study")

        database.categoriesDao.insert(category)

        val loaded = database.categoriesDao.getById(category.id)

        MatcherAssert.assertThat<DbCategory>(
            loaded as DbCategory,
            CoreMatchers.`is`(CoreMatchers.notNullValue())
        )
        MatcherAssert.assertThat(loaded.id, CoreMatchers.`is`(category.id))
        MatcherAssert.assertThat(loaded.name, CoreMatchers.`is`(category.name))
    }

    @Test
    fun insertCategories_fetchAll_returnsAllInsertedCategories() = runBlockingTest {
        val categories = arrayOf(
            DbCategory(1, "Study"),
            DbCategory(2, "Business"),
            DbCategory(3, "Sport"),
            DbCategory(4, "Hobby")
        )
        database.categoriesDao.insertAll(*categories)

        val loaded = database.categoriesDao.getAll()
        categories.sortBy { c -> c.name }

        MatcherAssert.assertThat(loaded.size, CoreMatchers.`is`(categories.size))
        for (i in categories.indices) {
            val insertedCategory = categories[i]
            val loadedCategory = loaded[i]

            MatcherAssert.assertThat(insertedCategory.id, CoreMatchers.`is`(loadedCategory.id))
            MatcherAssert.assertThat(insertedCategory.name, CoreMatchers.`is`(loadedCategory.name))
        }
    }

    @Test
    fun insertCategory_deleteItAndFetchById_returnsNull() = runBlockingTest {
        val category = DbCategory(1, "Study")
        database.categoriesDao.insert(category)

        database.categoriesDao.delete(category)
        val loaded = database.categoriesDao.getById(category.id)

        MatcherAssert.assertThat(loaded, CoreMatchers.`is`(CoreMatchers.nullValue()))
    }
}