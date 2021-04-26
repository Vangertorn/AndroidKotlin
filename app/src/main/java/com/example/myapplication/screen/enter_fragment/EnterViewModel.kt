package com.example.myapplication.screen.enter_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.launch

class EnterViewModel(private val userRepository: UsersRepository) : CoroutineViewModel() {
    val autoCompleteUserNamesLiveData = userRepository.allUserName.asLiveData()
    val logGedIn: LiveData<Boolean> = userRepository.checkUserLoggedIn().asLiveData()
    val loginResultLiveData = MutableLiveData<LoginResult>()

    fun login(userName: String, password: String) {
        launch {
            if (userName.isNotBlank() && password.isNotBlank()) {
                loginResultLiveData.postValue(userRepository.login(userName, password))
            } else {
                loginResultLiveData.postValue(LoginResult.EMPTY_FIELDS)
            }
        }
    }

    fun createNewUser(userName: String, password: String) {
        launch {
            if (userName.isNotBlank() && password.isNotBlank()) {
                loginResultLiveData.postValue(userRepository.createNewUser(userName, password))
            } else {
                loginResultLiveData.postValue(LoginResult.EMPTY_FIELDS)
            }
        }
    }

    fun deleteUser(userName: String, password: String) {
        launch {
            if (userName.isNotBlank() && password.isNotBlank()) {
                loginResultLiveData.postValue(userRepository.deleteUser(userName, password))
            } else {
                loginResultLiveData.postValue(LoginResult.EMPTY_FIELDS)
            }
        }
    }
}