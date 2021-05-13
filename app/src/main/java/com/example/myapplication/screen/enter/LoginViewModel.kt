package com.example.myapplication.screen.enter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val usersRepository: UsersRepository) : CoroutineViewModel() {
    val autoCompleteUserNamesLiveData = usersRepository.allUserName.asLiveData()
    val logGedIn: LiveData<Boolean> = usersRepository.checkUserLoggedIn().asLiveData()
    val loginResultLiveData = MutableLiveData<LoginResult>()

    fun login(userName: String, password: String) {
        launch {
            if (userName.isNotBlank() && password.isNotBlank()) {
                loginResultLiveData.postValue(usersRepository.login(userName, password))
            } else {
                loginResultLiveData.postValue(LoginResult.EMPTY_FIELDS)
            }
        }
    }


}