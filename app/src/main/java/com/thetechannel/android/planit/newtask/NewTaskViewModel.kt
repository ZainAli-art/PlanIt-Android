package com.thetechannel.android.planit.newtask

import androidx.lifecycle.*
import com.thetechannel.android.planit.Event
import com.thetechannel.android.planit.R
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import kotlinx.coroutines.launch

class NewTaskViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _taskAddedEvent = MutableLiveData<Event<Boolean>>()
    val taskAddedEvent: MutableLiveData<Event<Boolean>>
        get() = _taskAddedEvent

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>>
        get() = _snackBarText

    private val _errorLoadingCategories = MutableLiveData<Boolean>()

    private val _loadingCategories = MutableLiveData<Boolean>()
    private val _loadingTaskMethods = MutableLiveData<Boolean>()

    private val _errorLoadingTaskMethods = MutableLiveData<Boolean>()
    val categories: LiveData<List<Category>> = repository.observeCategories().switchMap {
        val result = MutableLiveData<List<Category>>()

        when (it) {
            is Result.Loading -> {
                _loadingCategories.value = true
                _errorLoadingCategories.value = false
                result.value = emptyList()
            }
            is Result.Error -> {
                _loadingCategories.value = false
                _errorLoadingCategories.value = true
                result.value = emptyList()
            }
            is Result.Success -> {
                _loadingCategories.value = false
                _errorLoadingCategories.value = false
                result.value = it.data
            }
        }

        result
    }

    val categoryNames: LiveData<List<String>> = categories.switchMap {
        val names = mutableListOf<String>()
        it.forEach { category -> names.add(category.name) }

        MutableLiveData(names.toList())
    }

    val taskMethods: LiveData<List<TaskMethod>> = repository.observeTaskMethods().switchMap {
        val result = MutableLiveData<List<TaskMethod>>()

        when (it) {
            is Result.Loading -> {
                _loadingTaskMethods.value = true
                _errorLoadingTaskMethods.value = false
                result.value = emptyList()
            }
            is Result.Error -> {
                _loadingTaskMethods.value = false
                _errorLoadingTaskMethods.value = true
                result.value = emptyList()
            }
            is Result.Success -> {
                _loadingTaskMethods.value = false
                _errorLoadingTaskMethods.value = false
                result.value = it.data
            }
        }

        result
    }

    val methodNames: LiveData<List<String>> = taskMethods.switchMap {
        val names = mutableListOf<String>()
        it.forEach { method -> names.add(method.name) }

        MutableLiveData(names.toList())
    }

    fun saveNewTask(task: Task) = viewModelScope.launch {
        showSnackBarMessage(R.string.schedule_task_snackbar_text)
        newTaskAdded()
        repository.saveTask(task)
    }

    private fun newTaskAdded() {
        _taskAddedEvent.value = Event(true)
    }

    private fun showSnackBarMessage(message: Int) {
        _snackBarText.value = Event(message)
    }
}

@Suppress("UNCHECKED_CAST")
class NewTaskViewModelFactory(
    private val repository: AppRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewTaskViewModel(repository) as T
    }
}
