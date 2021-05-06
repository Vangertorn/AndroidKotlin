package com.example.myapplication.screen.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData

import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class StartViewModel(private val usersRepository: UsersRepository) : CoroutineViewModel() {

    val userNameLiveDate: LiveData<String> =
        usersRepository.userName.map { it ?: "" }.asLiveData()

}