package com.example.myapplication.repository

import com.example.myapplication.database.dao.UsersDao
import com.example.myapplication.datastore.AppSettings
import com.example.myapplication.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UsersRepository(private val usersDao: UsersDao, private val appSettings: AppSettings) {

    val allUserName = usersDao.getAllUsersName()

    suspend fun login(userName: String) {
        withContext(Dispatchers.IO) {
            if (checkUserExists(userName).not()){
                val userId = usersDao.insertUser(User(name = userName))
                appSettings.setUserID(userId)
            } else{
                val userId = usersDao.getUserId(userName)
                appSettings.setUserID(userId)
            }
        }
    }

    private suspend fun checkUserExists(userName: String): Boolean {
        return withContext(Dispatchers.IO) {
            usersDao.getUserCount(userName) > 0
        }
    }
    fun checkUserLoggedIn(): Flow<Boolean>{
        return appSettings.userIdFlow().map { it>=0 }.flowOn(Dispatchers.IO)
    }
    suspend fun logout(){
        withContext(Dispatchers.IO){
            appSettings.setUserID(-1)
        }
    }
}