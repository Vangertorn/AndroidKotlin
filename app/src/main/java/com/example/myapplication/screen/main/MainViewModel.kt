package com.example.myapplication.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.myapplication.models.Note
import com.example.myapplication.repository.CloudRepository
import com.example.myapplication.repository.NotesRepository
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
class MainViewModel(
    private val cloudRepository: CloudRepository,
    private val notesRepository: NotesRepository,
    private val usersRepository: UsersRepository
) : CoroutineViewModel() {

    val progressLiveDate = MutableLiveData<Boolean>()

    val userNameLiveDate = usersRepository.userName.asLiveData()

    @ExperimentalCoroutinesApi
    val notesLiveData = notesRepository.currentUserNotesFlow.asLiveData()

    fun deleteNote(note: Note) {
        launch {
            notesRepository.deleteNote(note)
        }
    }

    @ExperimentalCoroutinesApi
    fun exportNotes() = launch {
        progressLiveDate.postValue(cloudRepository.exportNotes())
    }

    @ExperimentalCoroutinesApi
    fun importNotes() = launch {
        progressLiveDate.postValue(cloudRepository.importNotes())
    }
}


