package com.example.myapplication.database.dao

import androidx.room.*
import com.example.myapplication.models.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UsersDao {
    @Insert
    abstract fun insertUser(user: User): Long

    @Update
    abstract fun updateUser(user: User)

    @Delete
    abstract fun deleteUser(user: User)

    @Query("SELECT COUNT(*) FROM table_users WHERE name==:userName")
    abstract fun getUserCount(userName: String): Int

    @Query("SELECT name FROM table_users")
    abstract fun getAllUsersName(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM table_users WHERE name == :userName")
    abstract fun getUsersCountFlow(userName: String): Flow<Int>

    @Query("SELECT id FROM table_users WHERE name == :userName")
    abstract fun getUserId(userName: String): Long

}