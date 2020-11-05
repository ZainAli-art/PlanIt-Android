package com.thetechannel.android.planit.data.source

import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.data.succeeded
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.contains
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import java.net.URI
import java.sql.Time
import java.util.*

@ExperimentalCoroutinesApi
class DefaultAppRepositoryTest {

    private val cat1 = Category(1, "Study")
    private val cat2 = Category(2, "Business")
    private val cat3 = Category(3, "Sport")
    val localCategories = listOf(cat1, cat2)
    val remoteCategories = listOf(cat3)

    private val method1 = TaskMethod(1, "Pomodoro", Time(25000L), Time(5000L), URI("https://none"))
    private val method2 = TaskMethod(1, "Eat The Devil", Time(30000L), Time(5000L), URI("https://none"))
    val localMethods = listOf(method1)
    val remoteMethods = listOf(method1, method2)


    private val task1 = Task("task_1", Calendar.getInstance().time, Time(0L), method1.id, "Maths Assignment", cat1.id, false)
    private val task2 = Task("task_2", Calendar.getInstance().time, Time(0L), method1.id, "Email", cat2.id, false)
    private val task3 = Task("task_3", Calendar.getInstance().time, Time(0L), method1.id, "Sprint", cat3.id, false)
    val localTasks = listOf(task1)
    val remoteTasks = listOf(task2, task3)

    private lateinit var localDataSource: FakeDataSource
    private lateinit var remoteDataSource: FakeDataSource

    private lateinit var repository: DefaultAppRepository

    @Before
    fun createRepository() {
        localDataSource = FakeDataSource(mutableListOf(), mutableListOf(), mutableListOf())
        remoteDataSource = FakeDataSource(mutableListOf(), mutableListOf(), mutableListOf())

        repository = DefaultAppRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun getCategories_requestAllCategoriesFromRemoteDataSource() = runBlockingTest {
        remoteDataSource.categories = remoteCategories.toMutableList()
        localDataSource.categories = localCategories.toMutableList()
        val categories = repository.getCategories(true) as Result.Success

        assertThat(categories.data, IsEqual(remoteCategories))
    }

    @Test
    fun getTaskMethods_requestAllMethodsFromRemoteDataSource() = runBlockingTest {
        remoteDataSource.taskMethods = remoteMethods.toMutableList()
        localDataSource.taskMethods = localMethods.toMutableList()
        val methods = repository.getTaskMethods(true) as Result.Success

        assertThat(methods.data, IsEqual(remoteMethods))
    }

    @Test
    fun getTasks_requestAllTasksFromRemoteDataSource() = runBlockingTest {
        remoteDataSource.tasks = remoteTasks.toMutableList()
        localDataSource.tasks = remoteTasks.toMutableList()
        val tasks = repository.getTasks(true) as Result.Success

        assertThat(tasks.data, IsEqual(remoteTasks))
    }

    @Test
    fun saveCategory_savesCategoryInRemoteDataSourceAndSyncsWithLocalDataSource() = runBlockingTest {
        val category = Category(4, "Other")
        repository.saveCategory(category)

        /** using true here inserts identical category in local mutable list (mock) */
        val result = repository.getCategory(category.id, false)
        assertThat(result.succeeded, `is`(true))
        val loaded = (result as Result.Success).data

        assertThat(loaded, `is`(category))
        assertThat(remoteDataSource.categories, contains(loaded))
        assertThat(localDataSource.categories, contains(loaded))
    }

    @Test
    fun saveTaskMethod_savesTaskMethodInRemoteDataSourceAndSyncsWithLocalDataSource() = runBlockingTest {
        val method = TaskMethod(3, "Some Name", Time(500L), Time(300L), URI("http://picture"))
        repository.saveTaskMethod(method)

        /** using true here inserts identical task method in local mutable list (mock) */
        val result = repository.getTaskMethod(method.id, false)
        assertThat(result.succeeded, `is`(true))
        val loaded = (result as Result.Success).data

        assertThat(remoteDataSource.taskMethods?.toList(), contains(loaded))
        assertThat(localDataSource.taskMethods?.toList(), contains(loaded))
    }
}