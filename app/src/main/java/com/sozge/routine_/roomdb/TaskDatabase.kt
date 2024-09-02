package com.sozge.routine_.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sozge.routine_.model.Task


@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun TaskDao(): TaskDAO
}