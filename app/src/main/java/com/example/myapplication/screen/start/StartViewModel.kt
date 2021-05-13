package com.example.myapplication.screen.start

import androidx.lifecycle.LiveData

import androidx.lifecycle.asLiveData

import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class StartViewModel(private val usersRepository: UsersRepository) : CoroutineViewModel() {

    val userNameLiveDate: LiveData<String> =
        usersRepository.userName.asLiveData()

}