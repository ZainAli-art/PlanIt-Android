package com.thetechannel.android.planit.data.source.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categories ORDER BY name")
    fun observeAll(): LiveData<List<DbCategory>>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun observeById(id: Int): LiveData<DbCategory>

    @Query("SELECT * FROM categories ORDER BY name")
    suspend fun getAll(): List<DbCategory>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: Int): DbCategory?

    @Insert
    suspend fun insert(category: DbCategory)

    @Delete
    suspend fun delete(category: DbCategory)

    @Query("DELETE FROM categories")
    suspend fun deleteAll()
}

@Dao
interface TaskMethodsDao {
    @Query("SELECT * FROM task_methods ORDER BY name")
    fun observeAll(): LiveData<List<DbTaskMethod>>

    @Query("SELECT * FROM task_methods ORDER BY name")
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

    @Query("DELETE FROM task_methods")
    suspend fun deleteAll()
}

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks ORDER BY day, start_at")
    fun observeAll(): LiveData<List<DbTask>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun observeById(id: String): LiveData<DbTask>

    @Query("SELECT * FROM tasks WHERE day = :day ORDER BY start_at")
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

    @Query("SELECT * FROM TasksOverView")
    fun observeTasksOverView(): LiveData<TasksOverView>

    @Query("SELECT * FROM TodayProgress")
    fun observeTodayProgress(): LiveData<TodayProgress>

    @Query("SELECT * FROM TodayPieDataView")
    fun observeTodayPieDataViews(): LiveData<List<TodayPieDataView>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: String): DbTask?

    @Query("SELECT * FROM tasks WHERE day = :day ORDER BY start_at")
    suspend fun getByDay(day: Long): List<DbTask>

    @Query("SELECT * FROM tasks ORDER BY day, start_at")
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
    suspend fun getTaskDetailsByTaskId(id: String): DbTaskDetail

    @Query("SELECT * FROM TasksOverView")
    suspend fun getTasksOverView(): TasksOverView

    @Query("SELECT * FROM TodayProgress")
    suspend fun getTodayProgress(): TodayProgress

    @Query("SELECT * FROM TodayPieDataView")
    suspend fun getTodayPieDataViews(): List<TodayPieDataView>

    @Insert
    suspend fun insert(task: DbTask)

    @Query("UPDATE tasks SET completed = :completed WHERE id = :id")
    suspend fun updateCompleted(id: String, completed: Boolean)

    @Delete
    suspend fun delete(task: DbTask)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()
}