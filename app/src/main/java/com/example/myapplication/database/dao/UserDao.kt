package com.example.myapplication.database.dao

import androidx.room.*
import com.example.myapplication.models.User

@Dao
abstract class UsersDao {
    @Insert
    abstract fun insertUser(user: User) : Long

    @Update
    abstract fun updateUser(user: User)

    @Delete
    abstract fun deleteUser(user: User)

    @Query("SELECT * FROM table_users")
    abstract fun getUsers(): List<User>


}