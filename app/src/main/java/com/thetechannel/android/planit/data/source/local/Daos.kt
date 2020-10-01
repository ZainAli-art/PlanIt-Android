package com.thetechannel.android.planit.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.thetechannel.android.planit.data.Day
import com.thetechannel.android.planit.data.Task
import com.thetechannel.android.planit.data.TaskType
import java.util.*

@Dao
interface DaysDao {
    @Query("SELECT * FROM days")
    fun observeAll(): LiveData<List<Day>>

    @Query("SELECT * FROM days WHERE date = :date")
    fun observeByDate(date: Date): LiveData<Day>

    @Query("SELECT * FROM days")
    suspend fun getAll(): List<Day>

    @Query("SELECT * FROM days WHERE date = :date")
    suspend fun getByDate(date: Date): Day?

    @Insert
    suspend fun insert(day: Day)
}

@Dao
interface TaskTypesDao {
    @Query("SELECT * FROM task_types")
    fun observeAll(): LiveData<List<TaskType>>

    @Query("SELECT * FROM task_types")
    suspend fun getAll(): List<TaskType>

    @Query("SELECT * FROM task_types WHERE id = :id")
    fun observeById(id: Int): LiveData<TaskType>

    @Query("SELECT * FROM task_types WHERE id = :id")
    suspend fun getById(id: Int): TaskType?

    @Insert
    suspend fun insert(taskType: TaskType)
}

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks")
    fun observeAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun observeById(id: String): LiveData<Task>

    @Query("SELECT * FROM tasks WHERE day = :day")
    fun observeByDay(day: Date): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: String): Task?

    @Query("SELECT * FROM tasks WHERE day = :day")
    suspend fun getByDay(day: Date): List<Task>

    @Query("SELECT * FROM tasks")
    suspend fun getAll(): List<Task>

    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)
}