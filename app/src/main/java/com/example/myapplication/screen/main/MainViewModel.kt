package com.example.myapplication.screen.main

import androidx.lifecycle.MutableLiveData
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.asLiveData
import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.models.Note
import com.example.myapplication.repository.NotesRepository
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


class MainViewModel(
    private val notesRepository: NotesRepository,
    private val usersRepository: UsersRepository
) : CoroutineViewModel() {

    val notesLiveData = notesRepository.currentUserNotesFlow.asLiveData()

    fun deleteNote(note: Note) {
        launch {
            notesRepository.deleteNote(note)
        }
    }

    fun logout() {
        launch {
            usersRepository.logout()
        }
    }


}


