package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.database.DbCategory
import com.thetechannel.android.planit.data.source.database.DbTask
import com.thetechannel.android.planit.data.source.database.DbTaskMethod
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class DatabaseUtilTest {
    @Test
    fun convertDbCategoryToDomain_convertBack_returnsTrueIfBothAreTransferable() {
        val dbCategory = DbCategory(1, "Business")

        val domainCategory = dbCategory.asDomainModel()

        assertThat(dbCategory.id, `is`(domainCategory.id))
        assertThat(dbCategory.name, `is`(domainCategory.name))
    }

    @Test
    fun convertDbTaskMethodToDomain_convertBack_returnsTrueIfBothAreTransferable() {
        val dbMethod = DbTaskMethod(1, "pomodoro", 2500L, 1230L, "https://www.google.com");

        val domainMethod = dbMethod.asDomainModel()

        assertThat(dbMethod.id, `is`(domainMethod.id))
        assertThat(dbMethod.name, `is`(domainMethod.name))
        assertThat(dbMethod.workLapse, `is`(domainMethod.workLapse.time))
        assertThat(dbMethod.breakLapse, `is`(domainMethod.breakLapse.time))
        assertThat(dbMethod.iconUrl, `is`(domainMethod.iconUrl.toString()))
    }

    @Test
    fun convertDbTaskToDomain_convertBack_returnsTrueIfBothAreTransferable() {
        val dbTask = DbTask("task_1", 2, 0L, 1, "Maths Assignment", 1, false)

        val domainTask = dbTask.asDomainModel()

        assertThat(dbTask.id, `is`(domainTask.id))
        assertThat(dbTask.day, `is`(domainTask.day.time))
        assertThat(dbTask.startAt, `is`(domainTask.startAt.time))
        assertThat(dbTask.methodId, `is`(domainTask.methodId))
        assertThat(dbTask.title, `is`(domainTask.title))
        assertThat(dbTask.catId, `is`(domainTask.catId))
        assertThat(dbTask.completed, `is`(domainTask.completed))
    }
}