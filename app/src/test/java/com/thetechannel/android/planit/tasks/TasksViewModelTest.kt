package com.thetechannel.android.planit.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.source.FakeTestRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.URI
import java.sql.Time
import java.util.*

class TasksViewModelTest {

    private lateinit var category: Category
    private lateinit var method: TaskMethod
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
        category = Category(1, "Study")
        method = TaskMethod(1, "Pomodoro", Time(25 * 60000), Time(5 * 60000), URI("http://no-url"))
        task1 = Task(
            "task_1",
            Date(25L),
            Time(100),
            method.id,
            "Clean my room",
            category.id,
            true
        )
        task2 = Task(
            "task_2",
            Calendar.getInstance().time,
            Time(1500),
            method.id,
            "Maths Assignment",
            category.id,
            true
        )
        task3 = Task(
            "task_3",
            Calendar.getInstance().time,
            Time(5000),
            method.id,
            "Read Emails",
            category.id,
            false
        )

        repository.saveCategory(category)
        repository.saveTaskMethod(method)
        tasks = listOf(task1, task2, task3)
        tasks.forEach { repository.saveTask(it) }
    }

    @Test
    fun setFilteringAsAll_setsUpTaskDetailsFromAllInsertedTasks() {
        val viewModel = TasksViewModel(repository)

        viewModel.setFiltering(TaskFilterType.ALL)
        val details: List<TaskDetail> = viewModel.taskDetails.getOrAwaitValue()

        assertThat(details.size, `is`(3))
        details[0].assertBelongsTo(category, method, task1)
        details[1].assertBelongsTo(category, method, task2)
        details[2].assertBelongsTo(category, method, task3)
    }

    @Test
    fun setFilteringAsPending_setsUpTaskDetailsFromPendingTasks() {
        val viewModel = TasksViewModel(repository)
        viewModel.setFiltering(TaskFilterType.PENDING)

        val loaded = viewModel.taskDetails.getOrAwaitValue()

        assertThat(loaded.size, `is`(1))
        loaded[0].assertBelongsTo(category, method, task3)
    }

    @Test
    fun setFilteringAsCompleted_setsUpTaskDetailsFromCompletedTasks() {
        val viewModel = TasksViewModel(repository)
        viewModel.setFiltering(TaskFilterType.COMPLETED)

        val loaded = viewModel.taskDetails.getOrAwaitValue()

        assertThat(loaded.size, `is`(2))
        loaded[0].assertBelongsTo(category, method, task1)
        loaded[1].assertBelongsTo(category, method, task2)
    }

    @Test
    fun setFilteringAsCompletedToday_setsUpTaskDetailsFromCompletedTodayTasks() {
        val viewModel = TasksViewModel(repository)
        viewModel.setFiltering(TaskFilterType.COMPLETED_TODAY)

        val loaded = viewModel.taskDetails.getOrAwaitValue()

        assertThat(loaded.size, `is`(1))
        loaded[0].assertBelongsTo(category, method, task2)
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

    private fun TaskDetail.assertBelongsTo(category: Category, method: TaskMethod, task: Task) {
        assertThat(id, `is`(task.id))
        assertThat(categoryName, `is`(category.name))
        assertThat(methodName, `is`(method.name))
        assertThat(methodIconUrl, `is`(method.iconUrl))
        assertThat(
            timeLapse,
            `is`(Time(method.workLapse.time + method.breakLapse.time))
        )
        assertThat(title, `is`(task.title))
        assertThat(workStart, `is`(task.startAt))
        assertThat(
            workEnd,
            `is`(Time(task.startAt.time + method.workLapse.time))
        )
        assertThat(
            breakStart,
            `is`(Time(task.startAt.time + method.workLapse.time))
        )
        assertThat(
            breakEnd,
            `is`(Time(task.startAt.time + method.workLapse.time + method.breakLapse.time))
        )
        assertThat(completed, `is`(task.completed))
    }
}