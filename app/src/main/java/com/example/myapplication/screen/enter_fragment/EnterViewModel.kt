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
    fun login(user: String) {
        launch {
            try {
                if (user.isNotBlank()) {
                    userRepository.login(user)
                } else {
                    errorLiveData.postValue("Enter user name")
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }
}