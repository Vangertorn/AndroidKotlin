package com.example.myapplication

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val notes = arrayListOf<Note>(
            Note("Wash the dishes"),
            Note("Break  neighbour's leg or hand because he makes noises any night","25.03.21"),
            Note("Download film"),
            Note("Change the rubber"),
            Note("Plant potatoes"),
            Note("Call Maks"),
            Note("Repair tap"),
            Note("Clean shoes","26.03.21"),
            Note("Prepare dinner","25.03.21"),
            Note("Go to hairdresser")
    )
    fun addNotes(note: Note){
        notes.add(note)
    }
}

class Note(val title: String, val date: String? = null)

