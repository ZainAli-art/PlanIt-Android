package com.thetechannel.android.planit.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.Event
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.database.TasksOverView
import com.thetechannel.android.planit.data.source.database.TodayProgress

class HomeViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _eventOpenTasks: MutableLiveData<Event<TaskFilterType>> = MutableLiveData()
    val eventOpenTasks: LiveData<Event<TaskFilterType>>
        get() = _eventOpenTasks

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

    fun openPendingTasks() {
        _eventOpenTasks.value = Event(TaskFilterType.PENDING)
    }

    fun openCompletedTasks() {
        _eventOpenTasks.value = Event(TaskFilterType.COMPLETED)
    }

    fun openTasksCompletedToday() {
        _eventOpenTasks.value = Event(TaskFilterType.COMPLETED_TODAY)
    }
}