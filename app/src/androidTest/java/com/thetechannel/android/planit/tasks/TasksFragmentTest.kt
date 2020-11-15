package com.thetechannel.android.planit.tasks

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.thetechannel.android.planit.R
import com.thetechannel.android.planit.ServiceLocator
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.FakeAndroidTestRepository
import com.thetechannel.android.planit.data.source.domain.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.sql.Time
import java.util.*

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest {
    private lateinit var repository: AppRepository
    private lateinit var task1: Task
    private lateinit var task2: Task
    private lateinit var task3: Task
    private lateinit var task4: Task

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()

        ServiceLocator.repository = repository
    }

    @After
    fun cleanUpRepository() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun passAllTasksFilter_loadsAllTasksInRecyclerView() = runBlockingTest {
        task1 = Task("task_1", Calendar.getInstance().time, Time(1000L), 1, "Maths Assignment", 1, false)
        task2 = Task("task_2", Calendar.getInstance().time, Time(2030L), 1, "Read Emails", 2, true)
        task3 = Task("task_3", Date(23L), Time(4300L), 1, "Half an hour jog", 3, true)
        task4 = Task("task_4", Calendar.getInstance().time, Time(1000L), 1, "Prepare Slides", 1, false)
        repository.saveTasks(task1, task2, task3, task4)

        launchFragmentInContainer<TasksFragment>(TasksFragmentArgs(TaskFilterType.ALL).toBundle(), R.style.AppTheme)

        onView(withId(R.id.tasks_list)).check(matches(isDisplayed()))
        onView(withId(R.id.tasks_list)).check(matches(hasDescendant(withText(task1.title))))
        onView(withId(R.id.tasks_list)).check(matches(hasDescendant(withText(task2.title))))
        onView(withId(R.id.tasks_list)).check(matches(hasDescendant(withText(task3.title))))
        onView(withId(R.id.tasks_list)).check(matches(hasDescendant(withText(task4.title))))
    }
}