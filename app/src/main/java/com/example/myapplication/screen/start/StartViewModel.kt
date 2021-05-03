package com.example.myapplication.screen.start

import androidx.lifecycle.MutableLiveData

import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class StartViewModel(private val usersRepository: UsersRepository) : CoroutineViewModel() {
    init {
        userName()
    }

    val userNameLiveDate = MutableLiveData<String>()

    @ExperimentalCoroutinesApi
    fun userName() = launch {
        val name = usersRepository.getCurrentUserFlow().first()?.name
        if (name != null) {
            userNameLiveDate.postValue(name!!)
        } else {
            userNameLiveDate.postValue("")
        }
    }
}