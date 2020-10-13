package com.thetechannel.android.planit.data.source.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaysDao {
    @Query("SELECT * FROM days")
    fun observeAll(): LiveData<List<DbDay>>

    @Query("SELECT * FROM days WHERE date = :date")
    fun observeByDate(date: Long): LiveData<DbDay>

    @Query("SELECT * FROM days")
    suspend fun getAll(): List<DbDay>

    @Query("SELECT * FROM days WHERE date = :date")
    suspend fun getByDate(date: Long): DbDay?

    @Insert
    suspend fun insert(day: DbDay)
}

@Dao
interface TaskTypesDao {
    @Query("SELECT * FROM task_types")
    fun observeAll(): LiveData<List<DbTaskType>>

    @Query("SELECT * FROM task_types")
    suspend fun getAll(): List<DbTaskType>

    @Query("SELECT * FROM task_types WHERE id = :id")
    fun observeById(id: Int): LiveData<DbTaskType>

    @Query("SELECT * FROM task_types WHERE id = :id")
    suspend fun getById(id: Int): DbTaskType?

    @Insert
    suspend fun insert(taskType: DbTaskType)
}

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks")
    fun observeAll(): LiveData<List<DbTask>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun observeById(id: String): LiveData<DbTask>

    @Query("SELECT * FROM tasks WHERE day = :day")
    fun observeByDay(day: Long): LiveData<List<DbTask>>

    @Query("SELECT " +
                    "t.id AS id, " +
                    "name, " +
                    "start_at AS work_start, " +
                    "(start_at + work_lapse) AS work_end, " +
                    "(start_at + work_lapse) AS break_start, " +
                    "(start_at + work_lapse + break_lapse) AS break_end " +
                "FROM tasks t " +
                "INNER JOIN task_types tp ON t.type_id = tp.id " +
                "WHERE t.id = :id")
    fun observeTaskDetailsByTaskId(id: String): LiveData<DbTaskDetail>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: String): DbTask?

    @Query("SELECT * FROM tasks WHERE day = :day")
    suspend fun getByDay(day: Long): List<DbTask>

    @Query("SELECT * FROM tasks")
    suspend fun getAll(): List<DbTask>

    @Query("SELECT " +
            "t.id AS id, " +
            "name, " +
            "start_at AS work_start, " +
            "(start_at + work_lapse) AS work_end, " +
            "(start_at + work_lapse) AS break_start, " +
            "(start_at + work_lapse + break_lapse) AS break_end " +
            "FROM tasks t " +
            "INNER JOIN task_types tp ON t.type_id = tp.id " +
            "WHERE t.id = :id")
    fun getTaskDetailsByTaskId(id: String): DbTaskDetail

    @Insert
    suspend fun insert(task: DbTask)

    @Delete
    suspend fun delete(task: DbTask)
}