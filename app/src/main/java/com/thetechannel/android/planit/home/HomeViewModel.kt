package com.thetechannel.android.planit.home

import androidx.lifecycle.*
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.Event
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.TasksOverView
import com.thetechannel.android.planit.util.getPieEntries
import com.thetechannel.android.planit.util.isToday
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _openTasksEvent: MutableLiveData<Event<TaskFilterType>> = MutableLiveData()
    val openTasksEvent: LiveData<Event<TaskFilterType>>
        get() = _openTasksEvent

    private val _addNewTaskEvent = MutableLiveData<Event<Boolean>>()
    val addNewTaskEvent: LiveData<Event<Boolean>>
        get() = _addNewTaskEvent

    private val taskDetails = repository.observeTaskDetails()

    val tasksOverView: LiveData<TasksOverView> = taskDetails.map {
        when (it) {
            is Result.Success -> {
                val pending = it.data.filter { !it.completed }
                val completed = it.data.filter { it.completed }
                val completedToday = completed.filter { it.day.isToday() }

                TasksOverView(completed.size, pending.size, completedToday.size)
            }
            else -> TasksOverView(0, 0, 0)
        }
    }

    val todayProgress: LiveData<Int> = taskDetails.map {
        when (it) {
            is Result.Success -> {
                val totalTasks = it.data.filter { it.day.isToday() }
                if (totalTasks.isEmpty()) return@map 0

                val completedTasks = totalTasks.filter { it.completed }

                completedTasks.size * 100 / totalTasks.size
            }
            else -> 0
        }
    }

    val todayPieEntries: LiveData<List<PieEntry>> = taskDetails.map {
        when (it) {
            is Result.Success -> {
                getPieEntries(it.data.filter { it.day.isToday() })
            }
            else -> emptyList()
        }
    }

    fun openPendingTasks() {
        _openTasksEvent.value = Event(TaskFilterType.PENDING)
    }

    fun openCompletedTasks() {
        _openTasksEvent.value = Event(TaskFilterType.COMPLETED)
    }

    fun openTasksCompletedToday() {
        _openTasksEvent.value = Event(TaskFilterType.COMPLETED_TODAY)
    }

    fun addNewTask() {
        _addNewTaskEvent.value = Event(true)
    }

    fun refresh() {
        _dataLoading.value = true
        viewModelScope.launch {
            repository.refreshCategories()
            repository.refreshTaskMethods()
            repository.refreshTasks()
            _dataLoading.value = false
        }
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val repository: AppRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        HomeViewModel(repository) as T
}