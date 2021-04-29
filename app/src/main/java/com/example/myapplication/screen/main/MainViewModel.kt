package com.example.myapplication.screen.main

import androidx.lifecycle.MutableLiveData
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.asLiveData
import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.models.Note
import com.example.myapplication.repository.CloudRepository
import com.example.myapplication.repository.NotesRepository
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


class MainViewModel(
    private val cloudRepository: CloudRepository,
    private val notesRepository: NotesRepository,
    private val usersRepository: UsersRepository
) : CoroutineViewModel() {
    init {
        userName()
    }

    val progressLiveDate = MutableLiveData<Boolean>()

    val userNameLiveDate = MutableLiveData<String>()

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

    fun exportNotes() = launch {
        progressLiveDate.postValue(cloudRepository.exportNotes())
    }

    fun importNotes() = launch {
        progressLiveDate.postValue(cloudRepository.importNotes())
    }

    fun userName() = launch {
        if (usersRepository.getCurrentUserFlow().first().name != null)
            userNameLiveDate.postValue(usersRepository.getCurrentUserFlow().first().name)
    }


}


