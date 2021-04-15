package com.example.myapplication.database.dao

import androidx.room.*
import com.example.myapplication.models.Note


@Dao
abstract class NotesDao {
    @Insert
    abstract fun insertNote(note: Note): Long
    @Insert
    abstract fun insertNotes(notes: List<Note>): List<Long>
    @Update
    abstract fun updateNote(note: Note)
    @Update
    abstract fun updateNotes(notes: List<Note>)
    @Delete
    abstract fun deleteNote(note: Note)
    @Delete
    abstract fun deleteNotes(notes: List<Note>)
    @Query("SELECT*FROM table_notes")
    abstract fun getALLNotes():List<Note>

}