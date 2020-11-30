package com.thetechannel.android.planit.newtask

import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.contrib.PickerActions.setTime
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.thetechannel.android.planit.R
import com.thetechannel.android.planit.ServiceLocator
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.FakeAndroidTestRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.net.URI
import java.sql.Time

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class NewTaskFragmentTest {

    private lateinit var studyCategory: Category
    private lateinit var businessCategory: Category
    private lateinit var sportsCategory: Category
    private lateinit var categories: List<Category>
    private lateinit var pomodoroMethod: TaskMethod
    private lateinit var eatTheDevilMethod: TaskMethod
    private lateinit var methods: List<TaskMethod>

    private lateinit var repository: AppRepository

    @Before
    fun initRepository() = runBlockingTest {
        repository = FakeAndroidTestRepository()

        studyCategory = Category(1, "Study")
        businessCategory = Category(2, "Business")
        sportsCategory = Category(3, "Sports")
        categories = listOf(studyCategory, businessCategory, sportsCategory)
        pomodoroMethod = TaskMethod(1, "Pomodoro", Time(25 * 60000), Time(5 * 60000), URI(""))
        eatTheDevilMethod = TaskMethod(2, "Eat The Devil", Time(3 * 60 * 60000), Time(0), URI(""))
        methods = listOf(pomodoroMethod, eatTheDevilMethod)

        repository.saveCategories(studyCategory, businessCategory, sportsCategory)
        repository.saveTaskMethods(pomodoroMethod, eatTheDevilMethod)

        ServiceLocator.repository = repository
    }

    @After
    fun cleanUpRepository() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun pressCategorySpinner_displaysNamesOfInsertedCategories() {
        launchFragmentInContainer<NewTaskFragment>(Bundle(), R.style.AppTheme)

        onView(withId(R.id.categorySpinner))
            .perform(click())

        onData(allOf(`is`(instanceOf(String::class.java)),
            `is`(studyCategory.name))).check(matches(isDisplayed()))
        onData(allOf(`is`(instanceOf(String::class.java)),
            `is`(sportsCategory.name))).check(matches(isDisplayed()))
        onData(allOf(`is`(instanceOf(String::class.java)),
            `is`(sportsCategory.name))).check(matches(isDisplayed()))
    }

    @Test
    fun clickCategorySpinner_selectSports_setsSportsCategoryInCategorySpinner() {
        launchFragmentInContainer<NewTaskFragment>(Bundle(), R.style.AppTheme)

        onView(withId(R.id.categorySpinner)).perform(click())

        onData(allOf(`is`(instanceOf(String::class.java)),
            `is`(sportsCategory.name))).perform(click())

        onView(withText(sportsCategory.name)).check(matches(isDisplayed()))
    }

    @Test
    fun clickMethodSpinner_displaysNamesOfInsertedTaskMethods() {
        launchFragmentInContainer<NewTaskFragment>(Bundle(), R.style.AppTheme)

        onView(withId(R.id.methodSpinner)).perform(click())

        onData(allOf(`is`(instanceOf(String::class.java)),
            `is`(pomodoroMethod.name))).check(matches(isDisplayed()))
        onData(allOf(`is`(instanceOf(String::class.java)),
            `is`(eatTheDevilMethod.name))).check(matches(isDisplayed()))
    }

    @Test
    fun clickMethodSpinnerAndSelectEatTheDevilMethod_selectedMethodsNameIsShownInMethodSpinner() {
        launchFragmentInContainer<NewTaskFragment>(Bundle(), R.style.AppTheme)

        onView(withId(R.id.methodSpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)),
            `is`(eatTheDevilMethod.name))).perform(click())

        onView(withText(eatTheDevilMethod.name)).check(matches(isDisplayed()))
    }

    @Test
    fun enterTitleAndClickScheduleButton_displaysDateAndTimePickerDialogues() {
        launchFragmentInContainer<NewTaskFragment>(Bundle(), R.style.AppTheme)

        val title = "Do Maths Assignment"
        onView(withId(R.id.editTitle)).perform(typeText(title), closeSoftKeyboard())
        onView(withId(R.id.scheduleButton)).perform(click())
        onView(withClassName(`is`(DatePicker::class.java.name)))
            .perform(setDate(2000, 5, 26))
        onView(withText("OK"))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withClassName(`is`(TimePicker::class.java.name)))
            .perform(setTime(11, 56))
        onView(withText("OK"))
            .check(matches(isDisplayed()))
        // clicking pops the fragment resulting in crash
    }
}