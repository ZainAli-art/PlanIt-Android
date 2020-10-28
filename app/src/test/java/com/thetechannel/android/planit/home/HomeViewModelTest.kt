package com.thetechannel.android.planit.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thetechannel.android.planit.FakeAndroidTestRepository
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.sql.Time
import java.util.*

class HomeViewModelTest {

    private lateinit var repository: FakeAndroidTestRepository
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repository = FakeAndroidTestRepository()
        val task1 = Task(UUID.randomUUID().toString(), Calendar.getInstance().time, Time(1500), 1, "Maths Assignment", 1, true)
        val task2 = Task(UUID.randomUUID().toString(), Calendar.getInstance().time, Time(1540), 1, "Read Emails", 1, false)
        val task3 = Task(UUID.randomUUID().toString(), Date(25L), Time(100), 1, "Clean my room", 1, true)

        repository.addTasks(task1, task2, task3)

        viewModel = HomeViewModel(repository)
    }

    @Test
    fun getPendingTasks_returnsOne() {
        val tasks: Int = viewModel.pendingTasks.getOrAwaitValue().size

        assertThat(tasks, `is`(1))
    }

    @Test
    fun getTasksCompletedToday_returnsOne() {
        val tasks: Int = viewModel.tasksCompletedToday.getOrAwaitValue().size

        assertThat(tasks, `is`(1))
    }

    @Test
    fun getCompletedTasks_returnsTwo() {
        val tasks: Int = viewModel.completedTasks.getOrAwaitValue().size

        assertThat(tasks, `is`(2))
    }

    @Test
    fun getTodayProgress_returnsFifty() {
        val progress: Int = viewModel.todayProgress.getOrAwaitValue()

        assertThat(progress, `is`(50))
    }
}