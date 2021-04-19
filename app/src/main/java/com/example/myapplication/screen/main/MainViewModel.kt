package com.example.myapplication.screen.main

import androidx.lifecycle.MutableLiveData
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.asLiveData
import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.models.Note
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


class MainViewModel(private val notesDao: NotesDao) : CoroutineViewModel() {

    val notesLiveData =
        notesDao.getALLNotesFlow().map { it.reversed() }.flowOn(Dispatchers.IO).asLiveData()

    fun deleteNote(note: Note) {
        launch {
            notesDao.deleteNote(note)
        }
    }


}


