package com.dicoding.diva.pimpledetectku.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USERID_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun saveUser(userModel: UserModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = userModel.token
            preferences[NAME_KEY] = userModel.name
            preferences[STATE_KEY] = userModel.isLogin
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[TOKEN_KEY] ?:"",
                preferences[NAME_KEY] ?:"",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun login(userModel : UserModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = userModel.token
            preferences[NAME_KEY] = userModel.name
            preferences[STATE_KEY] = userModel.isLogin
        }
    }
}