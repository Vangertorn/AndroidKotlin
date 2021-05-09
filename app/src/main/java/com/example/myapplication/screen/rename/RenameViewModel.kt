package com.example.myapplication.screen.rename

import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RenameViewModel(val usersRepository: UsersRepository) : CoroutineViewModel() {
    fun renameUser(newName: String) {
        launch {
            usersRepository.renameUser(newName, usersRepository.userName.first())
        }
    }
}