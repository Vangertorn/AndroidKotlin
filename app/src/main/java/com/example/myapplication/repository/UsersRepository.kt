package com.example.myapplication.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.example.myapplication.database.dao.UsersDao
import com.example.myapplication.datastore.AppSettings
import com.example.myapplication.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

enum class LoginResult {
    USER_NOT_EXIST,
    WRONG_PASSWORD,
    NONE,
    EMPTY_FIELDS,
    DELETE_COMPLETED,
    LOGIN_COMPLETED_SUCCESSFULLY,
    USER_ALREADY_EXISTS,
    USER_CREATED_SUCCESSFUL
}

class UsersRepository(
    context: Context,
    private val usersDao: UsersDao,
    private val appSettings: AppSettings
) {

    val allUserName = usersDao.getAllUsersName()

    suspend fun login(userName: String, password: String): LoginResult {
        return withContext(Dispatchers.IO) {
            val userExist = checkUserExists(userName)
            if (!userExist) {
                return@withContext LoginResult.USER_NOT_EXIST
            }
            val userPassword = checkUserPassword(userName, password)
            if (!userPassword) {
                return@withContext LoginResult.WRONG_PASSWORD
            }
            val userId = usersDao.getUserId(userName)
            appSettings.setUserID(userId)
            return@withContext LoginResult.LOGIN_COMPLETED_SUCCESSFULLY
        }
    }

    suspend fun createNewUser(userName: String, password: String): LoginResult {
        return withContext(Dispatchers.IO) {
            val userExist = checkUserExists(userName)
            if (userExist) {
                return@withContext LoginResult.USER_ALREADY_EXISTS
            } else {
                val userId = usersDao.insertUser(User(name = userName, password = password))
                appSettings.setUserID(userId)
                return@withContext LoginResult.USER_CREATED_SUCCESSFUL
            }
        }
    }

    suspend fun deleteUser(userName: String, password: String): LoginResult {
        return withContext(Dispatchers.IO) {
            val userExist = checkUserExists(userName)
            if (!userExist) {
                return@withContext LoginResult.USER_NOT_EXIST
            }
            val userPassword = checkUserPassword(userName, password)
            if (!userPassword) {
                return@withContext LoginResult.WRONG_PASSWORD
            }
            usersDao.deleteUser(usersDao.getUser(userName, password))
            return@withContext LoginResult.DELETE_COMPLETED
        }
    }

    private suspend fun checkUserPassword(userName: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            usersDao.getUserPassword(userName) == password
        }
    }

    private suspend fun checkUserExists(userName: String): Boolean {
        return withContext(Dispatchers.IO) {
            usersDao.getUserCount(userName) > 0
        }
    }

    fun checkUserLoggedIn(): Flow<Boolean> {
        return appSettings.userIdFlow().map { it >= 0 }.flowOn(Dispatchers.IO)
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            appSettings.setUserID(-1)
        }
    }

    fun getCurrentUserFlow(): Flow<User> = appSettings.userIdFlow().flatMapLatest {
        usersDao.getById(it)
    }

    @SuppressLint("HardwareIds")
    val phoneId: String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}