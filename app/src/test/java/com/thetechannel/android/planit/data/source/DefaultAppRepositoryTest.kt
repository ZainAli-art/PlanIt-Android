package com.thetechannel.android.planit.data.source

import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.data.succeeded
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
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
        localDataSource = FakeDataSource(localCategories.toMutableSet(), localMethods.toMutableSet(), localTasks.toMutableSet())
        remoteDataSource = FakeDataSource(remoteCategories.toMutableSet(), remoteMethods.toMutableSet(), remoteTasks.toMutableSet())

        repository = DefaultAppRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun getCategories_requestAllCategoriesFromRemoteDataSource() = runBlockingTest {
        remoteDataSource.categories = remoteCategories.toMutableSet()
        localDataSource.categories = localCategories.toMutableSet()
        val categories = repository.getCategories(true) as Result.Success

        assertThat(categories.data, IsEqual(remoteCategories))
    }

    @Test
    fun getTaskMethods_requestAllMethodsFromRemoteDataSource() = runBlockingTest {
        remoteDataSource.taskMethods = remoteMethods.toMutableSet()
        localDataSource.taskMethods = localMethods.toMutableSet()
        val methods = repository.getTaskMethods(true) as Result.Success

        assertThat(methods.data, IsEqual(remoteMethods))
    }

    @Test
    fun getTasks_requestAllTasksFromRemoteDataSource() = runBlockingTest {
        remoteDataSource.tasks = remoteTasks.toMutableSet()
        localDataSource.tasks = remoteTasks.toMutableSet()
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
        assertThat(remoteDataSource.categories?.contains(category), `is`(true))
        assertThat(localDataSource.categories?.contains(category), `is`(true))
    }

    @Test
    fun saveTaskMethod_savesTaskMethodInRemoteDataSourceAndSyncsWithLocalDataSource() = runBlockingTest {
        val method = TaskMethod(3, "Some Name", Time(500L), Time(300L), URI("http://picture"))
        repository.saveTaskMethod(method)

        /** using true here inserts identical task method in local mutable list (mock) */
        val result = repository.getTaskMethod(method.id, false)
        assertThat(result.succeeded, `is`(true))
        val loaded = (result as Result.Success).data

        assertThat(remoteDataSource.taskMethods?.contains(method), `is`(true))
        assertThat(localDataSource.taskMethods?.contains(method), `is`(true))
    }

    @Test
    fun saveTask_savesTaskInRemoteDataSourceAndSyncsWithLocalDataSource() = runBlockingTest {
        val task = Task("task_1", Calendar.getInstance().time, Time(500L), 1, "some task", 1, false);
        repository.saveTask(task)

        assertThat(remoteDataSource.tasks?.contains(task), `is`(true))
        assertThat(localDataSource.tasks?.contains(task), `is`(true))
    }

    @Test
    fun completeTasks_completesTaskInBothRemoteAndLocalDataSource() = runBlockingTest {
        val task = Task("task_4", Calendar.getInstance().time, Time(500L), 1, "some task", 1, false);
        repository.saveTask(task)

        repository.completeTask(task)

        remoteDataSource.tasks?.forEach {
            if (it.id == task.id) {
                assertThat(it.completed, `is`(true))
            }
        }
        localDataSource.tasks?.forEach {
            if (it.id == task.id) {
                assertThat(it.completed, `is`(true))
            }
        }
    }

    @Test
    fun deleteCategory_deletesCategoryFromRemoteAndLocalDataSource() = runBlockingTest {
        repository.refreshCategories()
        val category = remoteCategories[0]
        repository.deleteCategory(category)

        assertThat(remoteDataSource.categories, not(contains(category)))
        assertThat(localDataSource.categories, not(contains(category)))
    }

    @Test
    fun deleteTaskMethod_deletesTaskMethodFromRemoteAndLocalDataSource() = runBlockingTest {
        repository.refreshTaskMethods()
        val method = remoteMethods[0]
        repository.deleteTaskMethod(method)

        assertThat(remoteDataSource.taskMethods, not(contains(method)))
        assertThat(localDataSource.taskMethods, not(contains(method)))
    }

    @Test
    fun deleteTask_deletesTaskFromRemoteAndLocalDataSource() = runBlockingTest {
        repository.refreshTasks()
        val task = remoteTasks[0]
        repository.deleteTask(task)

        assertThat(remoteDataSource.tasks, not(contains(task)))
        assertThat(localDataSource.tasks, not(contains(task)))
    }
}