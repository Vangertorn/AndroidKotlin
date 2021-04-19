package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.database.dao.UsersDao
import com.example.myapplication.models.Note
import com.example.myapplication.models.User

@Database(
    entities = [
        Note::class,
        User::class
    ],
    version = 2,
    exportSchema = false
)

abstract class PlannerDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun usersDao(): UsersDao
}

object DatabaseConstructor {
    fun create(context: Context): PlannerDatabase {
        return Room.databaseBuilder(
            context,
            PlannerDatabase::class.java,
            "com.example.myapplication.dp"
        ).build()
    }
}