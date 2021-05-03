package com.example.myapplication.screen.setting

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datastore.AppSettings
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingViewModel(
    private val usersRepository: UsersRepository,
) : CoroutineViewModel() {
    init {
        userName()
    }
    val userNameLiveDate = MutableLiveData<String>()
    fun logout() {
        launch {
            usersRepository.logout()
        }
    }

    fun deleteUser() {
        launch {
            usersRepository.deleteUser()
        }
    }

    fun userName() {
        launch {
            usersRepository.getCurrentUserFlow()
                .first()?.name?.let { userNameLiveDate.postValue(it) }
        }
    }
}