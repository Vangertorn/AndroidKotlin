package com.example.myapplication.screen.main

import androidx.lifecycle.MutableLiveData

import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.async

import kotlinx.coroutines.launch
import kotlinx.coroutines.yield


class MainViewModel : CoroutineViewModel() {

    val listLiveData = MutableLiveData<List<Note>>(
        arrayListOf(
            Note("Wash the dishes"),
            Note(
                "Break  neighbour's leg or hand because he makes noises any night",
                "25.03.21"
            ),
            Note("Download film"),
            Note("Change the rubber"),
            Note("Plant potatoes"),
            Note("Call Maks"),
            Note("Repair tap"),
            Note("Clean shoes", "26.03.21"),
            Note("Prepare dinner", "25.03.21"),
            Note("Go to hairdresser")
        )
    )


    fun addNotes(textNote: String, date: String? = null) {


        launch {

            val list = listLiveData.value!!.toMutableList()
            list.add(0, Note(textNote, date))
            listLiveData.postValue(list)

        }
    }


}

data class Note(val title: String, val date: String? = null)

