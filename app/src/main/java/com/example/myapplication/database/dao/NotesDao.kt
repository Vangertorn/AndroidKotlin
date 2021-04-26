package com.example.myapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.models.Note
import kotlinx.coroutines.flow.Flow


@Dao
abstract class NotesDao {
    @Insert
    abstract fun insertNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNotes(notes: List<Note>)

    @Update
    abstract fun updateNote(note: Note)

    @Delete
    abstract fun deleteNote(note: Note)


    @Query("SELECT * FROM table_notes WHERE userId == :userId ORDER BY id DESC")
    abstract fun getALLNotesFlowByUserId(userId: Long): Flow<List<Note>>

    @Query("SELECT * FROM table_notes WHERE userId==:userId ORDER BY id DESC")
    abstract fun getAllNotesByUserId(userId: Long): List<Note>

    @Query("UPDATE table_notes SET cloud = 1")
    abstract fun setAllNotesSyncWithCloud()

}