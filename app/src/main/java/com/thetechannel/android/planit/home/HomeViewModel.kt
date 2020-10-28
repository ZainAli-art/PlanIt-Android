package com.thetechannel.android.planit.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.util.isSameDay
import java.util.*

class HomeViewModel(
    private val repository: AppRepository
): ViewModel() {

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
}