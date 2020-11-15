package com.thetechannel.android.planit.data.source.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {
    private val STUDY = 1
    private val BUSINESS = 2
    private val SPORT = 3

    private lateinit var taskMethod: DbTaskMethod
    private lateinit var studyCategory: DbCategory
    private lateinit var businessCategory: DbCategory
    private lateinit var sportCategory: DbCategory

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PlanItDatabase
    private lateinit var tasksDao: TasksDao

    @Before
    fun initDb() = runBlockingTest {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlanItDatabase::class.java
        )
            .build()
        
        tasksDao = database.tasksDao

        taskMethod = DbTaskMethod(1, "pomodoro", 0L, 0L, "https://www.google.com")
        database.taskMethodsDao.insert(taskMethod)

        studyCategory = DbCategory(STUDY, "Study")
        businessCategory = DbCategory(BUSINESS, "Business")
        sportCategory = DbCategory(SPORT, "Sport")
        arrayOf(studyCategory, businessCategory, sportCategory).forEach {
            database.categoriesDao.insert(it)
        }
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTask_getById() = runBlockingTest {
        val task = DbTask(
            "TASK1",
            System.currentTimeMillis(),
            0,
            1,
            "Maths Assignment",
            STUDY,
            false
        )
        tasksDao.insert(task)

        val loadedTask = tasksDao.getById(task.id)

        assertThat<DbTask>(loadedTask as DbTask, CoreMatchers.notNullValue())
        assertThat(loadedTask.id, `is`(task.id))
        assertThat(loadedTask.day, `is`(task.day))
        assertThat(loadedTask.startAt, `is`(task.startAt))
        assertThat(loadedTask.methodId, `is`(task.methodId))
        assertThat(loadedTask.catId, `is`(task.catId))
        assertThat(loadedTask.title, `is`(task.title))
        assertThat(loadedTask.completed, `is`(task.completed))
    }

    @Test
    fun insertAndDeleteTask_getTaskById_returnsNull() = runBlockingTest {
        val task = DbTask(
            "TASK1",
            System.currentTimeMillis(),
            0,
            1,
            "Maths Assignment",
            STUDY,
            false
        )
        tasksDao.insert(task)
        tasksDao.delete(task)

        val loadedTask = tasksDao.getById(task.id)
        assertThat(loadedTask, CoreMatchers.nullValue())
    }

    @Test
    fun insertTaskAndTaskMethod_fetchTaskDetails_returnsTaskDetailsFromInsertedData() = runBlockingTest {
        val task =
            DbTask("TASK1", System.currentTimeMillis(), 0, 1, "Maths Assignment", 1, false)
        tasksDao.insert(task)

        val taskDetail = tasksDao.getTaskDetailsByTaskId(task.id)

        assertThat<DbTaskDetail>(taskDetail, CoreMatchers.notNullValue())
        assertThat(taskDetail.id, `is`(task.id))
        assertThat(taskDetail.method, `is`(taskMethod.name))
        assertThat(taskDetail.methodIconUrl, `is`(taskMethod.iconUrl))
        assertThat(
            taskDetail.timeLapse,
            `is`(taskMethod.workLapse + taskMethod.breakLapse)
        )
        assertThat(taskDetail.title, `is`(task.title))
        assertThat(taskDetail.workStart, `is`(task.startAt))
        assertThat(
            taskDetail.workEnd,
            `is`(task.startAt + taskMethod.workLapse)
        )
        assertThat(
            taskDetail.breakStart,
            `is`(task.startAt + taskMethod.workLapse)
        )
        assertThat(
            taskDetail.breakEnd, `is`(
                task.startAt + taskMethod.workLapse + taskMethod.breakLapse
            )
        )
    }

    @Test
    fun insertedIncompleteTask_updateTaskCompletedAndFetchById_returnsCompletedTask() =
        runBlockingTest {
            val task = DbTask(
                "TASK1",
                System.currentTimeMillis(),
                0,
                1,
                "Maths Assignment",
                STUDY,
                false
            )
            tasksDao.insert(task)

            tasksDao.updateCompleted(task.id, true)
            val loaded = tasksDao.getById(task.id)
            assertThat<DbTask>(loaded as DbTask, `is`(CoreMatchers.notNullValue()))

            assertThat(loaded.completed, `is`(true))
        }

    @Test
    fun insertTasks_getTasksOverview_returnsOverviewOfInsertedTasks() = runBlockingTest {
        val tasks = getVersatileTasks()
        tasks.forEach { tasksDao.insert(it) }

        val views = tasksDao.getTasksOverView()
        assertThat(views.completedTasks, `is`(2))
        assertThat(views.pendingTasks, `is`(4))
        assertThat(views.tasksCompletedToday, `is`(1))
    }

    @Test
    fun insertTasks_get_TodayProgress_returnsTodayCompletedTasksPercentage() = runBlockingTest {
        val tasks = getVersatileTasks()
        tasks.forEach { tasksDao.insert(it) }

        val progress = tasksDao.getTodayProgress()
        assertThat(progress.percentage, `is`(20))
    }

    @Test
    fun getTodayPieDataViews_returnCategoriesAndNumberOfTodayTasksLyingThoseCategories() = runBlockingTest {
        val tasks = getVersatileTasks()
        tasks.forEach { tasksDao.insert(it) }

        val views = tasksDao.getTodayPieDataViews()
        val viewData = mutableMapOf<String, TodayPieDataView>()
        for (v in views) viewData[v.name] = v

        assertThat(views.size, `is`(3))
        assertThat(viewData["Study"]?.count, `is`(2))
        assertThat(viewData["Business"]?.count, `is`(2))
        assertThat(viewData["Sport"]?.count, `is`(1))
    }

    @Test
    fun insertTasks_deleteAll_deletedAllInsertedTasks() = runBlockingTest {
        val tasks = getVersatileTasks()
        tasks.forEach { tasksDao.insert(it) }

        tasksDao.deleteAll()
        
        val loaded = tasksDao.getAll()
        assertThat(loaded.size, `is`(0))
    }

    @Test
    fun insertTasks_getAllTaskDetails_returnsDetailOfAllInsertedTasks() = runBlockingTest {
        val task1 = DbTask("task_1", 23000L, 2000L, taskMethod.id, "Maths Assignment", studyCategory.id, false)
        val task2 = DbTask("task_2", 23000L, 2000L, taskMethod.id, "Half an hour jog", sportCategory.id, true)
        arrayOf(task1, task2).forEach { tasksDao.insert(it) }

        val details = tasksDao.getAllTaskDetails()

        assertThat(details.size, `is`(2))
        val detail1 = details[0]
        assertThat(detail1.id, `is`(task1.id))
        assertThat(detail1.category, `is`(studyCategory.name))
        assertThat(detail1.method, `is`(taskMethod.name))
        assertThat(detail1.methodIconUrl, `is`(taskMethod.iconUrl))
        assertThat(detail1.timeLapse, `is`(taskMethod.workLapse + taskMethod.breakLapse))
        assertThat(detail1.title, `is`(task1.title))
        assertThat(detail1.workStart, `is`(task1.startAt))
        assertThat(detail1.workEnd, `is`(task1.startAt + taskMethod.workLapse))
        assertThat(detail1.breakStart, `is`(task1.startAt + taskMethod.workLapse))
        assertThat(detail1.breakEnd, `is`(task1.startAt + taskMethod.workLapse + taskMethod.breakLapse))
        val detail2 = details[1]
        assertThat(detail2.id, `is`(task2.id))
        assertThat(detail2.category, `is`(sportCategory.name))
        assertThat(detail2.method, `is`(taskMethod.name))
        assertThat(detail2.methodIconUrl, `is`(taskMethod.iconUrl))
        assertThat(detail2.timeLapse, `is`(taskMethod.workLapse + taskMethod.breakLapse))
        assertThat(detail2.title, `is`(task2.title))
        assertThat(detail2.workStart, `is`(task2.startAt))
        assertThat(detail2.workEnd, `is`(task2.startAt + taskMethod.workLapse))
        assertThat(detail2.breakStart, `is`(task2.startAt + taskMethod.workLapse))
        assertThat(detail2.breakEnd, `is`(task2.startAt + taskMethod.workLapse + taskMethod.breakLapse))
    }

    /**
     * Array of versatile tasks, provides a lot of dimensions for test cases
     *
     * - 6 tasks in total
     * - 5 are inserted today
     * - 2 are completed
     * - 4 are incomplete
     * - 1 is completed today
     * - today progress is (1 / 5) -> 20%
     */
    private fun getVersatileTasks() = arrayOf(
        DbTask(
            "task_1",
            System.currentTimeMillis(),
            1L,
            taskMethod.id,
            "Maths Assignment",
            studyCategory.id,
            false
        ),
        DbTask(
            "task_2",
            45000L,
            1L,
            taskMethod.id,
            "Prepare for Algo Quiz",
            studyCategory.id,
            true
        ),
        DbTask(
            "task_3",
            System.currentTimeMillis(),
            1L,
            taskMethod.id,
            "Prepare Slides",
            studyCategory.id,
            false
        ),
        DbTask(
            "task_4",
            System.currentTimeMillis(),
            1L,
            taskMethod.id,
            "View unread emails",
            businessCategory.id,
            true
        ),
        DbTask(
            "task_5",
            System.currentTimeMillis(),
            1L,
            taskMethod.id,
            "Plan next week layout",
            businessCategory.id,
            false
        ),
        DbTask(
            "task_6",
            System.currentTimeMillis(),
            1L,
            taskMethod.id,
            "Two brisks around the colony",
            sportCategory.id,
            false
        )
    )
}