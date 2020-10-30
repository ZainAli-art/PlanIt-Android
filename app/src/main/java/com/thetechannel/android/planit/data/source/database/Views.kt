package com.thetechannel.android.planit.data.source.database

import androidx.room.DatabaseView

@DatabaseView("""
    SELECT 
        (SELECT COUNT(*) 
        FROM tasks 
        WHERE completed = 1) AS 'completedTasks',
        (SELECT COUNT(*) 
        FROM tasks 
        WHERE completed = 0) AS 'pendingTasks',
        (SELECT COUNT(*) 
        FROM tasks 
        WHERE completed = 1 AND DATE(tasks.day / 1000, 'unixepoch') = CURRENT_DATE) AS 'tasksCompletedToday'
""")
data class TasksOverView(
    val completedTasks: Int,
    val pendingTasks: Int,
    val tasksCompletedToday: Int
)

@DatabaseView(
    """
    SELECT name, COUNT(*) AS 'count'
    FROM tasks
    JOIN categories ON categories.id = tasks.cat_id
    WHERE DATE(tasks.day / 1000, 'unixepoch') = CURRENT_DATE
    GROUP BY cat_id
    ORDER BY name
"""
)
data class TodayPieDataView(
    val name: String,
    val count: Int
)