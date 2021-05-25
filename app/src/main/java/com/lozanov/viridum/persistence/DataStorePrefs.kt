package com.lozanov.viridum.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStorePrefKeys {
    const val ONBOARDING_VALID = "onboarding_valid"
    const val AUTH_VALID = "auth_valid"
}

fun Context.readOnboardingValid(): Flow<Boolean> {
    val onboardingKey = booleanPreferencesKey(DataStorePrefKeys.ONBOARDING_VALID)
    return dataStore.data
        .map { preferences ->
            preferences[onboardingKey] ?: false
        }
}

fun Context.readAuthValid(): Flow<Boolean> {
    val authKey = booleanPreferencesKey(DataStorePrefKeys.AUTH_VALID)
    return dataStore.data
        .map { preferences ->
            preferences[authKey] ?: false
        }
}

suspend fun Context.writeOnboardingValid(valid: Boolean? = null) {
    val onboardingKey = booleanPreferencesKey(DataStorePrefKeys.ONBOARDING_VALID)
    dataStore.edit { settings ->
        val currentValidity = settings[onboardingKey] ?: false
        settings[onboardingKey] = valid ?: !currentValidity
    }
}

suspend fun Context.writeAuthValid(valid: Boolean? = null) {
    val authKey = booleanPreferencesKey(DataStorePrefKeys.AUTH_VALID)
    dataStore.edit { settings ->
        val currentValidity = settings[authKey] ?: false
        settings[authKey] = valid ?: !currentValidity
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prefs")