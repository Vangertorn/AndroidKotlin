package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.models.Note

@Database(
    entities = [
        Note::class
    ],
    version = 1,
    exportSchema = false
)

abstract class PlannerDatabase: RoomDatabase(){
    abstract fun notesDao(): NotesDao
}

object DatabaseConstructor{
    fun create(context: Context): PlannerDatabase{
        return Room.databaseBuilder(context,PlannerDatabase::class.java,"com.example.myapplication.dp").build()
    }
}