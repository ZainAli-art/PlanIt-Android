package com.thetechannel.android.planit.tasks

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch
import androidx.test.espresso.contrib.RecyclerViewActions
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
import java.net.URI
import java.sql.Time
import java.util.*

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest {
    private lateinit var repository: AppRepository
    private lateinit var studyCategory: Category
    private lateinit var businessCategory: Category
    private lateinit var sportCategory: Category
    private lateinit var method: TaskMethod
    private lateinit var task1: Task
    private lateinit var task2: Task
    private lateinit var task3: Task
    private lateinit var task4: Task

    @Before
    fun initRepository() = runBlockingTest {
        repository = FakeAndroidTestRepository()
        ServiceLocator.repository = repository

        val startTime = Time(1605431549682L)

        studyCategory = Category(1, "Study")
        businessCategory = Category(2, "Business")
        sportCategory = Category(3, "Business")
        method = TaskMethod(1, "Pomodoro", Time(25 * 60000), Time(5 * 60000), URI("http://null"))
        task1 = Task("task_1", Calendar.getInstance().time, startTime, 1, "Maths Assignment", studyCategory.id, false)
        task2 = Task("task_2", Calendar.getInstance().time, startTime, 1, "Read Emails", businessCategory.id, true)
        task3 = Task("task_3", Date(23L), Time(4300L), 1, "Half an hour jog", sportCategory.id, true)
        task4 = Task("task_4", Calendar.getInstance().time, Time(1000L), 1, "Prepare Slides", studyCategory.id, false)

        repository.saveCategories(studyCategory, businessCategory, sportCategory)
        repository.saveTaskMethod(method)
        repository.saveTasks(task1, task2, task3, task4)
    }

    @After
    fun cleanUpRepository() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun passAllTasksFilter_loadsAllTasksInRecyclerView() {

        launchFragmentInContainer<TasksFragment>(TasksFragmentArgs(TaskFilterType.ALL).toBundle(), R.style.AppTheme)

        onView(withId(R.id.tasksList)).check(matches(isDisplayed()))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withId(R.id.taskTitle))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText(task1.title))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText(task2.title))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText(task3.title))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText(task4.title))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withId(R.id.methodName))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText(method.name))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withId(R.id.methodDuration))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText("30 min"))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withId(R.id.categoryName))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText(studyCategory.name))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText(businessCategory.name))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText(sportCategory.name))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withId(R.id.interval))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withText("02:12 PM - 02:42 PM"))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withId(R.id.methodImg))))
        onView(withId(R.id.tasksList)).check(matches(hasDescendant(withId(R.id.delIcon))))
    }
}