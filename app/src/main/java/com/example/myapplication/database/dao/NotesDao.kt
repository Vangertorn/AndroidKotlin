package com.example.myapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.models.Note
import kotlinx.coroutines.flow.Flow


@Dao
abstract class NotesDao {
    @Insert
    abstract fun insertNote(note: Note): Long


    @Update
    abstract fun updateNote(note: Note)

    @Delete
    abstract fun deleteNote(note: Note)


    @Query("SELECT * FROM table_notes ORDER BY id DESC")
    abstract fun getALLNotes(): List<Note>


    @Query("SELECT * FROM table_notes WHERE userId == :userId ORDER BY id DESC")
    abstract fun getALLNotesFlowByUserId(userId: Long): Flow<List<Note>>

}