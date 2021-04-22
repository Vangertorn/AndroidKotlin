package com.example.myapplication.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("${BuildConfig.APPLICATION_ID}_datastore")

class AppSettings(context: Context) {
    private val dataStore = context.dataStore

    fun userIdFlow(): Flow<Long> = dataStore.data.map { preferences ->
        preferences[longPreferencesKey(USER_ID_KEY)] ?: -1

    }

    suspend fun userId(): Long = userIdFlow().first()

    suspend fun setUserID(userId: Long) {
        dataStore.edit { preferences ->
            preferences[longPreferencesKey(USER_ID_KEY)] = userId
        }
    }

    companion object {
        private const val USER_ID_KEY = "USER_ID_KEY"
    }
}