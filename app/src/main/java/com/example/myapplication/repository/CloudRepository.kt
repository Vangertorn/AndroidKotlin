package com.example.myapplication.repository

import com.example.myapplication.cloud.CloudInterface
import com.example.myapplication.cloud.CloudNote
import com.example.myapplication.cloud.CloudUser
import com.example.myapplication.cloud.ExportNotesRequestBody
import com.example.myapplication.models.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first

class CloudRepository(
    private val cloudInterface: CloudInterface,
    private val usersRepository: UsersRepository,
    private val notesRepository: NotesRepository
) {
    @ExperimentalCoroutinesApi
    suspend fun exportNotes(): Boolean {
        val user = usersRepository.getCurrentUserFlow().first()
        val notes = notesRepository.getCurrentUserNote()
        val cloudUser = CloudUser(
            userName = user.name
        )
        val cloudNote = notes.map {
            CloudNote(
                title = it.title,
                date = it.date,
                alarmEnabled = it.alarmEnabled
            )
        }
        val exportRequestBody =
            ExportNotesRequestBody(cloudUser, usersRepository.phoneId, cloudNote)
        val exportResult = cloudInterface.exportNotes(exportRequestBody).isSuccessful
        if (exportResult) notesRepository.setAllNotesSyncWithCloud()
        return exportResult
    }

    @ExperimentalCoroutinesApi
    suspend fun exportEmptyNotes(): Boolean {
        try {
            val user = usersRepository.getCurrentUserFlow().first()
            val notes = listOf<Note>()
            val cloudUser = CloudUser(
                userName = user.name
            )
            val cloudNote = notes.map {
                CloudNote(
                    title = it.title,
                    date = it.date,
                    alarmEnabled = it.alarmEnabled
                )
            }
            val exportRequestBody =
                ExportNotesRequestBody(cloudUser, usersRepository.phoneId, cloudNote)
            return cloudInterface.exportNotes(exportRequestBody).isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    @ExperimentalCoroutinesApi
    suspend fun importNotes(): Boolean {
        val user = usersRepository.getCurrentUserFlow().first()
        val response = cloudInterface.importNotes(user.name, usersRepository.phoneId)
        val cloudNotes = response.body() ?: emptyList()
        val notes =
            cloudNotes.map {
                Note(
                    title = it.title,
                    date = it.date,
                    userName = user.name,
                    cloud = true,
                    alarmEnabled = it.alarmEnabled
                )
            }
        notesRepository.updateNotes(notes)
        return response.isSuccessful
    }
}