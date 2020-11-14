package com.thetechannel.android.planit.home

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.thetechannel.android.planit.R
import com.thetechannel.android.planit.TaskFilterType
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
class HomeFragmentTest {

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
}