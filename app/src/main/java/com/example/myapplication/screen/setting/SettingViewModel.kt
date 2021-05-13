package com.example.myapplication.screen.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.myapplication.models.User
import com.example.myapplication.repository.CloudRepository
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingViewModel(
    private val usersRepository: UsersRepository, private val cloudRepository: CloudRepository
) : CoroutineViewModel() {
    val progressLiveDate = MutableLiveData<Boolean>()
    val userLiveDate = MutableLiveData<User>()
    init {
        getLastUser()
    }

    val userNameLiveDate = usersRepository.userName.asLiveData()
    fun logout(){
        launch {
            usersRepository.logout()
        }
    }

    fun deleteUser() {
        launch {
            val result = cloudRepository.exportEmptyNotes()
            if (result) {
                usersRepository.deleteUser()
            }
            progressLiveDate.postValue(result)
        }
    }

    private fun getLastUser() {
        launch {
            userLiveDate.postValue(usersRepository.getCurrentUserFlow().first())
        }
    }
}