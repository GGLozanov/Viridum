package com.lozanov.viridum.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStorePrefKeys {
    const val ONBOARDING_VALID = "onboarding_valid"
    const val AUTH_TOKEN = "auth_token"
}

fun Context.readOnboardingValid(): Flow<Boolean> {
    val onboardingKey = booleanPreferencesKey(DataStorePrefKeys.ONBOARDING_VALID)
    return dataStore.data
        .map { preferences ->
            preferences[onboardingKey] ?: false
        }
}

fun Context.readAuthToken(): Flow<String?> {
    val authKey = stringPreferencesKey(DataStorePrefKeys.AUTH_TOKEN)
    return dataStore.data
        .map { preferences ->
            preferences[authKey]
        }
}

suspend fun Context.writeOnboardingValid(valid: Boolean? = null) {
    val onboardingKey = booleanPreferencesKey(DataStorePrefKeys.ONBOARDING_VALID)
    dataStore.edit { settings ->
        val currentValidity = settings[onboardingKey] ?: false
        settings[onboardingKey] = valid ?: !currentValidity
    }
}

suspend fun Context.writeAuthToken(token: String) {
    val authKey = stringPreferencesKey(DataStorePrefKeys.AUTH_TOKEN)
    dataStore.edit { settings ->
        settings[authKey] = token
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prefs")