package com.bejussi.tipcalculator.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.bejussi.tipcalculator.domain.settings.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SettingsDataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): SettingsDataStoreRepository {

    companion object {
        val themeModeKey = stringPreferencesKey("THEME_MODE_KEY")
        val languageKey = stringPreferencesKey("LANGUAGE_KEY")
    }

    override suspend fun setTheme(themeMode: String) {
        dataStore.edit { preferences ->
            preferences[themeModeKey] = themeMode
        }
    }

    override fun getTheme(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val uiMode = preferences[themeModeKey] ?: "light"
                uiMode
            }
    }

    override suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[languageKey] = language
        }
    }

    override fun getLanguage(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val uiMode = preferences[languageKey] ?: "en"
                uiMode
            }
    }
}