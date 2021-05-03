package com.example.myapplication.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "table_notes",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("userName"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
open class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val date: String? = null,
    @ColumnInfo(index = true, name = "userName")
    val userName: String,
    val cloud: Boolean = false
) : Parcelable