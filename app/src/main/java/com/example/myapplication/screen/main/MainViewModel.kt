package com.example.myapplication.screen.main

import androidx.lifecycle.MutableLiveData
import android.os.Parcel
import android.os.Parcelable
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.*


class MainViewModel : CoroutineViewModel() {

    val listLiveData = MutableLiveData(
        listOf(
            Note(1, "Wash the dishes"),
            Note(2, "Break  neighbour's leg or hand because he makes noises any night", "25.03.21"),
            Note(3, "Download film"),
            Note(4, "Change the rubber"),
            Note(5, "Plant potatoes"),
            Note(6, "Call Maks"),
            Note(7, "Repair tap"),
            Note(8, "Clean shoes", "26.03.21"),
            Note(9, "Prepare dinner", "25.03.21"),
            Note(10, "Go to hairdresser")
        )
    )


    fun addNote(note: Note) {
        launch {
            val list = listLiveData.value!!.toMutableList()
            list.add(0, Note((list.maxByOrNull { it.id }?.id ?: 0) + 1, note.title, note.date))
            listLiveData.postValue(list)
        }
    }

    fun editNote(note: Note) {
        launch {
            val list = listLiveData.value!!.toMutableList()
            val position = list.indexOfFirst { it.id == note.id }
            list.removeAt(position)
            list.add(position, note)
            listLiveData.postValue(list)
        }
    }

    fun deleteNote(position: Int) {
        launch {
            val list = listLiveData.value!!.toMutableList()
            list.removeAt(position)
            listLiveData.postValue(list)
        }
    }


}

class Note(
    val id: Int,
    val title: String,
    val date: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }

}

