package com.thetechannel.android.planit.newtask

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.thetechannel.android.planit.Event
import com.thetechannel.android.planit.R
import com.thetechannel.android.planit.data.Result
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.*

private const val TAG = "NewTaskViewModel"

class NewTaskViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _saveProcessing = MutableLiveData<Boolean>(false)
    val saveProcessing: LiveData<Boolean>
        get() = _saveProcessing

    private val _reminderTimeMillis = MutableLiveData<Event<Long>>()
    val reminderTimeMillis: LiveData<Event<Long>>
        get() = _reminderTimeMillis

    val selectedCategoryIndex = MutableLiveData<Int>()
    val selectedTaskMethodIndex = MutableLiveData<Int>()
    val taskTitle = MutableLiveData<String>()
    private val _selectedDay = MutableLiveData<Date>()
    private val _selectedTime = MutableLiveData<Time>()

    private val _taskAddedEvent = MutableLiveData<Event<Boolean>>()

    val taskAddedEvent: MutableLiveData<Event<Boolean>>
        get() = _taskAddedEvent

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>>
        get() = _snackBarText

    private val _errorLoadingCategories = MutableLiveData<Boolean>()

    private val _loadingCategories = MutableLiveData<Boolean>()

    private val _loadingTaskMethods = MutableLiveData<Boolean>()

    private val _scheduleTaskEvent = MutableLiveData<Event<Boolean>>()
    val scheduleTaskEvent: LiveData<Event<Boolean>>
        get() = _scheduleTaskEvent

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

        it.forEach { category ->
            names.add(category.name)
        }

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

        it.forEach { method ->
            names.add(method.name)
        }

        MutableLiveData(names.toList())
    }

    fun scheduleTask() {
        _scheduleTaskEvent.value = Event(true)
    }

    fun selectDay(day: Date) {
        _selectedDay.value = day
    }

    fun saveNewTask() {
        _saveProcessing.value = true

        val day = _selectedDay.value
        val startAt = _selectedTime.value

        viewModelScope.launch {
            val methodList = repository.getTaskMethods(false)
            val catList = repository.getCategories(false)

            if (methodList !is Result.Success || catList !is Result.Success) {
                TODO("show some error")
            }

            val methodId = methodList.data[selectedTaskMethodIndex.value!!].id
            val catId = catList.data[selectedCategoryIndex.value!!].id
            val title = taskTitle.value

            if (day == null || startAt == null || title == null) {
                TODO("show some error")
            }

            val task = Task(
                day = day,
                startAt = startAt,
                methodId = methodId,
                title = title,
                catId = catId,
                completed = false
            )

            Log.i(TAG, "saveNewTask: $task")

            _reminderTimeMillis.value = Event(task.startAt.time)
            showSnackBarMessage(R.string.schedule_task_snackbar_text)
            saveNewTask(task)
        }
    }

    suspend fun saveNewTask(task: Task) {
        repository.saveTask(task)
        newTaskAdded()
    }

    private fun newTaskAdded() {
        _saveProcessing.value = false
        _taskAddedEvent.postValue(Event(true))
    }

    private fun showSnackBarMessage(@StringRes message: Int) {
        _snackBarText.value = Event(message)
    }

    fun selectTime(time: Time) {
        _selectedTime.value = time
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
