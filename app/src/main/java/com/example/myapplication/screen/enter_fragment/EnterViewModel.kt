package com.example.myapplication.screen.enter_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class EnterViewModel(private val userRepository: UsersRepository) : CoroutineViewModel() {
    val autoCompleteUserNamesLiveData = userRepository.allUserName.asLiveData()
    val logGedIn: LiveData<Boolean> = userRepository.checkUserLoggedIn().asLiveData()
    val errorLiveData = MutableLiveData<String>()

    fun login(userName: String, password: String) {
        launch {
            try {
                if (userName.isNotBlank() && password.isNotBlank()) {
                    userRepository.login(userName, password)
                } else {
                    errorLiveData.postValue("Enter user name and password")
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }

    fun createNewUser(userName: String, password: String) {
        launch {
            try {
                if (userName.isNotBlank() && password.isNotBlank()) {
                    userRepository.createNewUser(userName, password)
                } else {
                    errorLiveData.postValue("Enter user name and password")
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }

    fun deleteUser(userName: String, password: String) {
        launch {
            try {
                if (userName.isNotBlank() && password.isNotBlank()) {
                    userRepository.deleteUser(userName, password)
                } else {
                    errorLiveData.postValue("Enter user name and password")
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }
}