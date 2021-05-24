package com.lozanov.viridum.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStorePrefKeys {
    const val ONBOARDING_VALID = "onboarding_valid"
}

fun Context.readOnboaridingValid(): Flow<Boolean> {
    val onboardingKey = booleanPreferencesKey(DataStorePrefKeys.ONBOARDING_VALID)
    return dataStore.data
        .map { preferences ->
            preferences[onboardingKey] ?: false
        }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prefs")