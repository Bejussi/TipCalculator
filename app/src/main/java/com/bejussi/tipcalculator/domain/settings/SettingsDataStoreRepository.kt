package com.bejussi.tipcalculator.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreRepository {

    suspend fun setTheme(themeMode: String)

    fun getTheme(): Flow<String>

    suspend fun setLanguage(language: String)

    fun getLanguage(): Flow<String>
}