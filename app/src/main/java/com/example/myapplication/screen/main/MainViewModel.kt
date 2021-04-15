package com.example.myapplication.screen.main

import androidx.lifecycle.MutableLiveData
import android.os.Parcel
import android.os.Parcelable
import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.models.Note
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.*


class MainViewModel(private val notesDao: NotesDao) : CoroutineViewModel() {


    val listLiveData = MutableLiveData<List<Note>>(listOf())


    fun deleteNote(note: Note) {
        launch {
            notesDao.deleteNote(note)
        }
        invalidateList()
    }

    fun invalidateList() {
        launch {
            val notes = notesDao.getALLNotes()
            listLiveData.postValue(notes)
        }
    }


}


