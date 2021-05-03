package com.example.myapplication.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "table_users", indices = [Index(value = ["name"], unique = true)])
data class User(
    val password: String,
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String

) : Parcelable