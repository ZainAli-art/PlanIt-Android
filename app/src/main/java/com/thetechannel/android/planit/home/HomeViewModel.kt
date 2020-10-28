package com.thetechannel.android.planit.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.util.isSameDay
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class HomeViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _allTasks = repository.observeTasks()
    val pendingTasks: LiveData<List<Task>> = _allTasks.map {
        when (it) {
            is Result.Success -> it.data.filter { !it.completed }
            else -> emptyList()
        }
    }

    val todayTasks: LiveData<List<Task>> = _allTasks.map {
        when (it) {
            is Result.Success -> {
                it.data.filter {
                    it.day.isSameDay(Calendar.getInstance().time)
                }
            }
            else -> emptyList()
        }
    }

    val tasksCompletedToday: LiveData<List<Task>> = todayTasks.map {
        it.filter { it.completed }
    }

    val completedTasks: LiveData<List<Task>> = _allTasks.map {
        when (it) {
            is Result.Success -> it.data.filter { it.completed }
            else -> emptyList()
        }
    }

    val todayProgress: LiveData<Int> = todayTasks.map {
        if (it.isEmpty()) 0 else it.filter { it.completed }.size * 100 / it.size
    }
}