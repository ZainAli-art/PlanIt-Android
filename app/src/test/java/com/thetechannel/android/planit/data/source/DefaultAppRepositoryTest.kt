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
    private val task2 = Task("task_2", Calendar.getInstance().time, Time(0L), method1.id, "Email", cat3.id, false)
    private val task3 = Task("task_3", Calendar.getInstance().time, Time(0L), method1.id, "Sprint", cat3.id, true)
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
    fun getTaskDetailById_returnsTaskDetailFromRemoteDataSource() = runBlockingTest {
        val category = remoteCategories[0]
        val method = remoteMethods[0]
        val task = remoteTasks[0]
        val result = repository.getTaskDetail(task.id, true)

        assertThat(result.succeeded, `is`(true))
        val detail = (result as Result.Success).data

        assertThat(detail.id, `is`(task.id))
        assertThat(detail.categoryName, `is`(category.name))
        assertThat(detail.methodName, `is`(method.name))
        assertThat(detail.methodIconUrl, `is`(method.iconUrl))
        assertThat(
            detail.timeLapse.time,
            `is`(method.workLapse.time + method.breakLapse.time)
        )
        assertThat(detail.title, `is`(task.title))
        assertThat(detail.workStart, `is`(task.startAt))
        assertThat(
            detail.workEnd.time,
            `is`(task.startAt.time + method.workLapse.time)
        )
        assertThat(
            detail.breakStart.time,
            `is`(task.startAt.time + method.workLapse.time)
        )
        assertThat(
            detail.breakEnd.time,
            `is`(task.startAt.time + method.workLapse.time + method.breakLapse.time)
        )
    }

    @Test
    fun getTaskDetails_requestsAllTaskDetailsFromRemoteDataSource() = runBlockingTest {
        val result = repository.getTaskDetails(true)

        assertThat(result.succeeded, `is`(true))
        val details = (result as Result.Success).data
        assertThat(details.size, `is`(2))

        val detail1 = details[0]
        assertThat(detail1.id, `is`(task2.id))
        assertThat(detail1.categoryName, `is`(cat3.name))
        assertThat(detail1.methodName, `is`(method1.name))
        assertThat(detail1.methodIconUrl, `is`(method1.iconUrl))
        assertThat(
            detail1.timeLapse.time,
            `is`(method1.workLapse.time + method1.breakLapse.time)
        )
        assertThat(detail1.title, `is`(task2.title))
        assertThat(detail1.workStart, `is`(task2.startAt))
        assertThat(
            detail1.workEnd.time,
            `is`(task2.startAt.time + method1.workLapse.time)
        )
        assertThat(
            detail1.breakStart.time,
            `is`(task2.startAt.time + method1.workLapse.time)
        )
        assertThat(
            detail1.breakEnd.time,
            `is`(task2.startAt.time + method1.workLapse.time + method1.breakLapse.time)
        )
        val detail2 = details[1]
        assertThat(detail2.id, `is`(task3.id))
        assertThat(detail2.categoryName, `is`(cat3.name))
        assertThat(detail2.methodName, `is`(method1.name))
        assertThat(detail2.methodIconUrl, `is`(method1.iconUrl))
        assertThat(
            detail2.timeLapse.time,
            `is`(method1.workLapse.time + method1.breakLapse.time)
        )
        assertThat(detail2.title, `is`(task3.title))
        assertThat(detail2.workStart, `is`(task3.startAt))
        assertThat(
            detail2.workEnd.time,
            `is`(task3.startAt.time + method1.workLapse.time)
        )
        assertThat(
            detail2.breakStart.time,
            `is`(task3.startAt.time + method1.workLapse.time)
        )
        assertThat(
            detail2.breakEnd.time,
            `is`(task3.startAt.time + method1.workLapse.time + method1.breakLapse.time)
        )
    }

    @Test
    fun getTasksOverview_returnsTasksOverviewFromRemoteDataSource() = runBlockingTest {
        val result = repository.getTasksOverView(true)

        assertThat(result.succeeded, `is`(true))
        val overview = (result as Result.Success).data
        assertThat(overview.completedTasks, `is`(1))
        assertThat(overview.pendingTasks, `is`(1))
        assertThat(overview.tasksCompletedToday, `is`(1))
    }

    @Test
    fun getTodayProgress_requestsTodayProgressFromRemoteDataSource() = runBlockingTest {
        val result = repository.getTodayProgress(true)

        assertThat(result.succeeded, `is`(true))
        val progress = (result as Result.Success).data

        assertThat(progress.percentage, `is`(50))
    }

    @Test
    fun getTodayPieEntries_requestsTodayPieEntriesFromRemoteDataSource() = runBlockingTest {
        val result = repository.getTodayPieEntries(true)

        assertThat(result.succeeded, `is`(true))
        val entries = (result as Result.Success).data

        assertThat(entries.size, `is`(1))
        assertThat(entries[0].value, `is`(2f))
        assertThat(entries[0].label, `is`(cat3.name))
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