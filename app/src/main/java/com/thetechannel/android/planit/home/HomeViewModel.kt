package com.thetechannel.android.planit.home

import androidx.lifecycle.*
import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.Event
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.database.TasksOverView
import com.thetechannel.android.planit.data.source.database.TodayProgress
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

    val tasksOverView: LiveData<TasksOverView> = repository.observeTasksOverView().map {
        when (it) {
            is Result.Success -> it.data
            else -> TasksOverView(0, 0, 0)
        }
    }

    val todayProgress: LiveData<Int> = repository.observeTodayProgress().map {
        when (it) {
            is Result.Success -> it.data.percentage
            else -> 0
        }
    }

    val todayPieEntries: LiveData<List<PieEntry>> = repository.observeTodayPieEntries().map {
        when (it) {
            is Result.Success -> it.data
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