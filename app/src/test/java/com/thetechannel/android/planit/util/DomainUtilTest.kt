package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.net.URI
import java.sql.Time
import java.util.*

class DomainUtilTest {
    @Test
    fun convertDomainCategoryToNetwork_convertBack_returnsTrueIfBothAreTransferable() {
        val domainCat = Category(1, "Study")

        val networkCat = domainCat.asDataTransferObject()

        assertThat(domainCat.id, `is`(networkCat.id))
        assertThat(domainCat.name, `is`(networkCat.name))
    }

    @Test
    fun convertDomainCategoryToDb_convertBack_returnsTrueIfBothAreTransferable() {
        val domainCat = Category(1, "Study")

        val networkCat = domainCat.asDatabaseEntity()

        assertThat(domainCat.id, `is`(networkCat.id))
        assertThat(domainCat.name, `is`(networkCat.name))
    }

    @Test
    fun convertDomainTaskMethodToNetwork_convertBack_returnsTrueIfBothAreTransferable() {
        val domainMethod = TaskMethod(
            1,
            "Pomodoro",
            Time(25 * 60000),
            Time(5 * 60000),
            URI("https://www.google.com")
        )

        val networkMethod = domainMethod.asDataTransferObject()

        assertThat(domainMethod.id, `is`(networkMethod.id))
        assertThat(domainMethod.name, `is`(networkMethod.name))
        assertThat(domainMethod.workLapse.time, `is`(networkMethod.workLapse))
        assertThat(domainMethod.breakLapse.time, `is`(networkMethod.breakLapse))
        assertThat(domainMethod.iconUrl.toString(), `is`(networkMethod.iconUrl))
    }

    @Test
    fun convertDomainTaskMethodToDb_convertBack_returnsTrueIfBothAreTransferable() {
        val domainMethod = TaskMethod(
            1,
            "Pomodoro",
            Time(25 * 60000),
            Time(5 * 60000),
            URI("https://www.google.com")
        )

        val networkMethod = domainMethod.asDatabaseEntity()

        assertThat(domainMethod.id, `is`(networkMethod.id))
        assertThat(domainMethod.name, `is`(networkMethod.name))
        assertThat(domainMethod.workLapse.time, `is`(networkMethod.workLapse))
        assertThat(domainMethod.breakLapse.time, `is`(networkMethod.breakLapse))
        assertThat(domainMethod.iconUrl.toString(), `is`(networkMethod.iconUrl))
    }

    @Test
    fun convertDomainTaskToNetwork_convertBack_returnsTrueIfBothAreTransferable() {
        val domainTask =
            Task("task_1", Calendar.getInstance().time, Time(0), 1, "Maths Assignment", 1, true)

        val networkTask = domainTask.asDataTransferObject()

        assertThat(domainTask.id, `is`(networkTask.id))
        assertThat(domainTask.day.time, `is`(networkTask.day))
        assertThat(domainTask.startAt.time, `is`(networkTask.startAt))
        assertThat(domainTask.methodId, `is`(networkTask.methodId))
        assertThat(domainTask.title, `is`(networkTask.title))
        assertThat(domainTask.catId, `is`(networkTask.catId))
        assertThat(domainTask.completed, `is`(networkTask.completed.toBoolean()))
    }

    @Test
    fun convertDomainTaskToDb_convertBack_returnsTrueIfBothAreTransferable() {
        val domainTask =
            Task("task_1", Calendar.getInstance().time, Time(0), 1, "Maths Assignment", 1, true)

        val networkTask = domainTask.asDatabaseEntity()

        assertThat(domainTask.id, `is`(networkTask.id))
        assertThat(domainTask.day.time, `is`(networkTask.day))
        assertThat(domainTask.startAt.time, `is`(networkTask.startAt))
        assertThat(domainTask.methodId, `is`(networkTask.methodId))
        assertThat(domainTask.title, `is`(networkTask.title))
        assertThat(domainTask.catId, `is`(networkTask.catId))
        assertThat(domainTask.completed, `is`(networkTask.completed))
    }
}