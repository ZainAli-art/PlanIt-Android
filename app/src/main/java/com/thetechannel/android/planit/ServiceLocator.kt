package com.thetechannel.android.planit

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.thetechannel.android.planit.data.source.AppDataSource
import com.thetechannel.android.planit.data.source.AppRepository
import com.thetechannel.android.planit.data.source.DefaultAppRepository
import com.thetechannel.android.planit.data.source.database.LocalDataSource
import com.thetechannel.android.planit.data.source.database.PlanItDatabase
import com.thetechannel.android.planit.data.source.network.FakeRemoteDataSource

object ServiceLocator {
    private val lock = Any()
    private var database: PlanItDatabase? = null
    private var remoteDataSource: AppDataSource? = null

    @Volatile
    var repository: AppRepository? = null

    fun provideRepository(context: Context): AppRepository {
        return repository ?: createAppRepository(context)
    }

    private fun createAppRepository(context: Context): AppRepository {
        val newRepo = DefaultAppRepository(
            createLocalDataSource(context),
            createRemoteDataSource()
        )
        repository = newRepo
        return newRepo
    }

    private fun createRemoteDataSource(): AppDataSource {
        return remoteDataSource ?: FakeRemoteDataSource.also {
            remoteDataSource = it
        }
    }

    private fun createLocalDataSource(context: Context): AppDataSource {
        val db = database ?: createDatabase(context)
        return LocalDataSource(db.categoriesDao, db.taskMethodsDao, db.tasksDao)
    }

    private fun createDatabase(context: Context): PlanItDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            PlanItDatabase::class.java, "planit_db"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
//            runBlocking {
//                remoteDataSource.deleteAllCategories()
//                remoteDataSource.deleteAllTaskMethods()
//                remoteDataSource.deleteAllTasks()
//            }
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            repository = null
        }
    }
}