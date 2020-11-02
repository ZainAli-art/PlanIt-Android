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
class CategoriesDaoTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PlanItDatabase
    private lateinit var categoriesDao: CategoriesDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlanItDatabase::class.java
        )
            .build()

        categoriesDao = database.categoriesDao
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertCategory_fetchById_returnsInsertedCategory() = runBlockingTest {
        val category = DbCategory(1, "Study")

        categoriesDao.insert(category)

        val loaded = categoriesDao.getById(category.id)

        assertThat<DbCategory>(
            loaded as DbCategory,
            `is`(CoreMatchers.notNullValue())
        )
        assertThat(loaded.id, `is`(category.id))
        assertThat(loaded.name, `is`(category.name))
    }

    @Test
    fun insertCategories_fetchAll_returnsAllInsertedCategories() = runBlockingTest {
        val categories = arrayOf(
            DbCategory(1, "Study"),
            DbCategory(2, "Business"),
            DbCategory(3, "Sport"),
            DbCategory(4, "Hobby")
        )
        categoriesDao.insertAll(*categories)

        val loaded = categoriesDao.getAll()
        categories.sortBy { c -> c.name }

        assertThat(loaded.size, `is`(categories.size))
        for (i in categories.indices) {
            val insertedCategory = categories[i]
            val loadedCategory = loaded[i]

            assertThat(insertedCategory.id, `is`(loadedCategory.id))
            assertThat(insertedCategory.name, `is`(loadedCategory.name))
        }
    }

    @Test
    fun insertCategory_deleteItAndFetchById_returnsNull() = runBlockingTest {
        val category = DbCategory(1, "Study")
        categoriesDao.insert(category)

        categoriesDao.delete(category)
        val loaded = categoriesDao.getById(category.id)

        assertThat(loaded, `is`(CoreMatchers.nullValue()))
    }

    @Test
    fun insertCategories_deleteAll_allCategoriesAreDeleted() = runBlockingTest {
        val categories = arrayOf(
            DbCategory(1, "Study"),
            DbCategory(2, "Business"),
            DbCategory(3, "Sport")
        )
        categoriesDao.insertAll(*categories)

        categoriesDao.deleteAll()

        val loaded: List<DbCategory> = categoriesDao.getAll()
        assertThat(loaded.size, `is`(0))
    }
}