package com.thetechannel.android.planit.tasks

import androidx.lifecycle.*
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.util.isToday

class TasksViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _tasksFilterType = MutableLiveData<TaskFilterType>(TaskFilterType.ALL)
    val tasksFilterType: LiveData<TaskFilterType>
        get() = _tasksFilterType

    val tasks: LiveData<List<Task>> = _tasksFilterType.switchMap { type ->
        repository.observeTasks().switchMap { tasks -> filterTasks(tasks, type) }
    }

    private fun filterTasks(
        tasks: Result<List<Task>>,
        filter: TaskFilterType?
    ): LiveData<List<Task>> {

        val result = MutableLiveData<List<Task>>()

        result.value = when (tasks) {

            is Result.Success -> {
                when (filter) {
                    TaskFilterType.ALL -> tasks.data
                    TaskFilterType.PENDING -> tasks.data.filter { !it.completed }
                    TaskFilterType.COMPLETED -> tasks.data.filter { it.completed }
                    TaskFilterType.COMPLETED_TODAY -> tasks.data.filter { it.completed && it.day.isToday() }
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

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _errorLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorLoading: LiveData<Boolean>
        get() = _errorLoading

    fun setFiltering(filter: TaskFilterType) {
        _tasksFilterType.value = filter
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
