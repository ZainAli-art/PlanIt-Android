package com.thetechannel.android.planit.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thetechannel.android.planit.MainCoroutineRule
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.source.FakeTestRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.data.source.domain.TasksOverView
import com.thetechannel.android.planit.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.net.URI
import java.sql.Time
import java.util.*

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var task1: Task
    private lateinit var task2: Task
    private lateinit var task3: Task
    private lateinit var category: Category
    private lateinit var method: TaskMethod

    private lateinit var repository: FakeTestRepository
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule =  MainCoroutineRule()

    @Before
    fun setUp() = runBlocking {
        repository = FakeTestRepository()
        task1 = Task(UUID.randomUUID().toString(), Calendar.getInstance().time, Time(1500), 1, "Maths Assignment", 1, true)
        task2 = Task(UUID.randomUUID().toString(), Calendar.getInstance().time, Time(1540), 1, "Read Emails", 1, false)
        task3 = Task(UUID.randomUUID().toString(), Date(25L), Time(100), 1, "Clean my room", 1, true)
        category = Category(1, "Study")
        method = TaskMethod(1, "Pomodoro", Time(25 * 60000), Time(5 * 60000), URI("null"))

        repository.saveCategory(category)
        repository.saveTaskMethod(method)
        arrayOf(task1, task2, task3).forEach { repository.saveTask(it) }

        viewModel = HomeViewModel(repository)
    }

    @Test
    fun getTasksOverview_returnsOnePendingOneCompletedTodayAndTwoCompletedTasks() {
        val overview: TasksOverView = viewModel.tasksOverView.getOrAwaitValue()

        assertThat(overview.pendingTasks, `is`(1))
        assertThat(overview.completedTasks, `is`(2))
        assertThat(overview.tasksCompletedToday, `is`(1))
    }

    @Test
    fun getTodayProgress_returnsFifty() {
        val progress = viewModel.todayProgress.getOrAwaitValue()

        assertThat(progress, `is`(50))
    }

    @Test
    fun getTodayPieEntries_returnsTodayPieEntriesLiveData() {
        val entries = viewModel.todayPieEntries.getOrAwaitValue()

        assertThat(entries.size, `is`(1))
        assertThat(entries[0].value, `is`(2f))
        assertThat(entries[0].label, `is`(category.name))
    }

    @Test
    fun openPendingTasks_setsUpLoadTasksEventToLoadPendingTasks() {
        viewModel.openPendingTasks()

        val event = viewModel.openTasksEvent.getOrAwaitValue()

        assertThat(event.getContentIfNotHandled(), `is`(notNullValue()))
        assertThat(event.peekContent(), `is`(TaskFilterType.PENDING))
    }

    @Test
    fun openCompletedTasks_setsUpLoadTasksEventToLoadCompletedTasks() {
        viewModel.openCompletedTasks()

        val event = viewModel.openTasksEvent.getOrAwaitValue()

        assertThat(event.getContentIfNotHandled(), `is`(notNullValue()))
        assertThat(event.peekContent(), `is`(TaskFilterType.COMPLETED))
    }

    @Test
    fun openCompletedTodayTasks_setsUpLoadTaskEventToCompletedTodayTasks() {
        viewModel.openTasksCompletedToday()

        val event = viewModel.openTasksEvent.getOrAwaitValue()

        assertThat(event.getContentIfNotHandled(), `is`(notNullValue()))
        assertThat(event.peekContent(), `is`(TaskFilterType.COMPLETED_TODAY))
    }

    @Test
    fun loadData_Loading() {
        mainCoroutineRule.pauseDispatcher()

        viewModel.refresh()

        assertThat(viewModel.dataLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        assertThat(viewModel.dataLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun refreshWhenDataUnavailable_callsErrorsToDisplay() {
        repository.setReturnError(true)
        viewModel.refresh()

        assertThat(viewModel.error.getOrAwaitValue(), `is`(true))
        assertThat(viewModel.empty.getOrAwaitValue(), `is`(true))
    }
}