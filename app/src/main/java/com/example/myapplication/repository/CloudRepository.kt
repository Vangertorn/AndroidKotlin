package com.example.myapplication.repository

import com.example.myapplication.cloud.CloudInterface
import com.example.myapplication.cloud.CloudNote
import com.example.myapplication.cloud.CloudUser
import com.example.myapplication.cloud.ExportNotesRequestBody
import com.example.myapplication.models.Note
import kotlinx.coroutines.flow.first

class CloudRepository(
    private val cloudInterface: CloudInterface,
    private val usersRepository: UsersRepository,
    private val notesRepository: NotesRepository
) {
    suspend fun exportNotes(): Boolean {
        val user = usersRepository.getCurrentUserFlow().first()
        val notes = notesRepository.getCurrentUserNote()
        val cloudUser = CloudUser(
            userId = user.id,
            userName = user.name
        )
        val cloudNote = notes.map {
            CloudNote(
                id = it.id,
                title = it.title,
                date = it.date
            )
        }
        val exportRequestBody =
            ExportNotesRequestBody(cloudUser, usersRepository.phoneId, cloudNote)
        val exportResult = cloudInterface.exportNotes(exportRequestBody).isSuccessful
        if (exportResult) notesRepository.setAllNotesSyncWithCloud()
        return exportResult
    }

    suspend fun importNotes(): Boolean {
        val user = usersRepository.getCurrentUserFlow().first()
        val response = cloudInterface.importNotes(user.name, usersRepository.phoneId)
        val cloudNotes = response.body() ?: emptyList()
        val notes =
            cloudNotes.map {
                Note(
                    title = it.title,
                    date = it.date,
                    userId = user.id,
                    cloud = true
                )
            }
        notesRepository.insertNotes(notes)
        return response.isSuccessful
    }
}