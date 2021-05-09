package com.example.myapplication.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.example.myapplication.R
import com.example.myapplication.database.dao.UsersDao
import com.example.myapplication.datastore.AppSettings
import com.example.myapplication.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class LoginResult(val toast: Int) {

    USER_NOT_EXIST(R.string.User_not_exist),
    WRONG_PASSWORD(R.string.Wrong_password),
    NONE(0),
    EMPTY_FIELDS(R.string.Empty_fields),
    LOGIN_COMPLETED_SUCCESSFULLY(R.string.Login_completed_successfully),
    USER_ALREADY_EXISTS(R.string.User_already_exists),
    USER_CREATED_SUCCESSFUL(R.string.User_created_successful),
    PASSWORDS_DO_NOT_MATCH(R.string.Passwords_do_not_match);
}

class UsersRepository(
    context: Context,
    private val usersDao: UsersDao,
    private val appSettings: AppSettings
) {

    val allUserName = usersDao.getAllUsersName()
    val userName = appSettings.userNameFlow()

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
            appSettings.setUserName(userName)
            return@withContext LoginResult.LOGIN_COMPLETED_SUCCESSFULLY
        }
    }

    suspend fun createNewUser(userName: String, password: String): LoginResult {
        return withContext(Dispatchers.IO) {
            val userExist = checkUserExists(userName)
            if (userExist) {
                return@withContext LoginResult.USER_ALREADY_EXISTS
            } else {
                appSettings.setUserName(userName)
                usersDao.insertUser(User(password, userName))
                return@withContext LoginResult.USER_CREATED_SUCCESSFUL
            }
        }
    }

    suspend fun deleteUser() {
        withContext(Dispatchers.IO) {

            usersDao.deleteUser(usersDao.getUser(appSettings.userName()))
            logout()
        }
    }

    suspend fun renameUser(newName: String, oldName: String) {
        withContext(Dispatchers.IO) {
            usersDao.renameUser(newName,oldName)
            appSettings.setUserName(newName)
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
        return appSettings.userNameFlow().map { it }.map { it.isNotEmpty() }
            .flowOn(Dispatchers.IO)
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            appSettings.setUserName("")
        }
    }


    @ExperimentalCoroutinesApi
    fun getCurrentUserFlow(): Flow<User> = appSettings.userNameFlow().flatMapLatest {
        usersDao.getByNameFlow(it)
    }


    @SuppressLint("HardwareIds")
    val phoneId: String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}