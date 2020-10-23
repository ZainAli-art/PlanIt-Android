package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
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
        val domainCat = Category(1, "Study");

        val networkCat = domainCat.toDataTransferObject()

        assertThat(domainCat.id, `is`(networkCat.id))
        assertThat(domainCat.name, `is`(networkCat.name))
    }

    @Test
    fun convertDomainTaskMethodToNetwork_convertBack_returnsTrueIfBothAreTransferable() {
        val domainMethod = TaskMethod(1, "Pomodoro", Time(25 * 60000), Time(5 * 60000), URI("https://www.google.com"))

        val networkMethod = domainMethod.toDataTransferObject()

        assertThat(domainMethod.id, `is`(networkMethod.id))
        assertThat(domainMethod.name, `is`(networkMethod.name))
        assertThat(domainMethod.workLapse.time, `is`(networkMethod.workLapse.time))
        assertThat(domainMethod.breakLapse.time, `is`(networkMethod.breakLapse.time))
        assertThat(domainMethod.iconUrl.toString(), `is`(networkMethod.iconUrl))
    }

    @Test
    fun convertDomainTaskToNetwork_convertBack_returnsTrueIfBothAreTransferable() {
        val domainTask = Task("task_1", Calendar.getInstance().time, Time(0), 1, "Maths Assignment", 1, true)

        val networkTask = domainTask.toDataTransferObject()

        assertThat(domainTask.id, `is`(networkTask.id))
        assertThat(domainTask.day, `is`(networkTask.day))
        assertThat(domainTask.startAt.time, `is`(networkTask.startAt.time))
        assertThat(domainTask.methodId, `is`(networkTask.methodId))
        assertThat(domainTask.title, `is`(networkTask.title))
        assertThat(domainTask.catId, `is`(networkTask.catId))
        assertThat(domainTask.completed, `is`(networkTask.completed))
    }
}