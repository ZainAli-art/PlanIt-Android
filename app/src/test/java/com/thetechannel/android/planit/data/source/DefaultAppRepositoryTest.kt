package com.thetechannel.android.planit.data.source

import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import java.net.URI
import java.sql.Time
import java.util.*

@ExperimentalCoroutinesApi
class DefaultAppRepositoryTest {

    private val cat1 = Category(1, "Study")
    private val cat2 = Category(2, "Business")
    private val cat3 = Category(3, "Sport")
    private val method1 = TaskMethod(1, "Pomodoro", Time(25000L), Time(5000L), URI("https://none"))
    private val method2 = TaskMethod(1, "Eat The Devil", Time(30000L), Time(5000L), URI("https://none"))
    private val task1 = Task("task_1", Calendar.getInstance().time, Time(0L), method1.id, "Maths Assignment", cat1.id, false)
    private val task2 = Task("task_2", Calendar.getInstance().time, Time(0L), method1.id, "Email", cat2.id, false)
    private val task3 = Task("task_3", Calendar.getInstance().time, Time(0L), method1.id, "Sprint", cat3.id, false)
    private lateinit var localDataSource: FakeDataSource
    private lateinit var remoteDataSource: FakeDataSource

    private lateinit var repository: DefaultAppRepository

    @Before
    fun createRepository() {
        localDataSource = FakeDataSource(mutableListOf(cat1, cat2), mutableListOf(method1), mutableListOf(task1))
        remoteDataSource = FakeDataSource(mutableListOf(cat3), mutableListOf(method1, method2), mutableListOf(task2, task3))

        repository = DefaultAppRepository(localDataSource, remoteDataSource)
    }
}