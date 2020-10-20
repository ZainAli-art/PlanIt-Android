package com.thetechannel.android.planit.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.thetechannel.android.planit.data.source.database.DbCategory
import com.thetechannel.android.planit.data.source.database.PlanItDatabase
import com.thetechannel.android.planit.data.source.domain.CategoryId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
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
        val category = DbCategory(CategoryId.STUDY.data, "Study")

        database.categoriesDao.insert(category)

        val loaded = database.categoriesDao.getById(category.id)

        assertThat<DbCategory>(loaded as DbCategory, `is`(notNullValue()))
        assertThat(loaded.id, `is`(category.id))
        assertThat(loaded.name, `is`(category.name))
    }

    @Test
    fun insertCategories_fetchAll_returnsAllInsertedCategories() = runBlockingTest {
        val categories = arrayOf(
            DbCategory(CategoryId.STUDY.data, "Study"),
            DbCategory(CategoryId.BUSINESS.data, "Business"),
            DbCategory(CategoryId.SPORT.data, "Sport"),
            DbCategory(CategoryId.HOBBY.data, "Hobby")
        )
        database.categoriesDao.insertAll(*categories)

        val loaded = database.categoriesDao.getAll()

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
        val category = DbCategory(CategoryId.STUDY.data, "Study")
        database.categoriesDao.insert(category)

        database.categoriesDao.delete(category)
        val loaded = database.categoriesDao.getById(category.id)

        assertThat(loaded, `is`(nullValue()))
    }
}