package com.thetechannel.android.planit.data.source.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categories")
    fun observeAll(): LiveData<List<DbCategory>>

    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<DbCategory>

    @Insert
    suspend fun insert(category: DbCategory)

    @Delete
    suspend fun delete(category: DbCategory)
}

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
interface TaskMethodsDao {
    @Query("SELECT * FROM task_methods")
    fun observeAll(): LiveData<List<DbTaskMethod>>

    @Query("SELECT * FROM task_methods")
    suspend fun getAll(): List<DbTaskMethod>

    @Query("SELECT * FROM task_methods WHERE id = :id")
    fun observeById(id: Int): LiveData<DbTaskMethod>

    @Query("SELECT * FROM task_methods WHERE id = :id")
    suspend fun getById(id: Int): DbTaskMethod?

    @Insert
    suspend fun insert(taskMethod: DbTaskMethod)

    @Update
    suspend fun update(taskMethod: DbTaskMethod)

    @Delete
    suspend fun delete(taskMethod: DbTaskMethod)
}

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks")
    fun observeAll(): LiveData<List<DbTask>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun observeById(id: String): LiveData<DbTask>

    @Query("SELECT * FROM tasks WHERE day = :day")
    fun observeByDay(day: Long): LiveData<List<DbTask>>

    @Query(
        """
        SELECT 
            t.id AS id, 
            c.name AS category, 
            m.name AS method, 
            m.icon_url AS method_icon_url, 
            (m.work_lapse + m.break_lapse) AS time_lapse, 
            t.title AS title, 
            t.start_at AS work_start, 
            (t.start_at + m.work_lapse) AS work_end, 
            (t.start_at + m.work_lapse) AS break_start, 
            (t.start_at + m.work_lapse + m.break_lapse) AS break_end 
        FROM tasks t 
        JOIN categories c ON t.cat_id = c.id 
        JOIN task_methods m ON t.method_id = m.id
        WHERE t.id = :id
    """
    )
    fun observeTaskDetailsByTaskId(id: String): LiveData<DbTaskDetail>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: String): DbTask?

    @Query("SELECT * FROM tasks WHERE day = :day")
    suspend fun getByDay(day: Long): List<DbTask>

    @Query("SELECT * FROM tasks")
    suspend fun getAll(): List<DbTask>

    @Query(
        """
        SELECT 
            t.id AS id, 
            c.name AS category, 
            m.name AS method, 
            m.icon_url AS method_icon_url, 
            (m.work_lapse + m.break_lapse) AS time_lapse, 
            t.title AS title, 
            t.start_at AS work_start, 
            (t.start_at + m.work_lapse) AS work_end, 
            (t.start_at + m.work_lapse) AS break_start, 
            (t.start_at + m.work_lapse + m.break_lapse) AS break_end 
        FROM tasks t 
        JOIN categories c ON t.cat_id = c.id 
        JOIN task_methods m ON t.method_id = m.id
        WHERE t.id = :id
    """
    )
    fun getTaskDetailsByTaskId(id: String): DbTaskDetail

    @Insert
    suspend fun insert(task: DbTask)

    @Delete
    suspend fun delete(task: DbTask)
}