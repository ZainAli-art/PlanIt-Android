package com.thetechannel.android.planit.tasks

import androidx.lifecycle.*
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.util.isToday

class TasksViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _tasksFilterType = MutableLiveData<TaskFilterType>(TaskFilterType.ALL)
    val tasksFilterType: LiveData<TaskFilterType>
        get() = _tasksFilterType

    val taskDetails: LiveData<List<TaskDetail>> = _tasksFilterType.switchMap { filter ->
        repository.observeTaskDetails().switchMap { details -> filterTaskDetails(details, filter) }
    }

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _errorLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorLoading: LiveData<Boolean>
        get() = _errorLoading

    fun setFiltering(filter: TaskFilterType) {
        _tasksFilterType.value = filter
    }

    private fun filterTaskDetails(
        details: Result<List<TaskDetail>>,
        filter: TaskFilterType?
    ): LiveData<List<TaskDetail>> {

        val result = MutableLiveData<List<TaskDetail>>()

        result.value = when (details) {
            is Result.Success -> {
                _dataLoading.value = false
                _errorLoading.value = false
                when (filter) {
                    TaskFilterType.ALL -> details.data
                    TaskFilterType.PENDING -> details.data.filter { !it.completed }
                    TaskFilterType.COMPLETED -> details.data.filter { it.completed }
                    TaskFilterType.COMPLETED_TODAY -> details.data.filter { it.completed && it.day.isToday() }
                    else -> emptyList()
                }
            }
            is Result.Loading -> {
                _dataLoading.value = true
                _errorLoading.value = false
                emptyList()
            }
            is Result.Error -> {
                _dataLoading.value = false
                _errorLoading.value = true
                emptyList()
            }
        }

        return result
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
