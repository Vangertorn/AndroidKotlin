package com.example.myapplication.cloud

import com.google.gson.annotations.SerializedName

class CloudNote(
    @SerializedName("id")
    val id: Long = -1,
    @SerializedName("title")
    val title: String,
    @SerializedName("date")
    val date: String?
)

class ExportNotesRequestBody(
    @SerializedName("user")
    val user: CloudUser,
    @SerializedName("phoneId")
    val phoneId: String,
    @SerializedName("notes")
    val notes: List<CloudNote>
)

class CloudUser(
    @SerializedName("id")
    val userId: Long,
    @SerializedName("name")
    val userName: String
)
