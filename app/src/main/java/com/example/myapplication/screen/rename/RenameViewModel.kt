package com.example.myapplication.screen.rename

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RenameViewModel(val usersRepository: UsersRepository) : CoroutineViewModel() {
    val renameResultLiveData = MutableLiveData<LoginResult>()
    fun renameUser(newName: String) {
        launch {
            if (newName.isNotBlank()){
                renameResultLiveData.postValue(usersRepository.renameUser(newName, usersRepository.userName.first()))
            } else{
                renameResultLiveData.postValue(LoginResult.EMPTY_FIELDS)
            }

        }
    }
}