package com.example.myapplication.models

import androidx.room.Embedded
import androidx.room.Relation

class UserInfo(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "name",
        entityColumn = "userName"
    )
    val notes: MutableList<Note>
)