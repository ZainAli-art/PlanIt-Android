package com.thetechannel.android.planit

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.data.source.network.FakeRemoteDataSource
import com.thetechannel.android.planit.home.MainActivity
import com.thetechannel.android.planit.util.DataBindingIdlingResource
import com.thetechannel.android.planit.util.EspressoIdlingResource
import com.thetechannel.android.planit.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URI
import java.sql.Time

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private lateinit var studyCategory: Category
    private lateinit var businessCategory: Category
    private lateinit var sportsCategory: Category
    private lateinit var categories: List<Category>
    private lateinit var pomodoroMethod: TaskMethod
    private lateinit var eatTheDevilMethod: TaskMethod
    private lateinit var methods: List<TaskMethod>

    private lateinit var repository: AppRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() = runBlocking {
        ServiceLocator.setRemoteDataSource(FakeRemoteDataSource)

        repository = ServiceLocator.provideRepository(
            getApplicationContext()
        )

        studyCategory = Category(1, "Study")
        businessCategory = Category(2, "Business")
        sportsCategory = Category(3, "Sports")
        categories = listOf(studyCategory, businessCategory, sportsCategory)
        pomodoroMethod = TaskMethod(1, "Pomodoro", Time(25 * 60000), Time(5 * 60000), URI(""))
        eatTheDevilMethod = TaskMethod(2, "Eat The Devil", Time(3 * 60 * 60000), Time(0), URI(""))
        methods = listOf(pomodoroMethod, eatTheDevilMethod)

        repository.saveCategories(studyCategory, businessCategory, sportsCategory)
        repository.saveTaskMethods(pomodoroMethod, eatTheDevilMethod)
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun addNewTask_addsOnePendingTask() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // pendingTasks shows 0
        onView(withId(R.id.pendingTasks))
            .check(matches(withText("0")))

        // click add new task floating action button
        onView(withId(R.id.addNewTaskFab))
            .check(matches(isDisplayed()))
            .perform(click())

        // create and insert new task
        val title = "Do Maths Assignment"
        onView(withId(R.id.editTitle)).perform(
            ViewActions.typeText(title),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.scheduleButton)).perform(click())
        onView(withClassName(Matchers.`is`(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2000, 5, 26))
        onView(withText("OK")).perform(click())
        onView(withClassName(Matchers.`is`(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(11, 56))
        onView(withText("OK")).perform(click())

        // navigated up successfully
        onView(withId(R.id.homeFragmentSwipeRefreshLayout))
            .check(matches(isDisplayed()))

        // pendingTasks is updated to 1
        onView(withId(R.id.pendingTasks))
            .check(matches(allOf(isDisplayed(), withText("1"))))
    }
}