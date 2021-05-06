package com.example.myapplication.screen.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.myapplication.repository.CloudRepository
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingViewModel(
    private val usersRepository: UsersRepository, private val cloudRepository: CloudRepository
) : CoroutineViewModel() {
    val progressLiveDate = MutableLiveData<Boolean>()

    val userNameLiveDate = usersRepository.userName.map { it ?: "" }.asLiveData()
    fun logout() {
        launch {
            usersRepository.logout()
        }
    }

    fun deleteUser() {
        launch {
            progressLiveDate.postValue(cloudRepository.exportEmptyNotes())
            usersRepository.deleteUser()
        }
    }

}