package com.thetechannel.android.planit.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.database.TasksOverView
import com.thetechannel.android.planit.data.source.database.TodayProgress
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.util.isSameDay
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class HomeViewModel(
    private val repository: AppRepository
) : ViewModel() {

    val tasksOverView: LiveData<TasksOverView> = repository.observeTasksOverView().map {
        when (it) {
            is Result.Success -> it.data
            else -> TasksOverView(0, 0, 0)
        }
    }

    val todayProgress: LiveData<TodayProgress> = repository.observeTodayProgress().map {
        when (it) {
            is Result.Success -> it.data
            else -> TodayProgress(0)
        }
    }

    val todayPieEntries: LiveData<List<PieEntry>> = repository.observeTodayPieEntries().map {
        when (it) {
            is Result.Success -> it.data
            else -> emptyList()
        }
    }
}