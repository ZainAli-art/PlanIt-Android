package com.thetechannel.android.planit.tasks

import androidx.lifecycle.*
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.util.isToday
import kotlinx.coroutines.launch

class TasksViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val tasksFilterType = MutableLiveData<TaskFilterType>()

    private val taskDetails = repository.observeTaskDetails()

    val observableTaskDetails: LiveData<List<TaskDetail>> = tasksFilterType.switchMap { filter ->
        taskDetails.switchMap { details -> filterTaskDetails(details, filter) }
    }

    val error = taskDetails.map { it is Result.Error }
    val empty =
        taskDetails.map { (it as? Result.Success)?.data.isNullOrEmpty() }

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean>
        get() = _loading

    fun setFiltering(filter: TaskFilterType) {
        tasksFilterType.value = filter
    }

    private fun filterTaskDetails(
        details: Result<List<TaskDetail>>,
        filter: TaskFilterType?
    ): LiveData<List<TaskDetail>> = MutableLiveData<List<TaskDetail>>(
        getFilteredTaskDetails(details, filter)
    )

    private fun getFilteredTaskDetails(
        details: Result<List<TaskDetail>>,
        filter: TaskFilterType?
    ) = when (details) {
        is Result.Success -> {
            when (filter) {
                TaskFilterType.ALL -> details.data
                TaskFilterType.PENDING -> details.data.filter { !it.completed }
                TaskFilterType.COMPLETED -> details.data.filter { it.completed }
                TaskFilterType.COMPLETED_TODAY -> details.data.filter { it.completed && it.day.isToday() }
                else -> emptyList()
            }
        }
        else -> emptyList()
    }

    fun refresh() {
        println("refreshing tasks")
        _loading.value = true
        viewModelScope.launch {
            repository.refreshTasks()
            _loading.value = true
        }
    }
}

@Suppress("UNCHECKED_CAST")
class TasksViewModelFactory(
    private val repository: AppRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TasksViewModel(repository) as T
    }
}
