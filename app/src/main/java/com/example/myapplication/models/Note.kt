package com.example.myapplication.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "table_notes")
open class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val date: String? = null,
    val userId: Long = -1,
    val cloud: Boolean = false
) : Parcelable