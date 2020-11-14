package com.thetechannel.android.planit.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.source.FakeTestRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.sql.Time
import java.util.*

class TasksViewModelTest {

    private lateinit var category: Category
    private lateinit var task3: Task
    private lateinit var task2: Task
    private lateinit var task1: Task
    private lateinit var tasks: List<Task>
    private lateinit var repository: FakeTestRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = runBlocking {
        repository = FakeTestRepository()
        task1 =
            Task("task_1", Date(25L), Time(100), 1, "Clean my room", 1, true)
        task2 = Task(
            "task_2",
            Calendar.getInstance().time,
            Time(1500),
            2,
            "Maths Assignment",
            1,
            true
        )
        task3 = Task(
            "task_3",
            Calendar.getInstance().time,
            Time(5000),
            2,
            "Read Emails",
            1,
            false
        )
        category = Category(1, "Study")

        repository.saveCategory(category)
        tasks = listOf(task1, task2, task3)
        tasks.forEach { repository.saveTask(it) }
    }

    @Test
    fun setFilteringAsAll_setsUpTasksAsAllInsertedTasks() {
        val viewModel = TasksViewModel(repository)

        viewModel.setFiltering(TaskFilterType.ALL)

        val loaded = viewModel.tasks.getOrAwaitValue().sortedBy { it.id }

        assertThat(loaded.size, `is`(3))
        assertThat(loaded, IsEqual(tasks))
    }

    @Test
    fun setFilteringAsPending_setsUpTasksAsPendingTasks() {
        val viewModel = TasksViewModel(repository)
        viewModel.setFiltering(TaskFilterType.PENDING)

        val loaded = viewModel.tasks.getOrAwaitValue().sortedBy { it.id }

        assertThat(loaded.size, `is`(1))
        assertThat(loaded[0], `is`(task3))
    }

    @Test
    fun setFilteringAsCompleted_setsUpTasksAsCompletedTasks() {
        val viewModel = TasksViewModel(repository)
        viewModel.setFiltering(TaskFilterType.COMPLETED)

        val loaded = viewModel.tasks.getOrAwaitValue().sortedBy { it.id }

        assertThat(loaded.size, `is`(2))
        assertThat(loaded, IsEqual(listOf(task1, task2)))
    }

    @Test
    fun setFilteringAsCompletedToday_setsUpTasksAsThoseCompletedToday() {
        val viewModel = TasksViewModel(repository)
        viewModel.setFiltering(TaskFilterType.COMPLETED_TODAY)

        val loaded = viewModel.tasks.getOrAwaitValue().sortedBy { it.id }

        assertThat(loaded.size, `is`(1))
        assertThat(loaded[0], IsEqual(task2))
    }
}