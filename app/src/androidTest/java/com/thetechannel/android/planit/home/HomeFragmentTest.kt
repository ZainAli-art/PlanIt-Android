package com.thetechannel.android.planit.home

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.thetechannel.android.planit.R
import com.thetechannel.android.planit.ServiceLocator
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.FakeAndroidTestRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.net.URI
import java.sql.Time
import java.util.*


@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class HomeFragmentTest {

    private lateinit var category: Category
    private lateinit var method: TaskMethod

    private lateinit var repository: AppRepository

    @Before
    fun initRepository() = runBlockingTest {
        repository = FakeAndroidTestRepository()
        category = Category(1, "Study")
        method = TaskMethod(1, "Pomodoro", Time(25 * 60000), Time(5 * 60000), URI("null"))

        repository.saveCategory(category)
        repository.saveTaskMethod(method)

        ServiceLocator.repository = repository
    }

    @After
    fun cleanUpRepository() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun insertTasks_updatesTasksCountInTasksOverview() = runBlockingTest {
        arrayOf(
            Task("task_1", Calendar.getInstance().time, Time(1000L), method.id, "Maths Assignment", category.id, false),
            Task("task_2", Calendar.getInstance().time, Time(2030L), method.id, "Read Emails", category.id, true),
            Task("task_3", Date(23L), Time(4300L), 1, "Half an hour jog", category.id, true),
            Task("task_4", Calendar.getInstance().time, Time(1000L), method.id, "Prepare Slides", category.id, false)
        ).forEach { repository.saveTask(it) }

        val scenario = launchFragmentInContainer<HomeFragment>(Bundle(), R.style.AppTheme)

        onView(withId(R.id.pendingTasks)).check(matches(isDisplayed()))
        onView(withId(R.id.pendingTasks)).check(matches(withText("2")))
        onView(withId(R.id.completedTasks)).check(matches(isDisplayed()))
        onView(withId(R.id.completedTasks)).check(matches(withText("2")))
        onView(withId(R.id.completedTodayTasks)).check(matches(isDisplayed()))
        onView(withId(R.id.completedTodayTasks)).check(matches(withText("1")))
    }

    @Test
    fun clickPendingTasks_NavigateToTasksFragmentWithPendingFilterType() {
        val scenario = launchFragmentInContainer<HomeFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.pendingTasksContainer)).perform(click())

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToTasksFragment(TaskFilterType.PENDING)
        )
    }

    @Test
    fun clickCompletedToday_NavigateToTasksFragmentWithCompletedTodayFilterType() {
        val scenario = launchFragmentInContainer<HomeFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.completedTodayTasksContainer)).perform(click())

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToTasksFragment(TaskFilterType.COMPLETED_TODAY)
        )
    }

    @Test
    fun clickCompletedTasks_NavigateToTasksFragmentWithCompletedFilterType() {
        val scenario = launchFragmentInContainer<HomeFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.completedTasks)).perform(click())

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToTasksFragment(TaskFilterType.COMPLETED)
        )
    }

    @Test
    fun clickAddNewTaskFab_navigatesToNewTaskFragment() {
        val scenario = launchFragmentInContainer<HomeFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.addNewTaskFab))
            .check(matches(isDisplayed()))
            .perform(click())

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToNewTaskFragment()
        )
    }
}