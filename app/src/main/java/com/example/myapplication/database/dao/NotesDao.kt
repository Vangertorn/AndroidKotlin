package com.example.myapplication.database.dao

import androidx.room.*
import com.example.myapplication.models.Note


@Dao
abstract class NotesDao {
    @Insert
    abstract fun insertNote(note: Note): Long

    @Insert
    abstract fun insertNotes(notes: List<Note>)

    @Update
    abstract fun updateNote(note: Note)

    @Delete
    abstract fun deleteNote(note: Note)

    @Query("SELECT * FROM table_notes WHERE id==:noteId LIMIT 1")
    abstract fun getNoteById(noteId: Long): Note?

    @Query("DELETE FROM table_notes")
    abstract fun clearTableNotes()

    @Transaction
    open fun updateTableNotes(notes: List<Note>) {
        clearTableNotes()
        insertNotes(notes)
    }

    @Query("UPDATE table_notes SET cloud = 1")
    abstract fun setAllNotesSyncWithCloud()

}