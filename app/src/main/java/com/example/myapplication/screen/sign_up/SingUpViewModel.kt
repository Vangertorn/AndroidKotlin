package com.example.myapplication.screen.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.launch

class SingUpViewModel(private val usersRepository: UsersRepository) : CoroutineViewModel() {
    val createNewUserResultLiveData = MutableLiveData<LoginResult>()
    val logGedIn: LiveData<Boolean> = usersRepository.checkUserLoggedIn().asLiveData()

    fun createNewUser(userName: String, password: String, repeatPassword: String) {
        launch {
            if (password != repeatPassword) {
                createNewUserResultLiveData.postValue(LoginResult.PASSWORDS_DO_NOT_MATCH)
            } else if (userName.isNotBlank() && password.isNotBlank()) {
                createNewUserResultLiveData.postValue(
                    usersRepository.createNewUser(
                        userName,
                        password
                    )
                )
            } else {
                createNewUserResultLiveData.postValue(LoginResult.EMPTY_FIELDS)
            }
        }
    }
}