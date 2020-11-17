package com.thetechannel.android.planit.newtask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thetechannel.android.planit.Event
import com.thetechannel.android.planit.MainCoroutineRule
import com.thetechannel.android.planit.R
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.FakeTestRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.URI
import java.sql.Time
import java.util.*

@ExperimentalCoroutinesApi
class NewTaskViewModelTest {

    private lateinit var studyCategory: Category
    private lateinit var businessCategory: Category
    private lateinit var sportsCategory: Category
    private lateinit var categories: List<Category>
    private lateinit var pomodoroMethod: TaskMethod
    private lateinit var eatTheDevilMethod: TaskMethod
    private lateinit var methods: List<TaskMethod>

    private lateinit var repository: AppRepository
    private lateinit var viewModel: NewTaskViewModel

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    /** Synchronizes all operations */
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRepository() = runBlocking {
        repository = FakeTestRepository()

        studyCategory = Category(1, "Study")
        businessCategory = Category(2, "Business")
        sportsCategory = Category(3, "Sports")
        categories = listOf(studyCategory, businessCategory, sportsCategory)
        pomodoroMethod = TaskMethod(1, "Pomodoro", Time(25 * 60000), Time(5 * 60000), URI(""))
        eatTheDevilMethod = TaskMethod(2, "Eat The Devil", Time(3 * 60 * 60000), Time(0), URI(""))
        methods = listOf(pomodoroMethod, eatTheDevilMethod)

        repository.saveCategories(studyCategory, businessCategory, sportsCategory)
        repository.saveTaskMethods(pomodoroMethod, eatTheDevilMethod)
        viewModel = NewTaskViewModel(repository)
    }

    @Test
    fun loadCategories_returnsAllInsertedCategories() {
        val loadedCategories: List<Category> = viewModel.categories.getOrAwaitValue()

        loadedCategories.forEach { println(it) }

        assertThat(loadedCategories, IsEqual(categories))
    }

    @Test
    fun loadTaskMethods_returnsAllInsertedTaskMethods() {
        val loadedMethods: List<TaskMethod> = viewModel.taskMethods.getOrAwaitValue()

        assertThat(loadedMethods, IsEqual(methods))
    }

    @Test
    fun loadCategoryNames_returnsNamesOfInsertedCategories() {
        val names: List<String> = viewModel.categoryNames.getOrAwaitValue()

        for (i in categories.indices) {
            assertThat(categories[i].name, `is`(names[i]))
        }
    }

    @Test
    fun loadMethodNames_returnsNamesOfInsertedMethods() {
        val names: List<String> = viewModel.methodNames.getOrAwaitValue()

        for (i in methods.indices) {
            assertThat(methods[i].name, `is`(names[i]))
        }
    }

    @Test
    fun saveNewTask_snackBarTextAndNavigateBackEventUpdated() {
        val task = Task("task", Calendar.getInstance().time, Time(System.currentTimeMillis()), pomodoroMethod.id, "Maths Assignment", studyCategory.id, false)
        viewModel.saveNewTask(task)

        val message: Event<Int> = viewModel.snackBarText.getOrAwaitValue()
        assertThat(message.getContentIfNotHandled(), `is`(R.string.schedule_task_snackbar_text))
        val taskAddedEvent: Event<Boolean> = viewModel.taskAddedEvent.getOrAwaitValue()
        assertThat(taskAddedEvent.getContentIfNotHandled(), `is`(true))
    }
}