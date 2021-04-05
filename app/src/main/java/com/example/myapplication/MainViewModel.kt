package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.launch

class MainViewModel : CoroutineViewModel() {

    val listLiveData = MutableLiveData<List<Note>>()

    init {
        launch {
            val list = arrayListOf<Note>(
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
            listLiveData.postValue(list)
        }
    }

    fun addNotes(textNote: String) {
        launch {
            val list = listLiveData.value!!.toMutableList()
            if(textNote.isNotBlank()){
                list.add(0, Note(textNote))
            }

            listLiveData.postValue(list)
        }
    }

}

data class Note(val title: String, val date: String? = null)

